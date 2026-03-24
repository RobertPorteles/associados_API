package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeDiretorEquipeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeDiretorEquipeResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.EquipeDiretorEquipe;

@Component
public class EquipeDiretorEquipeMapper {

    public EquipeDiretorEquipe toEntity(EquipeDiretorEquipeRequestDto request) {
        EquipeDiretorEquipe diretor = new EquipeDiretorEquipe();
        diretor.setDataInicio(request.getDataInicio());
        diretor.setDataFim(request.getDataFim());
        return diretor;
    }

    public EquipeDiretorEquipeResponseDto toResponse(EquipeDiretorEquipe diretor) {
        EquipeDiretorEquipeResponseDto response = new EquipeDiretorEquipeResponseDto();
        response.setIdDiretorEquipe(diretor.getIdDiretorEquipe());
        response.setIdEquipe(diretor.getEquipe().getIdEquipe());
        response.setNomeEquipe(diretor.getEquipe().getNomeEquipe());
        response.setIdAssociado(diretor.getAssociado().getIdAssociado());
        response.setNomeAssociado(diretor.getAssociado().getNomeCompleto());
        response.setDataInicio(diretor.getDataInicio());
        response.setDataFim(diretor.getDataFim());
        return response;
    }

}
