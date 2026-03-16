package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;

@Component
public class AssociadoMapper {

	public AssociadoResponseDto montarDtoResposta(Associado associado) {
		AssociadoResponseDto response = new AssociadoResponseDto();
		response.setIdAssociado(associado.getIdAssociado());
		response.setNomeCompleto(associado.getNomeCompleto());
		response.setCpf(associado.getCpf());
		response.setEmailPrincipal(associado.getEmailPrincipal());
		response.setTelefonePrincipal(associado.getTelefonePrincipal());
		response.setDataNascimento(associado.getDataNascimento());
		response.setDataIngresso(associado.getDataIngresso());
		response.setDataVencimento(associado.getDataVencimento());
		response.setTipoOrigemEquipe(associado.getTipoOrigemEquipe());
		response.setStatusAtivo(associado.getStatusAtivo());
		response.setNomeEquipe(associado.getEquipeAtual().getNomeEquipe());
		response.setNomeCluster(associado.getCluster().getNome());
		response.setNomeAtuacaoEspecifica(associado.getAtuacaoEspecifica().getNome());
		return response;
	}

}
