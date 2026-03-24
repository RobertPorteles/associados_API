package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeLocalPresencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeLocalPresencialResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.EquipeLocalPresencial;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;

@Component
public class EquipeLocalPresencialMapper {

    public EquipeLocalPresencial toEntity(EquipeLocalPresencialRequestDto request) {
        EquipeLocalPresencial local = new EquipeLocalPresencial();
        local.setRua(FormataString.primeiraLetraMaiuscula(request.getRua()));
        local.setNumero(request.getNumero());
        local.setComplemento(request.getComplemento());
        local.setBairro(FormataString.primeiraLetraMaiuscula(request.getBairro()));
        local.setCidade(FormataString.primeiraLetraMaiuscula(request.getCidade()));
        local.setUf(request.getUf().toUpperCase());
        local.setCep(request.getCep());
        return local;
    }

    public EquipeLocalPresencialResponseDto toResponse(EquipeLocalPresencial local) {
        EquipeLocalPresencialResponseDto response = new EquipeLocalPresencialResponseDto();
        response.setIdLocalPresencial(local.getIdLocalPresencial());
        response.setIdEquipe(local.getEquipe().getIdEquipe());
        response.setNomeEquipe(local.getEquipe().getNomeEquipe());
        response.setRua(local.getRua());
        response.setNumero(local.getNumero());
        response.setComplemento(local.getComplemento());
        response.setBairro(local.getBairro());
        response.setCidade(local.getCidade());
        response.setUf(local.getUf());
        response.setCep(local.getCep());
        return response;
    }

}
