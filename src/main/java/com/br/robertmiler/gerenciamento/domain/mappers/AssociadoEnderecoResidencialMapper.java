package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;


import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoEnderecoResidencialResponseDto;

import com.br.robertmiler.gerenciamento.domain.entities.AssociadoEnderecoResidencial;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;

@Component
public class AssociadoEnderecoResidencialMapper {
     
    
    public AssociadoEnderecoResidencial toEntity(AssociadoRequestDto request) {
         AssociadoEnderecoResidencial endereco = new AssociadoEnderecoResidencial();

          if (request.getRua() != null)
         endereco.setRua(FormataString.primeiraLetraMaiuscula(request.getRua()));
         if (request.getNumero() != null)
        endereco.setNumero(request.getNumero());
          if (request.getComplemento() != null)
        endereco.setComplemento(request.getComplemento());
          if (request.getBairro() != null)
        endereco.setBairro(FormataString.primeiraLetraMaiuscula(request.getBairro()));
          if (request.getCidade() != null)
        endereco.setCidade(FormataString.primeiraLetraMaiuscula(request.getCidade()));
         if (request.getEstado() != null)
        endereco.setEstado(request.getEstado().toUpperCase());
         if (request.getCep() != null)
        endereco.setCep(request.getCep());

         


    return endereco;

        
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
