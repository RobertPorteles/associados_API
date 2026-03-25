package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilAssociadoResponseDto {

    private Long idPerfil;
    private Long idAssociado;

    // ── Campos editáveis pelo associado ───────────────────────────────────────

    private String fotoProfissional;
    private String nomeProfissional;
    private String nomeEmpresa;
    private String logomarcaEmpresa;
    private String telefonePrincipal;
    private String telefoneSecundario;
    private String email;
    private String site;
    private String linkedIn;
    private String instagram;
    private String youTube;
    private String outraRedeSocial;
    private String oQueFaco;
    private String publicoIdeal;
    private String principalProblemaResolvo;
    private String conexoesEstrategicas;
    private String interessesPessoais;

    // ── Campos automáticos (Fase 1) ───────────────────────────────────────────

    private String nomeCluster;
    private String nomeAtuacaoEspecifica;
    private String nomeEquipe;
    private String nomeCargoAtual;
    private StatusAssociado statusAssociado;
    private LocalDate dataIngresso;
    private LocalDate dataVencimento;

    // ── Flag e metadados ──────────────────────────────────────────────────────

    private Boolean perfilCompleto;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}
