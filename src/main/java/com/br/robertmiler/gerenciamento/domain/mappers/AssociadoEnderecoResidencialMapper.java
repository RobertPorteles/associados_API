package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoEnderecoResidencialResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoEnderecoResidencial;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;

@Component
public class AssociadoEnderecoResidencialMapper {

    public AssociadoEnderecoResidencial toEntity(AssociadoEnderecoResidencialRequestDto request) {
        AssociadoEnderecoResidencial novoEndereco = new AssociadoEnderecoResidencial();
        novoEndereco.setRua(FormataString.primeiraLetraMaiuscula(request.getRua()));
        novoEndereco.setNumero(request.getNumero());
        novoEndereco.setComplemento(request.getComplemento());
        novoEndereco.setBairro(FormataString.primeiraLetraMaiuscula(request.getBairro()));
        novoEndereco.setCidade(FormataString.primeiraLetraMaiuscula(request.getCidade()));
        novoEndereco.setEstado(FormataString.primeiraLetraMaiuscula(request.getEstado()));
        novoEndereco.setCep(request.getCep());

        return novoEndereco;
    }

    public AssociadoEnderecoResidencialResponseDto toResponse(AssociadoEnderecoResidencial endereco) {
        AssociadoEnderecoResidencialResponseDto response = new AssociadoEnderecoResidencialResponseDto();
        response.setIdEndereco(endereco.getIdEndereco());
        response.setRua(endereco.getRua());
        response.setNumero(endereco.getNumero());
        response.setComplemento(endereco.getComplemento());
        response.setBairro(endereco.getBairro());
        response.setCidade(endereco.getCidade());
        response.setEstado(endereco.getEstado());
        response.setCep(endereco.getCep());
        response.setNomeAssociado(endereco.getAssociado().getNomeCompleto());

        return response;
    }

}
