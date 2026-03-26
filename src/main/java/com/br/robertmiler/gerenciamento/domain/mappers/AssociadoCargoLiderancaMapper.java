package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoCargoLiderancaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoCargoLiderancaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoCargoLideranca;

@Component
public class AssociadoCargoLiderancaMapper {

	/**
	 * Converte o request em entidade, setando apenas campos primitivos.
	 * Os vínculos de FK (associado e cargoLideranca) devem ser
	 * resolvidos e setados pelo AssociadoCargoLiderancaService.
	 */
	public AssociadoCargoLideranca toEntity(AssociadoCargoLiderancaRequestDto request) {
		AssociadoCargoLideranca designacao = new AssociadoCargoLideranca();
		designacao.setDataInicio(request.getDataInicio());
		designacao.setDataFim(request.getDataFim());
		designacao.setAtivo(request.getAtivo());
		return designacao;
	}

	public AssociadoCargoLiderancaResponseDto toResponse(AssociadoCargoLideranca designacao) {
		AssociadoCargoLiderancaResponseDto dto = new AssociadoCargoLiderancaResponseDto();
		dto.setIdAssociadoCargo(designacao.getIdAssociadoCargo());
		dto.setNomeAssociado(designacao.getAssociado().getNomeCompleto());
		dto.setNomeCargo(designacao.getCargoLideranca().getNomeCargo()); // 19.6 - Nome do cargo é visível
		dto.setDataInicio(designacao.getDataInicio());
		dto.setDataFim(designacao.getDataFim());
		dto.setAtivo(designacao.getAtivo());
		return dto;
	}

}
