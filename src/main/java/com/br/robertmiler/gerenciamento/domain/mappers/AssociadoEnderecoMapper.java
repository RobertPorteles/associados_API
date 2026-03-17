package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoEnderecoResidencialResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoEnderecoResidencial;

@Component
public class AssociadoEnderecoMapper {


    public AssociadoEnderecoResidencial toEntity(AssociadoEnderecoResidencialRequestDto request){
        Associado novoAssociado = new Associado();
        AssociadoEnderecoResidencial novoEndereco = new AssociadoEnderecoResidencial();
		novoEndereco.setRua(request.getRua());
		novoEndereco.setNumero(request.getNumero());
		novoEndereco.setComplemento(request.getComplemento());
		novoEndereco.setBairro(request.getBairro());
		novoEndereco.setCidade(request.getCidade());
		novoEndereco.setEstado(request.getEstado());
		novoEndereco.setCep(request.getCep());
		novoEndereco.setAssociado(novoAssociado);

        return novoEndereco;
    }
    public AssociadoEnderecoResidencialResponseDto toResponse(AssociadoEnderecoResidencial response){
        AssociadoEnderecoResidencialResponseDto toResponse = new AssociadoEnderecoResidencialResponseDto();

        toResponse.setBairro(response.getBairro());
        toResponse.setCep(response.getCep());
        toResponse.setCidade(response.getCidade());
        toResponse.setComplemento(response.getComplemento());
        toResponse.setEstado(response.getEstado());
        toResponse.setIdEndereco(response.getIdEndereco());
        toResponse.setNomeAssociado(response.getAssociado().getNomeCompleto());
        toResponse.setNumero(response.getNumero());
        toResponse.setRua(response.getRua());


        return toResponse;

    }
}
