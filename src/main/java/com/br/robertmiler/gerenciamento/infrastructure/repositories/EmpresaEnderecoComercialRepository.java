package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.robertmiler.gerenciamento.domain.entities.EmpresaEnderecoComercial;

@Repository
public interface EmpresaEnderecoComercialRepository extends JpaRepository<EmpresaEnderecoComercial, Long> {

    Optional<EmpresaEnderecoComercial> findByEmpresa_IdEmpresa(Long idEmpresa);

}
