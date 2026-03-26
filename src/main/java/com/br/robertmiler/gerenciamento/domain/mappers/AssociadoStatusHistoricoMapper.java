package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoStatusHistoricoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoStatusHistorico;

@Component
public class AssociadoStatusHistoricoMapper {

    public AssociadoStatusHistoricoResponseDto toResponse(AssociadoStatusHistorico historico) {
        AssociadoStatusHistoricoResponseDto response = new AssociadoStatusHistoricoResponseDto();

        response.setIdStatusHistorico(historico.getIdStatusHistorico());
        response.setIdAssociado(historico.getAssociado().getIdAssociado());
        response.setNomeAssociado(historico.getAssociado().getNomeCompleto());
        response.setStatusAnterior(historico.getStatusAnterior());
        response.setStatusNovo(historico.getStatusNovo());
        response.setMotivo(historico.getMotivo());
        response.setDataInicioPausa(historico.getDataInicioPausa());
        response.setDataPrevisaoRetorno(historico.getDataPrevisaoRetorno());
        response.setIdRegistradoPor(historico.getRegistradoPor().getId());
        response.setNomeRegistradoPor(historico.getRegistradoPor().getUsername());
        response.setRegistradoEm(historico.getRegistradoEm());

        return response;
    }

}
