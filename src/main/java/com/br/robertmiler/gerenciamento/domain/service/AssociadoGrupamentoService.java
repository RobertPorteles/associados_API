package com.br.robertmiler.gerenciamento.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoGrupamentoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoGrupamentoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoGrupamento;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoGrupamentoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoGrupamentoRepository;

@Service
public class AssociadoGrupamentoService {

    @Autowired
    private AssociadoGrupamentoRepository associadoGrupamentoRepository;

    @Autowired
    private AssociadoGrupamentoMapper associadoGrupamentoMapper;

    @Autowired
    private GrupamentoEstrategicoService grupamentoService;

    @Transactional
    public AssociadoGrupamentoResponseDto vincularGrupamento(AssociadoGrupamentoRequestDto request) {
        var vinculo = associadoGrupamentoMapper.toEntity(request);
        associadoGrupamentoRepository.save(vinculo);
        return associadoGrupamentoMapper.toResponse(vinculo);
    }

    @Transactional
    public AssociadoGrupamentoResponseDto editarVinculo(Long idAssociadoGrupamento, AssociadoGrupamentoRequestDto request) {
        var vinculo = buscarVinculoEntity(idAssociadoGrupamento);

        var novoGrupamento = grupamentoService.buscarGrupamentoEntity(request.getIdGrupamento());
        vinculo.setGrupamento(novoGrupamento);
        vinculo.setDataInicio(request.getDataInicio());
        vinculo.setDataFim(request.getDataFim());
        vinculo.setAtivo(request.getAtivo());

        associadoGrupamentoRepository.save(vinculo);
        return associadoGrupamentoMapper.toResponse(vinculo);
    }

    @Transactional(readOnly = true)
    public AssociadoGrupamentoResponseDto buscarVinculoPorId(Long idAssociadoGrupamento) {
        var vinculo = buscarVinculoEntity(idAssociadoGrupamento);
        return associadoGrupamentoMapper.toResponse(vinculo);
    }

    @Transactional(readOnly = true)
    public AssociadoGrupamento buscarVinculoEntity(Long idAssociadoGrupamento) {
        return associadoGrupamentoRepository.findById(idAssociadoGrupamento)
                .orElseThrow(() -> new NaoEncontradoException("Vínculo de grupamento não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<AssociadoGrupamentoResponseDto> buscarGrupamentosPorAssociado(Long idAssociado) {
        return associadoGrupamentoRepository.findByAssociado_IdAssociado(idAssociado)
                .stream()
                .map(associadoGrupamentoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AssociadoGrupamentoResponseDto> buscarAssociadosPorGrupamento(Long idGrupamento) {
        return associadoGrupamentoRepository.findByGrupamento_IdGrupamento(idGrupamento)
                .stream()
                .map(associadoGrupamentoMapper::toResponse)
                .collect(Collectors.toList());
    }

}
