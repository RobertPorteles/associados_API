package com.br.robertmiler.gerenciamento.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoEnderecoResidencialResponseDto;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;
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
            Long idAssociado, 
            AssociadoRequestDto request) { 

       

        // 2. Usamos a variável idAssociado direto aqui! Fim do erro do request.getId()
        var associadoFound = associadoService.buscarAssociadoEntity(idAssociado);

        // 3. Converte o DTO para Entidade de Endereço
        var novoEndereco = enderecoResidencialMapper.toEntity(request);
        
        // 4. Faz o Link da entidade inteira
        novoEndereco.setAssociado(associadoFound);
                
        // 5. Salva e retorna
        enderecoResidencialRepository.save(novoEndereco);

        return enderecoResidencialMapper.toResponse(novoEndereco);
    }

    @Transactional
    public AssociadoEnderecoResidencialResponseDto editarEnderecoResidencial(Long idEndereco,
            AssociadoEnderecoResidencialRequestDto request) {

        var enderecoFound = enderecoResidencialRepository.findById(idEndereco)
                .orElseThrow(() -> new NaoEncontradoException("Endereço residencial não encontrado."));

        enderecoFound.setRua(FormataString.primeiraLetraMaiuscula(request.getRua()));
        enderecoFound.setNumero(request.getNumero());
        enderecoFound.setComplemento(request.getComplemento());
        enderecoFound.setBairro(FormataString.primeiraLetraMaiuscula(request.getBairro()));
        enderecoFound.setCidade(FormataString.primeiraLetraMaiuscula(request.getCidade()));
        enderecoFound.setEstado(request.getEstado().toUpperCase());
        enderecoFound.setCep(request.getCep());

        enderecoResidencialRepository.save(enderecoFound);

        return enderecoResidencialMapper.toResponse(enderecoFound);
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
