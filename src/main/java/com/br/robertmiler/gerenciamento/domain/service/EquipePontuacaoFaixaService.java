package com.br.robertmiler.gerenciamento.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipePontuacaoFaixaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipePontuacaoFaixaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.EquipePontuacaoFaixa;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.EquipePontuacaoFaixaMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EquipePontuacaoFaixaRepository;

@Service
public class EquipePontuacaoFaixaService {

    @Autowired
    private EquipePontuacaoFaixaRepository pontuacaoFaixaRepository;

    @Autowired
    private EquipePontuacaoFaixaMapper pontuacaoFaixaMapper;

    @Transactional
    public EquipePontuacaoFaixaResponseDto cadastrarFaixa(EquipePontuacaoFaixaRequestDto request) {
        var faixa = pontuacaoFaixaMapper.toEntity(request);
        pontuacaoFaixaRepository.save(faixa);
        return pontuacaoFaixaMapper.toResponse(faixa);
    }

    @Transactional
    public EquipePontuacaoFaixaResponseDto editarFaixa(Long idFaixa, EquipePontuacaoFaixaRequestDto request) {
        var faixa = buscarFaixaEntity(idFaixa);

        faixa.setMinimoAssociados(request.getMinimoAssociados());
        faixa.setMaximoAssociados(request.getMaximoAssociados());
        faixa.setPontos(request.getPontos());
        faixa.setAtivo(request.getAtivo());

        pontuacaoFaixaRepository.save(faixa);
        return pontuacaoFaixaMapper.toResponse(faixa);
    }

    @Transactional(readOnly = true)
    public EquipePontuacaoFaixaResponseDto buscarFaixaPorId(Long idFaixa) {
        var faixa = buscarFaixaEntity(idFaixa);
        return pontuacaoFaixaMapper.toResponse(faixa);
    }

    @Transactional(readOnly = true)
    public List<EquipePontuacaoFaixaResponseDto> buscarTodasFaixas() {
        return pontuacaoFaixaRepository.findAllByOrderByMinimoAssociadosAsc()
                .stream()
                .map(pontuacaoFaixaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EquipePontuacaoFaixaResponseDto> buscarFaixasAtivas() {
        return pontuacaoFaixaRepository.findByAtivoTrue()
                .stream()
                .map(pontuacaoFaixaMapper::toResponse)
                .collect(Collectors.toList());
    }

    public EquipePontuacaoFaixa buscarFaixaEntity(Long idFaixa) {
        return pontuacaoFaixaRepository.findById(idFaixa)
                .orElseThrow(() -> new NaoEncontradoException("Faixa de pontuação não encontrada."));
    }

}
