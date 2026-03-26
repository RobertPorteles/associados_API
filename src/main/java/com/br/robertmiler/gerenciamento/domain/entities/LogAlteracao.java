package com.br.robertmiler.gerenciamento.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Registro imutável de auditoria para alterações em campos sensíveis de Associado.
 * Gerado automaticamente pelo AuditoriaAspect em métodos anotados com @RequiresLog.
 */
@Getter
@Setter
@Entity
@Table(name = "log_alteracao")
public class LogAlteracao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idLog;

    /** Nome da entidade alterada (ex: "Associado"). */
    @Column(name = "entidade", nullable = false)
    private String entidade;

    /** Nome do campo alterado (ex: "status", "equipeAtual"). */
    @Column(name = "campo_alterado", nullable = false)
    private String campoAlterado;

    /** Representação String do valor anterior (pode ser null se não disponível). */
    @Column(name = "valor_anterior", columnDefinition = "TEXT")
    private String valorAnterior;

    /** Representação String do novo valor. */
    @Column(name = "valor_novo", columnDefinition = "TEXT")
    private String valorNovo;

    /** CPF do usuário ADM_CC responsável pela alteração. */
    @Column(name = "responsavel_cpf", nullable = false)
    private String responsavelCpf;

    /** Momento exato da alteração. */
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
}
