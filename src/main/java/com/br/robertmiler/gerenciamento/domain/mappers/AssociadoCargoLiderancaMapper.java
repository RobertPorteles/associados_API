package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoCargoLiderancaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoCargoLiderancaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoCargoLideranca;
import com.br.robertmiler.gerenciamento.domain.service.AssociadoService;
import com.br.robertmiler.gerenciamento.domain.service.CargoLiderancaService;

@Component
public class AssociadoCargoLiderancaMapper {

    @Autowired
    private AssociadoService associadoService;

    @Autowired
    private CargoLiderancaService cargoLiderancaService;

    public AssociadoCargoLideranca toEntity(AssociadoCargoLiderancaRequestDto request) {
        var associado = associadoService.buscarAssociadoEntity(request.getIdAssociado());
        var cargo = cargoLiderancaService.buscarCargoEntity(request.getIdCargoLideranca());

        AssociadoCargoLideranca designacao = new AssociadoCargoLideranca();
        designacao.setAssociado(associado);
        designacao.setCargoLideranca(cargo);
        designacao.setDataInicio(request.getDataInicio());
        designacao.setDataFim(request.getDataFim());
        designacao.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
        return designacao;
    }

    public AssociadoCargoLiderancaResponseDto toResponse(AssociadoCargoLideranca designacao) {
        AssociadoCargoLiderancaResponseDto dto = new AssociadoCargoLiderancaResponseDto();
        dto.setIdAssociadoCargo(designacao.getIdAssociadoCargo());
        dto.setNomeAssociado(designacao.getAssociado().getNomeCompleto());
        dto.setNomeCargo(designacao.getCargoLideranca().getNomeCargo());
        dto.setClassificacaoFinanceira(designacao.getCargoLideranca().getClassificacaoFinanceira());
        dto.setDataInicio(designacao.getDataInicio());
        dto.setDataFim(designacao.getDataFim());
        dto.setAtivo(designacao.getAtivo());
        return dto;
    }

}
