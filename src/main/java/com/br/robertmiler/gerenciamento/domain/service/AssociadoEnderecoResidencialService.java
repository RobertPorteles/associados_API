package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoEnderecoResidencialResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoEnderecoResidencial;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoEnderecoResidencialMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoEnderecoResidencialRepository;

@Service
public class AssociadoEnderecoResidencialService {

    @Autowired
    private AssociadoService associadoService;

    @Autowired
    private AssociadoEnderecoResidencialRepository enderecoResidencialRepository;

    @Autowired
    private AssociadoEnderecoResidencialMapper enderecoResidencialMapper;

    @Transactional
    public AssociadoEnderecoResidencialResponseDto cadastrarEnderecoResidencial(AssociadoEnderecoResidencialRequestDto request) {
        var associadoFound = associadoService.buscarAssociadoEntity(request.getIdAssociado());

        AssociadoEnderecoResidencial novoEndereco = new AssociadoEnderecoResidencial();
        novoEndereco.setRua(FormataString.primeiraLetraMaiuscula(request.getRua()));
        novoEndereco.setNumero(request.getNumero());
        novoEndereco.setComplemento(request.getComplemento());
        novoEndereco.setBairro(request.getBairro());
        novoEndereco.setCidade(FormataString.primeiraLetraMaiuscula(request.getCidade()));
        novoEndereco.setEstado(FormataString.primeiraLetraMaiuscula(request.getEstado()));
        novoEndereco.setCep(request.getCep());
        novoEndereco.setAssociado(associadoFound);

        enderecoResidencialRepository.save(novoEndereco);

        return enderecoResidencialMapper.montarDtoResposta(novoEndereco);
    }

    public AssociadoEnderecoResidencialResponseDto buscarEnderecoResidencialPorAssociado(Long idAssociado) {
        var enderecoFound = enderecoResidencialRepository.findByAssociado_IdAssociado(idAssociado)
                .orElseThrow(() -> new NaoEncontradoException("Endereço residencial não encontrado para o associado informado."));

        return enderecoResidencialMapper.montarDtoResposta(enderecoFound);
    }

}
