package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.PerfilAssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PerfilAssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.PerfilAssociado;
import com.br.robertmiler.gerenciamento.domain.exceptions.JaCadastradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.PerfilAssociadoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoCargoLiderancaRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.PerfilAssociadoRepository;

@Service
public class PerfilAssociadoService {

    @Autowired
    private PerfilAssociadoRepository perfilRepository;

    @Autowired
    private PerfilAssociadoMapper perfilMapper;

    @Autowired
    private AssociadoService associadoService;

    @Autowired
    private AssociadoCargoLiderancaRepository cargoLiderancaRepository;

    @Transactional
    public PerfilAssociadoResponseDto cadastrarPerfil(PerfilAssociadoRequestDto request) {
        if (perfilRepository.existsByAssociado_IdAssociado(request.getIdAssociado())) {
            throw new JaCadastradoException("Este associado já possui um perfil cadastrado.");
        }

        var associado = associadoService.buscarAssociadoEntity(request.getIdAssociado());

        var perfil = perfilMapper.toEntity(request);
        perfil.setAssociado(associado);
        atualizarFlagPerfilCompleto(perfil);

        perfilRepository.save(perfil);
        return montarResponse(perfil);
    }

    @Transactional
    public PerfilAssociadoResponseDto editarPerfil(Long idPerfil, PerfilAssociadoRequestDto request) {
        var perfil = buscarPerfilEntity(idPerfil);

        perfilMapper.atualizarEntidade(perfil, request);
        atualizarFlagPerfilCompleto(perfil);

        perfilRepository.save(perfil);
        return montarResponse(perfil);
    }

    @Transactional(readOnly = true)
    public PerfilAssociadoResponseDto buscarPerfilPorId(Long idPerfil) {
        var perfil = buscarPerfilEntity(idPerfil);
        return montarResponse(perfil);
    }

    @Transactional(readOnly = true)
    public PerfilAssociadoResponseDto buscarPerfilPorAssociado(Long idAssociado) {
        var perfil = perfilRepository.findByAssociado_IdAssociado(idAssociado)
                .orElseThrow(() -> new NaoEncontradoException("Perfil não encontrado para este associado."));
        return montarResponse(perfil);
    }

    public PerfilAssociado buscarPerfilEntity(Long idPerfil) {
        return perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new NaoEncontradoException("Perfil não encontrado."));
    }

    // ── Privados ──────────────────────────────────────────────────────────────

    /**
     * Monta o DTO de resposta resolvendo o cargo ativo atual do associado.
     */
    private PerfilAssociadoResponseDto montarResponse(PerfilAssociado perfil) {
        var cargoOpt = cargoLiderancaRepository
                .findFirstByAssociado_IdAssociadoAndAtivoTrueOrderByDataInicioDesc(
                        perfil.getAssociado().getIdAssociado());
        var nomeCargoAtual = cargoOpt
                .map(c -> c.getCargoLideranca().getNomeCargo())
                .orElse(null);
        return perfilMapper.toResponse(perfil, nomeCargoAtual);
    }

    /**
     * Item 5 do Sprint 3 — Flag perfil_completo.
     * true somente quando todos os campos obrigatórios estão preenchidos.
     * Chamado após cada save para manter o estado sempre atualizado.
     */
    private void atualizarFlagPerfilCompleto(PerfilAssociado perfil) {
        boolean completo =
                isPreenchido(perfil.getFotoProfissional())
                && isPreenchido(perfil.getNomeProfissional())
                && isPreenchido(perfil.getNomeEmpresa())
                && isPreenchido(perfil.getTelefonePrincipal())
                && isPreenchido(perfil.getEmail())
                && isPreenchido(perfil.getOQueFaco())
                && isPreenchido(perfil.getPublicoIdeal())
                && isPreenchido(perfil.getPrincipalProblemaResolvo())
                && isPreenchido(perfil.getConexoesEstrategicas())
                && isPreenchido(perfil.getInteressesPessoais());

        perfil.setPerfilCompleto(completo);
    }

    private boolean isPreenchido(String valor) {
        return valor != null && !valor.isBlank();
    }

}
