-- ============================================================
-- V2 — Tabela de auditoria: log_alteracao
-- Registros imutáveis gerados pelo AuditoriaAspect em
-- métodos anotados com @RequiresLog.
-- ============================================================

CREATE TABLE IF NOT EXISTS log_alteracao (
    id_log            BIGSERIAL       PRIMARY KEY,
    entidade          VARCHAR(100)    NOT NULL,
    campo_alterado    VARCHAR(100)    NOT NULL,
    valor_anterior    TEXT,
    valor_novo        TEXT,
    responsavel_cpf   VARCHAR(20)     NOT NULL,
    data_hora         TIMESTAMP       NOT NULL DEFAULT NOW()
);

-- Índices para consultas frequentes
CREATE INDEX IF NOT EXISTS idx_log_entidade_campo
    ON log_alteracao (entidade, campo_alterado);

CREATE INDEX IF NOT EXISTS idx_log_responsavel
    ON log_alteracao (responsavel_cpf);

CREATE INDEX IF NOT EXISTS idx_log_data_hora
    ON log_alteracao (data_hora DESC);

-- ============================================================
-- Obs: a tabela associado_visibilidade já deve existir (V1).
-- Este script apenas adiciona a tabela de auditoria.
-- ============================================================
