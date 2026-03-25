package com.br.robertmiler.gerenciamento.domain.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Perfil público do associado na rede C+C (Fase 3 do PRD).
 * Os campos automáticos (cluster, atuação, equipe, cargo, status, datas)
 * são resolvidos em tempo de consulta a partir da entidade Associado —
 * não são armazenados aqui.
 */
@Getter
@Setter
@Entity
@Table(name = "perfil_associado")
public class PerfilAssociado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idPerfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associado_id", nullable = false, unique = true)
    private Associado associado;

    // ── Campos preenchidos pelo associado ─────────────────────────────────────

    @Column(name = "foto_profissional")
    private String fotoProfissional;

    @Column(name = "nome_profissional", length = 40)
    private String nomeProfissional;

    @Column(name = "nome_empresa", length = 80)
    private String nomeEmpresa;

    @Column(name = "logomarca_empresa")
    private String logomarcaEmpresa;

    @Column(name = "telefone_principal", length = 20)
    private String telefonePrincipal;

    @Column(name = "telefone_secundario", length = 20)
    private String telefoneSecundario;

    @Column(name = "email", length = 60)
    private String email;

    @Column(name = "site", length = 60)
    private String site;

    @Column(name = "linked_in", length = 60)
    private String linkedIn;

    @Column(name = "instagram", length = 60)
    private String instagram;

    @Column(name = "you_tube", length = 60)
    private String youTube;

    @Column(name = "outra_rede_social", length = 60)
    private String outraRedeSocial;

    @Column(name = "o_que_faco", length = 200)
    private String oQueFaco;

    @Column(name = "publico_ideal", length = 150)
    private String publicoIdeal;

    @Column(name = "principal_problema_resolvo", length = 200)
    private String principalProblemaResolvo;

    @Column(name = "conexoes_estrategicas", length = 150)
    private String conexoesEstrategicas;

    @Column(name = "interesses_pessoais", length = 200)
    private String interessesPessoais;

    // ── Flag automático ───────────────────────────────────────────────────────

    /**
     * true quando todos os campos obrigatórios do perfil estão preenchidos.
     * Atualizado automaticamente pelo PerfilAssociadoService após cada save.
     */
    @Column(name = "perfil_completo", nullable = false)
    private Boolean perfilCompleto = false;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    private void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

}
