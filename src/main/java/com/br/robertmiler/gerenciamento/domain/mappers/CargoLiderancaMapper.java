package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.CargoLiderancaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.CargoLiderancaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.CargoLideranca;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;

@Component
public class CargoLiderancaMapper {

    public CargoLideranca toEntity(CargoLiderancaRequestDto request) {
        CargoLideranca cargo = new CargoLideranca();
        cargo.setNomeCargo(FormataString.primeiraLetraMaiuscula(request.getNomeCargo()));
        cargo.setClassificacaoFinanceira(request.getClassificacaoFinanceira());
        cargo.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
        return cargo;
    }

    public CargoLiderancaResponseDto toResponse(CargoLideranca cargo) {
        CargoLiderancaResponseDto dto = new CargoLiderancaResponseDto();
        dto.setIdCargoLideranca(cargo.getIdCargoLideranca());
        dto.setNomeCargo(cargo.getNomeCargo());
        dto.setClassificacaoFinanceira(cargo.getClassificacaoFinanceira());
        dto.setAtivo(cargo.getAtivo());
        return dto;
    }

}
