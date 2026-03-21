package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.GrupamentoEstrategicoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.GrupamentoEstrategicoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.GrupamentoEstrategico;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.RegraNegocioException;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;
import com.br.robertmiler.gerenciamento.domain.mappers.GrupamentoEstrategicoMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.PaginacaoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.GrupamentoEstrategicoRepository;

@Service
public class GrupamentoEstrategicoService {

    @Autowired
    private GrupamentoEstrategicoRepository grupamentoRepository;

    @Autowired
    private GrupamentoEstrategicoMapper grupamentoMapper;

    @Autowired
    private PaginacaoMapper paginacaoMapper;

    @Transactional
    public GrupamentoEstrategicoResponseDto cadastrarGrupamento(GrupamentoEstrategicoRequestDto request) {

        grupamentoRepository.findByNomeGrupamento(FormataString.primeiraLetraMaiuscula(request.getNomeGrupamento()))
                .ifPresent(g -> {
                    throw new RegraNegocioException("Já existe um grupamento com este nome.");
                });

        var siglaUpper = request.getSigla().toUpperCase().trim();
        grupamentoRepository.findBySigla(siglaUpper)
                .ifPresent(g -> {
                    throw new RegraNegocioException("Já existe um grupamento com esta sigla.");
                });

        var grupamento = grupamentoMapper.toEntity(request);
        grupamentoRepository.save(grupamento);

        return grupamentoMapper.toResponse(grupamento);
    }

    @Transactional
    public GrupamentoEstrategicoResponseDto editarGrupamento(Long idGrupamento, GrupamentoEstrategicoRequestDto request) {

        var grupamento = buscarGrupamentoEntity(idGrupamento);

        var nomeNormalizado = FormataString.primeiraLetraMaiuscula(request.getNomeGrupamento());
        var grupComMesmoNome = grupamentoRepository.findByNomeGrupamento(nomeNormalizado);
        if (grupComMesmoNome.isPresent() && !grupComMesmoNome.get().getIdGrupamento().equals(idGrupamento)) {
            throw new RegraNegocioException("Já existe um grupamento com este nome.");
        }

        var siglaUpper = request.getSigla().toUpperCase().trim();
        var grupComMesmaSigla = grupamentoRepository.findBySigla(siglaUpper);
        if (grupComMesmaSigla.isPresent() && !grupComMesmaSigla.get().getIdGrupamento().equals(idGrupamento)) {
            throw new RegraNegocioException("Já existe um grupamento com esta sigla.");
        }

        grupamento.setNomeGrupamento(nomeNormalizado);
        grupamento.setSigla(siglaUpper);
        grupamento.setAtivo(request.getAtivo());

        grupamentoRepository.save(grupamento);

        return grupamentoMapper.toResponse(grupamento);
    }

    @Transactional(readOnly = true)
    public GrupamentoEstrategicoResponseDto buscarGrupamentoPorId(Long idGrupamento) {
        var grupamento = buscarGrupamentoEntity(idGrupamento);
        return grupamentoMapper.toResponse(grupamento);
    }

    @Transactional(readOnly = true)
    public GrupamentoEstrategico buscarGrupamentoEntity(Long idGrupamento) {
        return grupamentoRepository.findById(idGrupamento)
                .orElseThrow(() -> new NaoEncontradoException("Grupamento estratégico não encontrado."));
    }

    @Transactional(readOnly = true)
    public PaginacaoResponseDto<GrupamentoEstrategicoResponseDto> buscarTodosGrupamentos(Integer number, Integer size) {
        var pageable = PageRequest.of(number, size, Sort.by("nomeGrupamento").ascending());
        var page = grupamentoRepository.findAll(pageable).map(grupamentoMapper::toResponse);
        return paginacaoMapper.montarDtoResposta(page);
    }

}
