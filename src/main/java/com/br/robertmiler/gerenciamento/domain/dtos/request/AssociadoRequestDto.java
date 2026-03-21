package com.br.robertmiler.gerenciamento.domain.dtos.request;

import java.time.LocalDate;

import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;
import com.br.robertmiler.gerenciamento.domain.enums.TipoOrigemEquipe;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoRequestDto {

	@NotBlank(message = "O nome completo é obrigatório.")
	private String nomeCompleto;

	@NotBlank(message = "O CPF é obrigatório.")
	@Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
	private String cpf;

	@NotBlank(message = "O e-mail principal é obrigatório.")
	@Email(message = "O e-mail informado não é válido.")
	private String emailPrincipal;

	@NotBlank(message = "O telefone principal é obrigatório.")
	private String telefonePrincipal;

	@NotNull(message = "A data de nascimento é obrigatória.")
	private LocalDate dataNascimento;

	@NotNull(message = "A data de ingresso é obrigatória.")
	private LocalDate dataIngresso;

	private LocalDate dataVencimento;

	@NotNull(message = "O tipo de origem da equipe é obrigatório.")
	private TipoOrigemEquipe tipoOrigemEquipe;

	@NotNull(message = "O status do associado é obrigatório.")
	private StatusAssociado statusAssociado;

	@NotNull(message = "A equipe é obrigatória.")
	private Long idEquipe;

	@NotNull(message = "O cluster é obrigatório.")
	private Long idCluster;

	@NotNull(message = "A atuação específica é obrigatória.")
	private Long idAtuacaoEspecifica;

	private String rua;

	private String numero;

	private String complemento;

	private String bairro;

	private String cidade;

	private String estado;

	private String cep;

}
