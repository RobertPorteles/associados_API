package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoEnderecoResidencialResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoEnderecoResidencial;

@Component
public class AssociadoEnderecoResidencialMapper {

    public AssociadoEnderecoResidencialResponseDto montarDtoResposta(AssociadoEnderecoResidencial endereco) {
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
