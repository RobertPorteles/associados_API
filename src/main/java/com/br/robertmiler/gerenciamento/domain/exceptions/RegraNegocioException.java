package com.br.robertmiler.gerenciamento.domain.exceptions;

public class RegraNegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RegraNegocioException() {
		super("Regra de negócio inválida.");
	}

	public RegraNegocioException(String mensagem) {
		super(mensagem);
	}

}