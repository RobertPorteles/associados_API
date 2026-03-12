package com.br.robertmiler.gerenciamento.domain.entities;

import java.security.Timestamp;
import java.sql.Date;
import java.util.UUID;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Associados")
public class Associados {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "nome_completo", unique = true)
    private String nome_completo;
    @Column(name = "cpf", unique = true)
    private String cpf;
    @Column(name = "data_nascimento")
    private Date data_nascimento;
    @Column(name = "email_principal")
    private String email_principal;
    @Column(name = "telefone_principal")
    private String telefone_principal;
    @Column(name = "data_ingresso")
    private Date data_ingresso;
    @Column(name = "data_vencimento")
    private Date data_vencimento;
    @Column(name = "tipo_origem_equipe")
    private String tipo_origem_equipe;
    @Column(name = "status_ativo")
    private Boolean status_ativo;
    @Column(name = "criado")
    private Timestamp criado_em;
    private Timestamp atualizado_em;

}
   /*

- 
-
-   id_equipe **BIGINT (FK)**
-   id_cluster **BIGINT (FK)**
-   id_atuacao_especifica **BIGINT (FK)**
-   id_padrinho **BIGINT (FK)**

-   tipo_origem_equipe **VARCHAR(30)**

-   id_equipe_origem **BIGINT (FK)**

-   status_ativo **BOOLEAN**
-   criado_em **TIMESTAMP**
-   atualizado_em **TIMESTAMP**
}
 */