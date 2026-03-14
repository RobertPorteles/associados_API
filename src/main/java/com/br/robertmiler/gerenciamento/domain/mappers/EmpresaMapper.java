package com.br.robertmiler.gerenciamento.domain.mappers;


import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EmpresaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EmpresaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.Empresa;


@Component
public class EmpresaMapper {

    public Empresa toEntity(EmpresaRequestDto requestDto, Associado associado) {
        Empresa novaEmpresa = new Empresa();

        novaEmpresa.setCargo(requestDto.getCargo());
        novaEmpresa.setCnpj(requestDto.getCnpj());
        novaEmpresa.setRazaoSocial(requestDto.getRazaoSocial());
        novaEmpresa.setNomeFantasia(requestDto.getNomeFantasia());
        novaEmpresa.setAssociado(associado);

        return novaEmpresa;
    }

    public EmpresaResponseDto toResponse(Empresa empresa) {
        EmpresaResponseDto responseDto = new EmpresaResponseDto();

        responseDto.setIdEmpresa(empresa.getIdEmpresa());
        responseDto.setCargo(empresa.getCargo());
        responseDto.setCnpj(empresa.getCnpj());
        responseDto.setRazaoSocial(empresa.getRazaoSocial());
        responseDto.setNomeFantasia(empresa.getNomeFantasia());
        responseDto.setNomeAssociado(empresa.getAssociado().getNomeCompleto());

        return responseDto;
    }
}