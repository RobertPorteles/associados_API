package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeDesignacaoLiderancaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeDesignacaoLiderancaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.EquipeDesignacaoLideranca;

@Component
public class EquipeDesignacaoLiderancaMapper {

    public EquipeDesignacaoLideranca toEntity(EquipeDesignacaoLiderancaRequestDto request) {
        EquipeDesignacaoLideranca designacao = new EquipeDesignacaoLideranca();
        designacao.setDataInicio(request.getDataInicio());
        designacao.setDataFim(request.getDataFim());
        return designacao;
    }

    public EquipeDesignacaoLiderancaResponseDto toResponse(EquipeDesignacaoLideranca designacao) {
        EquipeDesignacaoLiderancaResponseDto response = new EquipeDesignacaoLiderancaResponseDto();
        response.setIdDesignacao(designacao.getIdDesignacao());
        response.setIdEquipe(designacao.getEquipe().getIdEquipe());
        response.setNomeEquipe(designacao.getEquipe().getNomeEquipe());
        response.setIdCargoLideranca(designacao.getCargoLideranca().getIdCargoLideranca());
        response.setNomeCargoLideranca(designacao.getCargoLideranca().getNomeCargo());
        response.setIdAssociado(designacao.getAssociado().getIdAssociado());
        response.setNomeAssociado(designacao.getAssociado().getNomeCompleto());
        response.setDataInicio(designacao.getDataInicio());
        response.setDataFim(designacao.getDataFim());
        return response;
    }

}
