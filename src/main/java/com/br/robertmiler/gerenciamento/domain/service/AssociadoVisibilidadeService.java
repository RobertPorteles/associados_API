package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoVisibilidadeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoVisibilidadeResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoVisibilidade;
import com.br.robertmiler.gerenciamento.domain.exceptions.JaCadastradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoVisibilidadeMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoVisibilidadeRepository;

@Service
public class AssociadoVisibilidadeService {

    @Autowired
    private AssociadoVisibilidadeRepository associadoVisibilidadeRepository;

    @Autowired
    private AssociadoVisibilidadeMapper associadoVisibilidadeMapper;

    @Autowired
    private AssociadoService associadoService;

    @Transactional
    public AssociadoVisibilidadeResponseDto cadastrarVisibilidade(AssociadoVisibilidadeRequestDto request) {
        if (associadoVisibilidadeRepository.existsByAssociado_IdAssociado(request.getIdAssociado())) {
            throw new JaCadastradoException("Preferências de visibilidade já cadastradas para este associado.");
        }

        var visibilidade = associadoVisibilidadeMapper.toEntity(request);
        associadoVisibilidadeRepository.save(visibilidade);
        return associadoVisibilidadeMapper.toResponse(visibilidade);
    }

    @Transactional
    public AssociadoVisibilidadeResponseDto editarVisibilidade(Long idVisibilidade, AssociadoVisibilidadeRequestDto request) {
        var visibilidade = buscarVisibilidadeEntity(idVisibilidade);

        visibilidade.setExibirAniversario(request.isExibirAniversario());
        visibilidade.setExibirEnderecoComercial(request.isExibirEnderecoComercial());

        associadoVisibilidadeRepository.save(visibilidade);
        return associadoVisibilidadeMapper.toResponse(visibilidade);
    }

    @Transactional(readOnly = true)
    public AssociadoVisibilidadeResponseDto buscarVisibilidadePorId(Long idVisibilidade) {
        var visibilidade = buscarVisibilidadeEntity(idVisibilidade);
        return associadoVisibilidadeMapper.toResponse(visibilidade);
    }

    @Transactional(readOnly = true)
    public AssociadoVisibilidadeResponseDto buscarVisibilidadePorAssociado(Long idAssociado) {
        associadoService.buscarAssociadoEntity(idAssociado);

        var visibilidade = associadoVisibilidadeRepository.findByAssociado_IdAssociado(idAssociado)
                .orElseThrow(() -> new NaoEncontradoException("Preferências de visibilidade não encontradas para este associado."));

        return associadoVisibilidadeMapper.toResponse(visibilidade);
    }

    @Transactional(readOnly = true)
    public AssociadoVisibilidade buscarVisibilidadeEntity(Long idVisibilidade) {
        return associadoVisibilidadeRepository.findById(idVisibilidade)
                .orElseThrow(() -> new NaoEncontradoException("Preferências de visibilidade não encontradas."));
    }

}
