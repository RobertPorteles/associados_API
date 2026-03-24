package com.br.robertmiler.gerenciamento.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "associado_visibilidade")
public class AssociadoVisibilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idVisibilidade;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associado_id", nullable = false, unique = true)
    private Associado associado;

    /**
     * Quando true, expõe apenas Dia e Mês do aniversário para a rede.
     * A data de nascimento completa permanece visível somente ao próprio
     * associado e à ADM C+C.
     */
    @Column(name = "exibir_aniversario", nullable = false)
    private boolean exibirAniversario;

    /**
     * Quando true, o endereço comercial da empresa é exibido no Perfil C+C
     * e fica visível para toda a rede.
     */
    @Column(name = "exibir_endereco_comercial", nullable = false)
    private boolean exibirEnderecoComercial;

}
