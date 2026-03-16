package com.br.robertmiler.gerenciamento.domain.handlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler {

	private ResponseEntity<Object> createResponse(HttpStatus status, String message) {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("datetime", LocalDateTime.now());
		body.put("status", status.value());
		body.put("message", message);

		return ResponseEntity.status(status.value()).body(body);
	}

	@ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handlerJsonInvalido(
			org.springframework.http.converter.HttpMessageNotReadableException ex) {
		return createResponse(HttpStatus.BAD_REQUEST, "JSON inválido. Verifique a sintaxe do corpo da requisição.");
	}

	@ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
	public ResponseEntity<Object> handlerRotaNaoEncontrada(
			org.springframework.web.servlet.resource.NoResourceFoundException ex) {
		return createResponse(HttpStatus.NOT_FOUND, "Rota não encontrada. Verifique o caminho da requisição.");
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handlerMissingRequestParam(MissingServletRequestParameterException ex) {
		return createResponse(HttpStatus.BAD_REQUEST, "Parâmetro obrigatório ausente: " + ex.getParameterName() + ".");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handlerMethodArgumentNotValid(MethodArgumentNotValidException ex) {

		Map<String, Object> body = new HashMap<String, Object>();
		body.put("datetime", LocalDateTime.now());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("message", "Erro de validação nos campos da requisição.");

		Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (msg1, msg2) -> msg1));

		body.put("errors", errors);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(body);
	}
}
