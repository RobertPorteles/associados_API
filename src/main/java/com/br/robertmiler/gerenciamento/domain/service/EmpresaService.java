package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EmpresaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EmpresaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.Empresa;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.EmpresaMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EmpresaRepository;

@Service
public class EmpresaService {

	@Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private EmpresaMapper empresaMapper;

	

	@Transactional
	public EmpresaResponseDto criar(EmpresaRequestDto requestDto) {
       
        Associado associado = associadoRepository.findById(requestDto.getIdAssociado())
                .orElseThrow(() -> new NaoEncontradoException("Associado não encontrado."));

        /*
		ToEntity converte de request para entidade, toResponse de Entidade para response.
		Botei pra pegar o associado no paramentro e salvar!
		*/
        Empresa empresa = empresaMapper.toEntity(requestDto, associado);

        
        empresaRepository.save(empresa);

        return empresaMapper.toResponse(empresa);
    }

	public EmpresaResponseDto buscarEmpresaPorId(Long idEmpresa) {
		var empresaFound = buscarEmpresaEntity(idEmpresa);

		EmpresaResponseDto response = new EmpresaResponseDto();
		response.setIdEmpresa(empresaFound.getIdEmpresa());
		response.setRazaoSocial(empresaFound.getRazaoSocial());
		response.setCnpj(empresaFound.getCnpj());
		response.setNomeFantasia(empresaFound.getNomeFantasia());
		response.setCargo(empresaFound.getCargo());
		response.setNomeAssociado(empresaFound.getAssociado().getNomeCompleto());

		return response;
	}

	public Empresa buscarEmpresaEntity(Long idEmpresa) {
		return empresaRepository.findById(idEmpresa)
				.orElseThrow(() -> new NaoEncontradoException("Empresa não encontrada."));
	}

}
