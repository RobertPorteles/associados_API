package com.br.robertmiler.gerenciamento.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoCargoLiderancaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoCargoLiderancaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoCargoLideranca;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoCargoLiderancaMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoCargoLiderancaRepository;

@Service
public class AssociadoCargoLiderancaService {

    @Autowired
    private AssociadoCargoLiderancaRepository associadoCargoRepository;

    @Autowired
    private AssociadoCargoLiderancaMapper associadoCargoMapper;

    @Autowired
    private CargoLiderancaService cargoLiderancaService;

    @Transactional
    public AssociadoCargoLiderancaResponseDto designarCargo(AssociadoCargoLiderancaRequestDto request) {
        var designacao = associadoCargoMapper.toEntity(request);
        associadoCargoRepository.save(designacao);
        return associadoCargoMapper.toResponse(designacao);
    }

    @Transactional
    public AssociadoCargoLiderancaResponseDto editarDesignacao(Long idAssociadoCargo, AssociadoCargoLiderancaRequestDto request) {
        var designacao = buscarDesignacaoEntity(idAssociadoCargo);

        var novoCargo = cargoLiderancaService.buscarCargoEntity(request.getIdCargoLideranca());
        designacao.setCargoLideranca(novoCargo);
        designacao.setDataInicio(request.getDataInicio());
        designacao.setDataFim(request.getDataFim());
        if (request.getAtivo() != null) {
            designacao.setAtivo(request.getAtivo());
        }

        associadoCargoRepository.save(designacao);
        return associadoCargoMapper.toResponse(designacao);
    }

    @Transactional(readOnly = true)
    public AssociadoCargoLiderancaResponseDto buscarDesignacaoPorId(Long idAssociadoCargo) {
        var designacao = buscarDesignacaoEntity(idAssociadoCargo);
        return associadoCargoMapper.toResponse(designacao);
    }

    @Transactional(readOnly = true)
    public AssociadoCargoLideranca buscarDesignacaoEntity(Long idAssociadoCargo) {
        return associadoCargoRepository.findById(idAssociadoCargo)
                .orElseThrow(() -> new NaoEncontradoException("Designação de cargo não encontrada."));
    }

    @Transactional(readOnly = true)
    public List<AssociadoCargoLiderancaResponseDto> buscarCargosPorAssociado(Long idAssociado) {
        return associadoCargoRepository.findByAssociado_IdAssociado(idAssociado)
                .stream()
                .map(associadoCargoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AssociadoCargoLiderancaResponseDto> buscarAssociadosPorCargo(Long idCargoLideranca) {
        return associadoCargoRepository.findByCargoLideranca_IdCargoLideranca(idCargoLideranca)
                .stream()
                .map(associadoCargoMapper::toResponse)
                .collect(Collectors.toList());
    }

}
