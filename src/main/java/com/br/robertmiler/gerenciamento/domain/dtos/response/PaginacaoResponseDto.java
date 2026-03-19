package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginacaoResponseDto<T> {

	private List<T> items; //dados
	private Integer page;
	private Integer size;
	private Long totalItems;
	private Integer totalPages;
	private Boolean hasNext; //tem próxima folha
	private Boolean hasPrevious; //tem folha anterior
}
