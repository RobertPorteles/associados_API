package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeLocalPresencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeLocalPresencialResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.EquipeLocalPresencial;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.EquipeLocalPresencialMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EquipeLocalPresencialRepository;

@Service
public class EquipeLocalPresencialService {

    @Autowired
    private EquipeLocalPresencialRepository localPresencialRepository;

    @Autowired
    private EquipeLocalPresencialMapper localPresencialMapper;

    @Autowired
    private EquipeService equipeService;

    @Transactional
    public EquipeLocalPresencialResponseDto cadastrarLocalPresencial(EquipeLocalPresencialRequestDto request) {
        var equipe = equipeService.buscarEquipeEntity(request.getIdEquipe());

        var local = localPresencialMapper.toEntity(request);
        local.setEquipe(equipe);

        localPresencialRepository.save(local);
        return localPresencialMapper.toResponse(local);
    }

    @Transactional
    public EquipeLocalPresencialResponseDto editarLocalPresencial(Long idLocalPresencial,
            EquipeLocalPresencialRequestDto request) {

        var local = buscarLocalPresencialEntity(idLocalPresencial);

        local.setRua(request.getRua());
        local.setNumero(request.getNumero());
        local.setComplemento(request.getComplemento());
        local.setBairro(request.getBairro());
        local.setCidade(request.getCidade());
        local.setUf(request.getUf().toUpperCase());
        local.setCep(request.getCep());

        localPresencialRepository.save(local);
        return localPresencialMapper.toResponse(local);
    }

    @Transactional(readOnly = true)
    public EquipeLocalPresencialResponseDto buscarLocalPresencialPorId(Long idLocalPresencial) {
        var local = buscarLocalPresencialEntity(idLocalPresencial);
        return localPresencialMapper.toResponse(local);
    }

    @Transactional(readOnly = true)
    public EquipeLocalPresencialResponseDto buscarLocalPresencialPorEquipe(Long idEquipe) {
        var local = localPresencialRepository.findByEquipe_IdEquipe(idEquipe)
                .orElseThrow(() -> new NaoEncontradoException("Local presencial não encontrado para esta equipe."));
        return localPresencialMapper.toResponse(local);
    }

    public EquipeLocalPresencial buscarLocalPresencialEntity(Long idLocalPresencial) {
        return localPresencialRepository.findById(idLocalPresencial)
                .orElseThrow(() -> new NaoEncontradoException("Local presencial não encontrado."));
    }

}
