package com.br.robertmiler.gerenciamento.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeDiretorTerritorioRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeDiretorTerritorioResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.EquipeDiretorTerritorio;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.EquipeDiretorTerritorioMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EquipeDiretorTerritorioRepository;

@Service
public class EquipeDiretorTerritorioService {

    @Autowired
    private EquipeDiretorTerritorioRepository diretorTerritorioRepository;

    @Autowired
    private EquipeDiretorTerritorioMapper diretorTerritorioMapper;

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private AssociadoService associadoService;

    @Transactional
    public EquipeDiretorTerritorioResponseDto cadastrarDiretorTerritorio(EquipeDiretorTerritorioRequestDto request) {
        var equipe = equipeService.buscarEquipeEntity(request.getIdEquipe());
        var associado = associadoService.buscarAssociadoEntity(request.getIdAssociado());

        var diretor = diretorTerritorioMapper.toEntity(request);
        diretor.setEquipe(equipe);
        diretor.setAssociado(associado);

        diretorTerritorioRepository.save(diretor);
        return diretorTerritorioMapper.toResponse(diretor);
    }

    @Transactional
    public EquipeDiretorTerritorioResponseDto editarDiretorTerritorio(Long idDiretorTerritorio,
            EquipeDiretorTerritorioRequestDto request) {

        var diretor = buscarDiretorTerritorioEntity(idDiretorTerritorio);
        var novoAssociado = associadoService.buscarAssociadoEntity(request.getIdAssociado());

        diretor.setAssociado(novoAssociado);
        diretor.setDataInicio(request.getDataInicio());
        diretor.setDataFim(request.getDataFim());

        diretorTerritorioRepository.save(diretor);
        return diretorTerritorioMapper.toResponse(diretor);
    }

    @Transactional(readOnly = true)
    public EquipeDiretorTerritorioResponseDto buscarDiretorTerritorioPorId(Long idDiretorTerritorio) {
        var diretor = buscarDiretorTerritorioEntity(idDiretorTerritorio);
        return diretorTerritorioMapper.toResponse(diretor);
    }

    @Transactional(readOnly = true)
    public List<EquipeDiretorTerritorioResponseDto> buscarDiretoresTerritorioPorEquipe(Long idEquipe) {
        return diretorTerritorioRepository.findByEquipe_IdEquipe(idEquipe)
                .stream()
                .map(diretorTerritorioMapper::toResponse)
                .collect(Collectors.toList());
    }

    public EquipeDiretorTerritorio buscarDiretorTerritorioEntity(Long idDiretorTerritorio) {
        return diretorTerritorioRepository.findById(idDiretorTerritorio)
                .orElseThrow(() -> new NaoEncontradoException("Diretor de território não encontrado."));
    }

}
