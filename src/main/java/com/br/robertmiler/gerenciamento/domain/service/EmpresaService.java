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
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.EmpresaMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.PaginacaoMapper;
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

	@Transactional(readOnly = true)
	public PaginacaoResponseDto<EmpresaResponseDto> buscarEmpresasPorAssociado(Long idAssociado, Integer number, Integer size) {
		var pageable = PageRequest.of(number, size);
		var empresasFound = empresaRepository.findByAssociado_IdAssociado(idAssociado, pageable);
		var page = empresasFound.map(empresaMapper::montarDtoResposta);
		return paginacaoMapper.montarDtoResposta(page);
	}
}
