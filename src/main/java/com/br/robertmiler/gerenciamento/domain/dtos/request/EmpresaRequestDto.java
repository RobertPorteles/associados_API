package com.br.robertmiler.gerenciamento.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaRequestDto {

	@NotNull(message = "O associado é obrigatório.")
	private Long idAssociado;

	@NotBlank(message = "A razão social é obrigatória.")
	private String razaoSocial;

	@NotBlank(message = "O CNPJ é obrigatório.")
	private String cnpj;

	private String nomeFantasia;

	// ── Endereço comercial (opcional na edição) ───────────────────────────────
	private String rua;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;
	private String cep;

}
