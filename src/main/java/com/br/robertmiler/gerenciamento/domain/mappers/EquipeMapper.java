package com.br.robertmiler.gerenciamento.domain.mappers;

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
        
        return novaEquipe;
    }

    public EquipeResponseDto toResponse(Equipe equipe) {
        EquipeResponseDto response = new EquipeResponseDto();
        response.setIdEquipe(equipe.getIdEquipe());
        response.setNomeEquipe(equipe.getNomeEquipe());

        return response;
    }
}
