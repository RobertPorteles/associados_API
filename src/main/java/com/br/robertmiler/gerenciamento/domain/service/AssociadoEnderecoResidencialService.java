package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoEnderecoResidencialResponseDto;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoEnderecoMapper;

import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoEnderecoResidencialRepository;

@Service
public class AssociadoEnderecoResidencialService {



    @Autowired
    private AssociadoEnderecoResidencialRepository enderecoResidencialRepository;

    @Autowired
    private AssociadoEnderecoMapper enderecoResidencialMapper;

    @Transactional
    public AssociadoEnderecoResidencialResponseDto cadastrarEnderecoResidencial(AssociadoEnderecoResidencialRequestDto request) {
        //var associadoFound = associadoService.buscarAssociadoEntity(request.getIdAssociado());

        AssociadoEnderecoMapper mapper = new AssociadoEnderecoMapper();
        

        

        //aqui eu chutei o balde e fiz em uma linha se quiser separar depois botando em variavel e com tigo

        return mapper.toResponse(enderecoResidencialRepository.save(mapper.toEntity(request)));


    }

    public AssociadoEnderecoResidencialResponseDto buscarEnderecoResidencialPorAssociado(Long idAssociado) {
        var enderecoFound = enderecoResidencialRepository.findByAssociado_IdAssociado(idAssociado)
                .orElseThrow(() -> new NaoEncontradoException("Endereço residencial não encontrado para o associado informado."));

        return enderecoResidencialMapper.toResponse(enderecoFound);
    }

}
