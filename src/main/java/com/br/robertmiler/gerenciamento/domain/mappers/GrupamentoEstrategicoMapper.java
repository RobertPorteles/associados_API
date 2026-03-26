package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.GrupamentoEstrategicoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.GrupamentoEstrategicoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.GrupamentoEstrategico;


@Component
public class GrupamentoEstrategicoMapper {

    public GrupamentoEstrategico toEntity(GrupamentoEstrategicoRequestDto request) {
        

        GrupamentoEstrategico grupamento = new GrupamentoEstrategico(request.getNomeGrupamento(), request.getSigla());
        grupamento.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);

        return grupamento;
    }

    public GrupamentoEstrategicoResponseDto toResponse(GrupamentoEstrategico grupamento) {
        GrupamentoEstrategicoResponseDto dto = new GrupamentoEstrategicoResponseDto();
        dto.setIdGrupamento(grupamento.getIdGrupamento());
        dto.setNomeGrupamento(grupamento.getNome());
        dto.setSigla(grupamento.getSigla());
        dto.setAtivo(grupamento.isAtivo());
        return dto;
    }

}
