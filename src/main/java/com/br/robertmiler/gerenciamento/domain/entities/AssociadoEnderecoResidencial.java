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
@Table(name = "associado_endereco_residencial")
public class AssociadoEnderecoResidencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ IDENTITY é mais simples
    private Long idEndereco;

    @Column(name = "rua")
    private String rua;

    @Column(name = "numero")
    private String numero;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "cep")
    private String cep;

    // ✅ OneToOne — um endereço por Associado
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associado_id", nullable = false, unique = true)
    private Associado associado;
}

	
