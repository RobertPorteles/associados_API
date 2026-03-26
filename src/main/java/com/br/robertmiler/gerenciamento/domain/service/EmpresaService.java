package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EmpresaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EmpresaResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.Empresa;
import com.br.robertmiler.gerenciamento.domain.entities.EmpresaEnderecoComercial;
import com.br.robertmiler.gerenciamento.domain.exceptions.JaCadastradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;
import com.br.robertmiler.gerenciamento.domain.mappers.EmpresaMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.PaginacaoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EmpresaEnderecoComercialRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EmpresaRepository;

@Service
public class EmpresaService {

	@Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private EmpresaMapper empresaMapper;

    @Autowired
    private EmpresaEnderecoComercialRepository enderecoComercialRepository;

    @Autowired
    private PaginacaoMapper paginacaoMapper;
	
	@Transactional
	public EmpresaResponseDto criar(EmpresaRequestDto requestDto) {
       
        Associado associado = associadoRepository.findById(requestDto.getIdAssociado())
                .orElseThrow(() -> new NaoEncontradoException("Associado não encontrado."));

        Empresa empresa = empresaMapper.toEntity(requestDto, associado);
        
        empresaRepository.save(empresa);

        return empresaMapper.montarDtoResposta(empresa);
    }

	public EmpresaResponseDto buscarEmpresaPorId(Long idEmpresa) {
		var empresaFound = buscarEmpresaEntity(idEmpresa);

		return empresaMapper.montarDtoResposta(empresaFound);
	}

	public Empresa buscarEmpresaEntity(Long idEmpresa) {
		return empresaRepository.findById(idEmpresa)
				.orElseThrow(() -> new NaoEncontradoException("Empresa não encontrada."));
	}

	@Transactional
	public EmpresaResponseDto editarEmpresa(Long idEmpresa, EmpresaRequestDto requestDto) {

		var empresa = buscarEmpresaEntity(idEmpresa);

		var cnpjExistente = empresaRepository.findByCnpj(requestDto.getCnpj());
		if (cnpjExistente.isPresent() && !cnpjExistente.get().getIdEmpresa().equals(idEmpresa)) {
			throw new JaCadastradoException("CNPJ já cadastrado para outra empresa.");
		}

		empresa.setRazaoSocial(requestDto.getRazaoSocial());
		empresa.setNomeFantasia(requestDto.getNomeFantasia());
		empresa.setCnpj(requestDto.getCnpj());

		empresaRepository.save(empresa);

		// 9.2 - Editar Endereço Comercial (opcional: só atualiza se rua for informada)
		if (requestDto.getRua() != null && !requestDto.getRua().isBlank()) {
			var enderecoOpt = enderecoComercialRepository.findByEmpresa_IdEmpresa(idEmpresa);
			if (enderecoOpt.isPresent()) {
				var endereco = enderecoOpt.get();
				endereco.setRua(FormataString.primeiraLetraMaiuscula(requestDto.getRua()));
				endereco.setNumero(requestDto.getNumero());
				endereco.setComplemento(requestDto.getComplemento());
				endereco.setBairro(FormataString.primeiraLetraMaiuscula(requestDto.getBairro()));
				endereco.setCidade(FormataString.primeiraLetraMaiuscula(requestDto.getCidade()));
				endereco.setEstado(FormataString.primeiraLetraMaiuscula(requestDto.getEstado()));
				endereco.setCep(requestDto.getCep());
				enderecoComercialRepository.save(endereco);
			} else {
				var novoEndereco = new EmpresaEnderecoComercial();
				novoEndereco.setRua(FormataString.primeiraLetraMaiuscula(requestDto.getRua()));
				novoEndereco.setNumero(requestDto.getNumero());
				novoEndereco.setComplemento(requestDto.getComplemento());
				novoEndereco.setBairro(FormataString.primeiraLetraMaiuscula(requestDto.getBairro()));
				novoEndereco.setCidade(FormataString.primeiraLetraMaiuscula(requestDto.getCidade()));
				novoEndereco.setEstado(FormataString.primeiraLetraMaiuscula(requestDto.getEstado()));
				novoEndereco.setCep(requestDto.getCep());
				novoEndereco.setEmpresa(empresa);
				enderecoComercialRepository.save(novoEndereco);
			}
		}

		return empresaMapper.montarDtoResposta(empresa);
	}

	@Transactional(readOnly = true)
	public PaginacaoResponseDto<EmpresaResponseDto> buscarEmpresasPorAssociado(Long idAssociado, Integer number, Integer size) {
		var pageable = PageRequest.of(number, size);
		var empresasFound = empresaRepository.findByAssociado_IdAssociado(idAssociado, pageable);
		var page = empresasFound.map(empresaMapper::montarDtoResposta);
		return paginacaoMapper.montarDtoResposta(page);
	}
}
