package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.robertmiler.gerenciamento.domain.entities.EquipePontuacaoFaixa;

public interface EquipePontuacaoFaixaRepository extends JpaRepository<EquipePontuacaoFaixa, Long> {

    List<EquipePontuacaoFaixa> findByAtivoTrue();

    List<EquipePontuacaoFaixa> findAllByOrderByMinimoAssociadosAsc();

}
