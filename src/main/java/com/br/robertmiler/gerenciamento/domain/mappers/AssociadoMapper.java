package com.br.robertmiler.gerenciamento.domain.mappers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.RegisterRequest;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;

@Component
public class AssociadoMapper {

	/**
	 * Converte o request em entidade, setando apenas campos primitivos/enum.
	 * Vínculos de FK (equipe, cluster, atuação, padrinho, equipeOrigem)
	 * devem ser resolvidos e setados pelo AssociadoService.
	 */
	public Associado toEntity(AssociadoRequestDto request) {
		Associado novoAssociado = new Associado();
		novoAssociado.setNomeCompleto(FormataString.primeiraLetraMaiuscula(request.getNomeCompleto()));
		novoAssociado.setCpf(request.getCpf());
		novoAssociado.setEmailPrincipal(request.getEmailPrincipal());
		novoAssociado.setTelefonePrincipal(request.getTelefonePrincipal());
		novoAssociado.setDataNascimento(request.getDataNascimento());
		novoAssociado.setDataIngresso(request.getDataIngresso());
		novoAssociado.setDataVencimento(request.getDataVencimento());
		novoAssociado.setTipoOrigemEquipe(request.getTipoOrigemEquipe());
		novoAssociado.setStatusAssociado(request.getStatusAssociado());
		novoAssociado.setDataInicioPausa(request.getDataInicioPausa());
		novoAssociado.setDataPrevisaoRetorno(request.getDataPrevisaoRetorno());
		novoAssociado.setDataPagamentoPrimeiraAnuidade(request.getDataPagamentoPrimeiraAnuidade());
		novoAssociado.setMotivoStatusInativo(request.getMotivoStatusInativo());
		novoAssociado.setCriadoEm(LocalDateTime.now());
		novoAssociado.setAtualizadoEm(LocalDateTime.now());
		return novoAssociado;
	}

	public AssociadoResponseDto toResponse(Associado response) {
		AssociadoResponseDto dto = new AssociadoResponseDto();
		dto.setIdAssociado(response.getIdAssociado());
		dto.setNomeCompleto(response.getNomeCompleto());
		dto.setCpf(response.getCpf());
		dto.setEmailPrincipal(response.getEmailPrincipal());
		dto.setTelefonePrincipal(response.getTelefonePrincipal());
		dto.setDataNascimento(response.getDataNascimento());
		dto.setDataIngresso(response.getDataIngresso());
		dto.setDataVencimento(response.getDataVencimento());
		dto.setTipoOrigemEquipe(response.getTipoOrigemEquipe());
		dto.setStatusAssociado(response.getStatusAssociado());
		dto.setDataInicioPausa(response.getDataInicioPausa());
		dto.setDataPrevisaoRetorno(response.getDataPrevisaoRetorno());
		dto.setDataPagamentoPrimeiraAnuidade(response.getDataPagamentoPrimeiraAnuidade());
		dto.setMotivoStatusInativo(response.getMotivoStatusInativo());
		dto.setCriadoEm(response.getCriadoEm());
		dto.setAtualizadoEm(response.getAtualizadoEm());
		dto.setNomeEquipe(response.getEquipeAtual().getNomeEquipe());
		if (response.getPadrinho() != null) dto.setNomePadrinho(response.getPadrinho().getNomeCompleto());
		if (response.getEquipeOrigem() != null) dto.setNomeEquipeOrigem(response.getEquipeOrigem().getNomeEquipe());
		dto.setNomeCluster(response.getCluster().getNome());
		dto.setNomeAtuacaoEspecifica(response.getAtuacaoEspecifica().getNome());
		return dto;
	}

	// Usado pelo fluxo de autenticação/registro
	public Associado toEntityFromRegister(RegisterRequest request) {
		Associado associado = new Associado();
		associado.setNomeCompleto(FormataString.primeiraLetraMaiuscula(request.getNomeCompleto()));
		associado.setCpf(request.getCpf());
		associado.setEmailPrincipal(request.getEmail());
		return associado;
	}
}
