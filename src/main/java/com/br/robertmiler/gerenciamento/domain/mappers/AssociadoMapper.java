package com.br.robertmiler.gerenciamento.domain.mappers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.RegisterRequest;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;
import com.br.robertmiler.gerenciamento.domain.service.AtuacaoEspecificaService;
import com.br.robertmiler.gerenciamento.domain.service.ClusterService;
import com.br.robertmiler.gerenciamento.domain.service.EquipeService;

@Component
public class AssociadoMapper {
	@Autowired
	private EquipeService equipeService;

	@Autowired
	private ClusterService clusterService;

	@Autowired
	private AtuacaoEspecificaService atuacaoEspecificaService;

	public Associado toEntity(AssociadoRequestDto request) {

		var equipeFound = equipeService.buscarEquipeEntity(request.getIdEquipe());
		var clusterFound = clusterService.buscarClusterEntity(request.getIdCluster());
		var atuacaoFound = atuacaoEspecificaService.buscarAtuacaoEspecificaEntity(request.getIdAtuacaoEspecifica());

		Associado novoAssociado = new Associado();
		novoAssociado.getIdAssociado();
		novoAssociado.setNomeCompleto(FormataString.primeiraLetraMaiuscula(request.getNomeCompleto()));
		novoAssociado.setCpf(request.getCpf());
		novoAssociado.setEmailPrincipal(request.getEmailPrincipal());
		novoAssociado.setTelefonePrincipal(request.getTelefonePrincipal());
		novoAssociado.setDataNascimento(request.getDataNascimento());
		novoAssociado.setDataIngresso(request.getDataIngresso());
		novoAssociado.setDataVencimento(request.getDataVencimento());
		novoAssociado.setTipoOrigemEquipe(request.getTipoOrigemEquipe());
		novoAssociado.setStatusAssociado(request.getStatusAssociado());
		;
		novoAssociado.setCriadoEm(LocalDateTime.now());
		novoAssociado.setAtualizadoEm(LocalDateTime.now());
		novoAssociado.setEquipeAtual(equipeFound);
		novoAssociado.setEquipeOrigem(equipeFound);
		novoAssociado.setCluster(clusterFound);
		novoAssociado.setAtuacaoEspecifica(atuacaoFound);

		

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
		dto.setCriadoEm(response.getCriadoEm());
		dto.setAtualizadoEm(response.getAtualizadoEm());
		dto.setNomeEquipe(response.getEquipeAtual().getNomeEquipe());
		dto.setNomeCluster(response.getCluster().getNome());
		dto.setNomeAtuacaoEspecifica(response.getAtuacaoEspecifica().getNome());
		return dto;
	}

	// AssociadoMapper existente
	public Associado toEntityFromRegister(RegisterRequest request) {
		Associado associado = new Associado();
		associado.setNomeCompleto(request.getNomeCompleto());
		associado.setCpf(request.getCpf());
		associado.setEmailPrincipal(request.getEmail());
		return associado;
	}
}
