package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EmpresaEnderecoComercialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EmpresaEnderecoComercialResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.EmpresaEnderecoComercial;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;
import com.br.robertmiler.gerenciamento.domain.mappers.EmpresaEnderecoComercialMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EmpresaEnderecoComercialRepository;

@Service
public class EmpresaEnderecoComercialService {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private EmpresaEnderecoComercialRepository enderecoComercialRepository;

    @Autowired
    private EmpresaEnderecoComercialMapper enderecoComercialMapper;

    @Transactional
    public EmpresaEnderecoComercialResponseDto cadastrarEnderecoComercial(EmpresaEnderecoComercialRequestDto request) {
        var empresaFound = empresaService.buscarEmpresaEntity(request.getIdEmpresa());

        EmpresaEnderecoComercial novoEndereco = new EmpresaEnderecoComercial();
        novoEndereco.setRua(FormataString.primeiraLetraMaiuscula(request.getRua()));
        novoEndereco.setNumero(request.getNumero());
        novoEndereco.setComplemento(request.getComplemento());
        novoEndereco.setBairro(request.getBairro());
        novoEndereco.setCidade(FormataString.primeiraLetraMaiuscula(request.getCidade()));
        novoEndereco.setEstado(FormataString.primeiraLetraMaiuscula(request.getEstado()));
        novoEndereco.setCep(request.getCep());
        novoEndereco.setEmpresa(empresaFound);

        enderecoComercialRepository.save(novoEndereco);

        return enderecoComercialMapper.montarDtoResposta(novoEndereco);
    }

    @Transactional
    public EmpresaEnderecoComercialResponseDto editarEnderecoComercial(Long idEnderecoComercial,
            EmpresaEnderecoComercialRequestDto request) {

        var enderecoFound = enderecoComercialRepository.findById(idEnderecoComercial)
                .orElseThrow(() -> new NaoEncontradoException("Endereço comercial não encontrado."));

        enderecoFound.setRua(FormataString.primeiraLetraMaiuscula(request.getRua()));
        enderecoFound.setNumero(request.getNumero());
        enderecoFound.setComplemento(request.getComplemento());
        enderecoFound.setBairro(FormataString.primeiraLetraMaiuscula(request.getBairro()));
        enderecoFound.setCidade(FormataString.primeiraLetraMaiuscula(request.getCidade()));
        enderecoFound.setEstado(FormataString.primeiraLetraMaiuscula(request.getEstado()));
        enderecoFound.setCep(request.getCep());

        enderecoComercialRepository.save(enderecoFound);

        return enderecoComercialMapper.montarDtoResposta(enderecoFound);
    }

    @Transactional(readOnly = true)
    public EmpresaEnderecoComercialResponseDto buscarEnderecoComercialPorEmpresa(Long idEmpresa) {
        var enderecoFound = enderecoComercialRepository.findByEmpresa_IdEmpresa(idEmpresa)
                .orElseThrow(() -> new NaoEncontradoException("Endereço comercial não encontrado para a empresa informada."));

        return enderecoComercialMapper.montarDtoResposta(enderecoFound);
    }

}
