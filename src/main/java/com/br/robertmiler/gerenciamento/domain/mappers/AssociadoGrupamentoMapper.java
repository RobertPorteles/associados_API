package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoGrupamentoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoGrupamentoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoGrupamento;
import com.br.robertmiler.gerenciamento.domain.service.AssociadoService;
import com.br.robertmiler.gerenciamento.domain.service.GrupamentoEstrategicoService;

@Component
public class AssociadoGrupamentoMapper {

    @Autowired
    private AssociadoService associadoService;

    @Autowired
    private GrupamentoEstrategicoService grupamentoService;

    public AssociadoGrupamento toEntity(AssociadoGrupamentoRequestDto request) {
        var associado = associadoService.buscarAssociadoEntity(request.getIdAssociado());
        var grupamento = grupamentoService.buscarGrupamentoEntity(request.getIdGrupamento());

        AssociadoGrupamento vinculo = new AssociadoGrupamento();
        vinculo.setAssociado(associado);
        vinculo.setGrupamento(grupamento);
        vinculo.setDataInicio(request.getDataInicio());
        vinculo.setDataFim(request.getDataFim());
        vinculo.setAtivo(request.getAtivo());
        return vinculo;
    }

    public AssociadoGrupamentoResponseDto toResponse(AssociadoGrupamento vinculo) {
        AssociadoGrupamentoResponseDto dto = new AssociadoGrupamentoResponseDto();
        dto.setIdAssociadoGrupamento(vinculo.getIdAssociadoGrupamento());
        dto.setNomeAssociado(vinculo.getAssociado().getNomeCompleto());
        dto.setNomeGrupamento(vinculo.getGrupamento().getNome());
        dto.setSigla(vinculo.getGrupamento().getSigla());
        dto.setDataInicio(vinculo.getDataInicio());
        dto.setDataFim(vinculo.getDataFim());
        dto.setAtivo(vinculo.getAtivo());
        return dto;
    }

}
