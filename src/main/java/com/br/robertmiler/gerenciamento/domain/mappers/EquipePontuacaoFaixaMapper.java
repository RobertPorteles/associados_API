package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipePontuacaoFaixaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipePontuacaoFaixaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.EquipePontuacaoFaixa;

@Component
public class EquipePontuacaoFaixaMapper {

    public EquipePontuacaoFaixa toEntity(EquipePontuacaoFaixaRequestDto request) {
        EquipePontuacaoFaixa faixa = new EquipePontuacaoFaixa();
        faixa.setMinimoAssociados(request.getMinimoAssociados());
        faixa.setMaximoAssociados(request.getMaximoAssociados());
        faixa.setPontos(request.getPontos());
        faixa.setAtivo(request.getAtivo());
        return faixa;
    }

    public EquipePontuacaoFaixaResponseDto toResponse(EquipePontuacaoFaixa faixa) {
        EquipePontuacaoFaixaResponseDto response = new EquipePontuacaoFaixaResponseDto();
        response.setIdFaixa(faixa.getIdFaixa());
        response.setMinimoAssociados(faixa.getMinimoAssociados());
        response.setMaximoAssociados(faixa.getMaximoAssociados());
        response.setPontos(faixa.getPontos());
        response.setAtivo(faixa.getAtivo());
        response.setCriadoEm(faixa.getCriadoEm());
        response.setAtualizadoEm(faixa.getAtualizadoEm());
        return response;
    }

}
