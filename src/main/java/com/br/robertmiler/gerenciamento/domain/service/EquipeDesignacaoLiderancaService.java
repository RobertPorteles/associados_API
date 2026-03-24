package com.br.robertmiler.gerenciamento.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeDesignacaoLiderancaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeDesignacaoLiderancaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.EquipeDesignacaoLideranca;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.RegraNegocioException;
import com.br.robertmiler.gerenciamento.domain.mappers.EquipeDesignacaoLiderancaMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EquipeDesignacaoLiderancaRepository;

@Service
public class EquipeDesignacaoLiderancaService {

    @Autowired
    private EquipeDesignacaoLiderancaRepository designacaoRepository;

    @Autowired
    private EquipeDesignacaoLiderancaMapper designacaoMapper;

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private CargoLiderancaService cargoLiderancaService;

    @Autowired
    private AssociadoService associadoService;

    @Transactional
    public EquipeDesignacaoLiderancaResponseDto cadastrarDesignacao(EquipeDesignacaoLiderancaRequestDto request) {
        var equipe = equipeService.buscarEquipeEntity(request.getIdEquipe());
        var cargo = cargoLiderancaService.buscarCargoEntity(request.getIdCargoLideranca());
        var associado = associadoService.buscarAssociadoEntity(request.getIdAssociado());

        // Verifica se já existe designação ativa para o mesmo associado + cargo + equipe
        var existentes = designacaoRepository
                .findByEquipe_IdEquipeAndCargoLideranca_IdCargoLideranca(
                        request.getIdEquipe(), request.getIdCargoLideranca());

        var jaDesignado = existentes.stream()
                .anyMatch(d -> d.getAssociado().getIdAssociado().equals(request.getIdAssociado())
                        && d.getDataFim() == null);

        if (jaDesignado) {
            throw new RegraNegocioException(
                    "Este associado já possui este cargo ativo nesta equipe.");
        }

        var designacao = designacaoMapper.toEntity(request);
        designacao.setEquipe(equipe);
        designacao.setCargoLideranca(cargo);
        designacao.setAssociado(associado);

        designacaoRepository.save(designacao);
        return designacaoMapper.toResponse(designacao);
    }

    @Transactional
    public EquipeDesignacaoLiderancaResponseDto editarDesignacao(Long idDesignacao,
            EquipeDesignacaoLiderancaRequestDto request) {

        var designacao = buscarDesignacaoEntity(idDesignacao);
        var novoCargo = cargoLiderancaService.buscarCargoEntity(request.getIdCargoLideranca());
        var novoAssociado = associadoService.buscarAssociadoEntity(request.getIdAssociado());

        designacao.setCargoLideranca(novoCargo);
        designacao.setAssociado(novoAssociado);
        designacao.setDataInicio(request.getDataInicio());
        designacao.setDataFim(request.getDataFim());

        designacaoRepository.save(designacao);
        return designacaoMapper.toResponse(designacao);
    }

    @Transactional(readOnly = true)
    public EquipeDesignacaoLiderancaResponseDto buscarDesignacaoPorId(Long idDesignacao) {
        var designacao = buscarDesignacaoEntity(idDesignacao);
        return designacaoMapper.toResponse(designacao);
    }

    @Transactional(readOnly = true)
    public List<EquipeDesignacaoLiderancaResponseDto> buscarDesignacoesPorEquipe(Long idEquipe) {
        return designacaoRepository.findByEquipe_IdEquipe(idEquipe)
                .stream()
                .map(designacaoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public EquipeDesignacaoLideranca buscarDesignacaoEntity(Long idDesignacao) {
        return designacaoRepository.findById(idDesignacao)
                .orElseThrow(() -> new NaoEncontradoException("Designação de liderança não encontrada."));
    }

}
