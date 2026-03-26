package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import com.br.robertmiler.gerenciamento.domain.entities.LogAlteracao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogAlteracaoRepository extends JpaRepository<LogAlteracao, Long> {

    List<LogAlteracao> findByEntidadeAndCampoAlteradoOrderByDataHoraDesc(String entidade, String campoAlterado);

    List<LogAlteracao> findByResponsavelCpfOrderByDataHoraDesc(String responsavelCpf);
}
