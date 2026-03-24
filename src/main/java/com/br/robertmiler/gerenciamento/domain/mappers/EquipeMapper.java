package com.br.robertmiler.gerenciamento.domain.mappers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Equipe;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;

@Component
public class EquipeMapper {

    public Equipe toEntity(EquipeRequestDto request) {
        Equipe novaEquipe = new Equipe();
        novaEquipe.setNomeEquipe(FormataString.primeiraLetraMaiuscula(request.getNomeEquipe()));
        novaEquipe.setDataInicioFormacao(request.getDataInicioFormacao());
        // dataPrevisaoLancamento é calculada no service (início + 63 dias)
        novaEquipe.setDataEfetivaLancamento(request.getDataEfetivaLancamento());
        novaEquipe.setDiaReuniao(request.getDiaReuniao());
        novaEquipe.setHorarioReuniao(request.getHorarioReuniao());
        novaEquipe.setModeloReuniao(request.getModeloReuniao());
        novaEquipe.setLinkReuniaoOnline(request.getLinkReuniaoOnline());
        novaEquipe.setStatusEquipe(request.getStatusEquipe());
        novaEquipe.setCriadoEm(LocalDateTime.now());
        novaEquipe.setAtualizadoEm(LocalDateTime.now());
        return novaEquipe;
    }

    public EquipeResponseDto toResponse(Equipe equipe) {
        EquipeResponseDto response = new EquipeResponseDto();
        response.setIdEquipe(equipe.getIdEquipe());
        response.setNomeEquipe(equipe.getNomeEquipe());
        response.setDataInicioFormacao(equipe.getDataInicioFormacao());
        response.setDataPrevisaoLancamento(equipe.getDataPrevisaoLancamento());
        response.setDataEfetivaLancamento(equipe.getDataEfetivaLancamento());
        response.setDiaReuniao(equipe.getDiaReuniao());
        response.setHorarioReuniao(equipe.getHorarioReuniao());
        response.setModeloReuniao(equipe.getModeloReuniao());
        response.setLinkReuniaoOnline(equipe.getLinkReuniaoOnline());
        response.setStatusEquipe(equipe.getStatusEquipe());
        response.setCriadoEm(equipe.getCriadoEm());
        response.setAtualizadoEm(equipe.getAtualizadoEm());
        return response;
    }
}
