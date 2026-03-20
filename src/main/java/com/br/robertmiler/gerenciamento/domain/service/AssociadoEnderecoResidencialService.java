package com.br.robertmiler.gerenciamento.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoEnderecoResidencialResponseDto;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoEnderecoResidencialMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoEnderecoResidencialRepository;

@Service
public class AssociadoEnderecoResidencialService {

    @Autowired
    private AssociadoEnderecoResidencialRepository enderecoResidencialRepository;

    @Autowired
    private AssociadoEnderecoResidencialMapper enderecoResidencialMapper;

    @Autowired
    private AssociadoService associadoService;

    @Transactional
    public AssociadoEnderecoResidencialResponseDto cadastrarEnderecoResidencial(
            AssociadoEnderecoResidencialRequestDto request) {

        var associadoFound = associadoService.buscarAssociadoEntity(request.getIdAssociado());

        var novoEndereco = enderecoResidencialMapper.toEntity(request);
        novoEndereco.setAssociado(associadoFound);

        enderecoResidencialRepository.save(novoEndereco);

        return enderecoResidencialMapper.toResponse(novoEndereco);
    }

    @Transactional(readOnly = true)
    public List<AssociadoEnderecoResidencialResponseDto> buscarEnderecosResidenciaisPorAssociado(Long idAssociado) {

        associadoService.buscarAssociadoEntity(idAssociado);

        var enderecosFound = enderecoResidencialRepository.findByAssociado_IdAssociado(idAssociado);

        return enderecosFound.stream()
                .map(enderecoResidencialMapper::toResponse)
                .toList();
    }

}
