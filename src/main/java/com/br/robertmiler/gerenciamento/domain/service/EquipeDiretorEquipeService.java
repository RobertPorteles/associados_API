package com.br.robertmiler.gerenciamento.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeDiretorEquipeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeDiretorEquipeResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.EquipeDiretorEquipe;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.EquipeDiretorEquipeMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EquipeDiretorEquipeRepository;

@Service
public class EquipeDiretorEquipeService {

    @Autowired
    private EquipeDiretorEquipeRepository diretorEquipeRepository;

    @Autowired
    private EquipeDiretorEquipeMapper diretorEquipeMapper;

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private AssociadoService associadoService;

    @Transactional
    public EquipeDiretorEquipeResponseDto cadastrarDiretorEquipe(EquipeDiretorEquipeRequestDto request) {
        var equipe = equipeService.buscarEquipeEntity(request.getIdEquipe());
        var associado = associadoService.buscarAssociadoEntity(request.getIdAssociado());

        var diretor = diretorEquipeMapper.toEntity(request);
        diretor.setEquipe(equipe);
        diretor.setAssociado(associado);

        diretorEquipeRepository.save(diretor);
        return diretorEquipeMapper.toResponse(diretor);
    }

    @Transactional
    public EquipeDiretorEquipeResponseDto editarDiretorEquipe(Long idDiretorEquipe,
            EquipeDiretorEquipeRequestDto request) {

        var diretor = buscarDiretorEquipeEntity(idDiretorEquipe);
        var novoAssociado = associadoService.buscarAssociadoEntity(request.getIdAssociado());

        diretor.setAssociado(novoAssociado);
        diretor.setDataInicio(request.getDataInicio());
        diretor.setDataFim(request.getDataFim());

        diretorEquipeRepository.save(diretor);
        return diretorEquipeMapper.toResponse(diretor);
    }

    @Transactional(readOnly = true)
    public EquipeDiretorEquipeResponseDto buscarDiretorEquipePorId(Long idDiretorEquipe) {
        var diretor = buscarDiretorEquipeEntity(idDiretorEquipe);
        return diretorEquipeMapper.toResponse(diretor);
    }

    @Transactional(readOnly = true)
    public List<EquipeDiretorEquipeResponseDto> buscarDiretoresEquipePorEquipe(Long idEquipe) {
        return diretorEquipeRepository.findByEquipe_IdEquipe(idEquipe)
                .stream()
                .map(diretorEquipeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public EquipeDiretorEquipe buscarDiretorEquipeEntity(Long idDiretorEquipe) {
        return diretorEquipeRepository.findById(idDiretorEquipe)
                .orElseThrow(() -> new NaoEncontradoException("Diretor de equipe não encontrado."));
    }

}
