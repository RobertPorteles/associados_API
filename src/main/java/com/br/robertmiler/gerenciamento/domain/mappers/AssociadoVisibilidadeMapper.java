package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoVisibilidadeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoVisibilidadeResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoVisibilidade;
import com.br.robertmiler.gerenciamento.domain.service.AssociadoService;

@Component
public class AssociadoVisibilidadeMapper {

    @Autowired
    private AssociadoService associadoService;

    public AssociadoVisibilidade toEntity(AssociadoVisibilidadeRequestDto request) {
        var associado = associadoService.buscarAssociadoEntity(request.getIdAssociado());

        AssociadoVisibilidade visibilidade = new AssociadoVisibilidade();
        visibilidade.setAssociado(associado);
        visibilidade.setExibirAniversario(request.isExibirAniversario());
        visibilidade.setExibirEnderecoComercial(request.isExibirEnderecoComercial());
        return visibilidade;
    }

    public AssociadoVisibilidadeResponseDto toResponse(AssociadoVisibilidade visibilidade) {
        AssociadoVisibilidadeResponseDto dto = new AssociadoVisibilidadeResponseDto();
        dto.setIdVisibilidade(visibilidade.getIdVisibilidade());
        dto.setNomeAssociado(visibilidade.getAssociado().getNomeCompleto());
        dto.setExibirAniversario(visibilidade.isExibirAniversario());
        dto.setExibirEnderecoComercial(visibilidade.isExibirEnderecoComercial());
        return dto;
    }

}
