package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.robertmiler.gerenciamento.domain.entities.AtuacaoEspecifica;

@Repository
public interface AtuacaoEspecificaRepository extends JpaRepository<AtuacaoEspecifica, Long> {

}
