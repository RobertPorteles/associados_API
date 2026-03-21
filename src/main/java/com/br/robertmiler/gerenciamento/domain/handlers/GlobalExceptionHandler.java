package com.br.robertmiler.gerenciamento.domain.handlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.br.robertmiler.gerenciamento.domain.exceptions.JaCadastradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.RegraNegocioException;

@ControllerAdvice
public class GlobalExceptionHandler {

	public ResponseEntity<Object> createResponse(HttpStatusCode status, Exception ex) {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("datetime", LocalDateTime.now());
		body.put("status", status.value());
		body.put("message", ex.getMessage());

		return ResponseEntity.status(status.value()).body(body);
	}

	@ExceptionHandler(JaCadastradoException.class)
	public ResponseEntity<Object> handlerJaCadastradoException(JaCadastradoException ex) {
		return createResponse(HttpStatus.CONFLICT, ex);
	}

	@ExceptionHandler(NaoEncontradoException.class)
	public ResponseEntity<Object> handlerNaoEncontrado(NaoEncontradoException ex) {
		return createResponse(HttpStatus.NOT_FOUND, ex);
	}

	@ExceptionHandler(RegraNegocioException.class)
	public ResponseEntity<Object> handlerRegraNegocio(RegraNegocioException ex) {
		return createResponse(HttpStatus.UNPROCESSABLE_CONTENT, ex);
	}

}