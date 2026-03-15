package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.response.EmpresaEnderecoComercialResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.EmpresaEnderecoComercial;

@Component
public class EmpresaEnderecoComercialMapper {

    public EmpresaEnderecoComercialResponseDto montarDtoResposta(EmpresaEnderecoComercial endereco) {
        EmpresaEnderecoComercialResponseDto response = new EmpresaEnderecoComercialResponseDto();
        response.setIdEnderecoComercial(endereco.getIdEnderecoComercial());
        response.setRua(endereco.getRua());
        response.setNumero(endereco.getNumero());
        response.setComplemento(endereco.getComplemento());
        response.setBairro(endereco.getBairro());
        response.setCidade(endereco.getCidade());
        response.setEstado(endereco.getEstado());
        response.setCep(endereco.getCep());
        response.setNomeFantasiaEmpresa(endereco.getEmpresa().getNomeFantasia());
        return response;
    }

}
