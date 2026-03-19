package com.br.robertmiler.gerenciamento.domain.mappers;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;

@Component
public class PaginacaoMapper {

	public <T> PaginacaoResponseDto<T> montarDtoResposta(Page<T> page) {

		PaginacaoResponseDto<T> response = new PaginacaoResponseDto<T>();
		response.setItems(page.getContent());
		response.setPage(page.getNumber());
		response.setSize(page.getSize());
		response.setTotalItems(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());
		response.setHasNext(page.hasNext());
		response.setHasPrevious(page.hasPrevious());

		return response;
	}
}
