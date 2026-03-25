package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoStatusHistoricoResponseDto {

    private Long idStatusHistorico;
    private Long idAssociado;
    private String nomeAssociado;
    private StatusAssociado statusAnterior;
    private StatusAssociado statusNovo;
    private String motivo;
    private LocalDate dataInicioPausa;
    private LocalDate dataPrevisaoRetorno;
    private Long idRegistradoPor;
    private String nomeRegistradoPor;
    private LocalDateTime registradoEm;

}
