package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeDiretorTerritorioRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeDiretorTerritorioResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.EquipeDiretorTerritorio;

@Component
public class EquipeDiretorTerritorioMapper {

    public EquipeDiretorTerritorio toEntity(EquipeDiretorTerritorioRequestDto request) {
        EquipeDiretorTerritorio diretor = new EquipeDiretorTerritorio();
        diretor.setDataInicio(request.getDataInicio());
        diretor.setDataFim(request.getDataFim());
        return diretor;
    }

    public EquipeDiretorTerritorioResponseDto toResponse(EquipeDiretorTerritorio diretor) {
        EquipeDiretorTerritorioResponseDto response = new EquipeDiretorTerritorioResponseDto();
        response.setIdDiretorTerritorio(diretor.getIdDiretorTerritorio());
        response.setIdEquipe(diretor.getEquipe().getIdEquipe());
        response.setNomeEquipe(diretor.getEquipe().getNomeEquipe());
        response.setIdAssociado(diretor.getAssociado().getIdAssociado());
        response.setNomeAssociado(diretor.getAssociado().getNomeCompleto());
        response.setDataInicio(diretor.getDataInicio());
        response.setDataFim(diretor.getDataFim());
        return response;
    }

}
