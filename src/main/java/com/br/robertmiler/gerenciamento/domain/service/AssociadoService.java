package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoEnderecoResidencial;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoEnderecoResidencialRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoRepository;

@Service
public class AssociadoService {

	@Autowired
	private EquipeService equipeService;

	@Autowired
	private ClusterService clusterService;

	@Autowired
	private AtuacaoEspecificaService atuacaoEspecificaService;

	@Autowired
	private AssociadoRepository associadoRepository;

	@Autowired
	private AssociadoEnderecoResidencialRepository enderecoResidencialRepository;

	@Autowired
	private AssociadoMapper associadoMapper;
	
	@Transactional
	public AssociadoResponseDto cadastrarAssociado(AssociadoRequestDto request) {

		var equipeFound = equipeService.buscarEquipeEntity(request.getIdEquipe());
		var clusterFound = clusterService.buscarClusterEntity(request.getIdCluster());
		var atuacaoFound = atuacaoEspecificaService.buscarAtuacaoEspecificaEntity(request.getIdAtuacaoEspecifica());

		Associado novoAssociado = new Associado();
		novoAssociado.setNomeCompleto(request.getNomeCompleto());
		novoAssociado.setCpf(request.getCpf());
		novoAssociado.setEmailPrincipal(request.getEmailPrincipal());
		novoAssociado.setTelefonePrincipal(request.getTelefonePrincipal());
		novoAssociado.setDataNascimento(request.getDataNascimento());
		novoAssociado.setDataIngresso(request.getDataIngresso());
		novoAssociado.setDataVencimento(request.getDataVencimento());
		novoAssociado.setTipoOrigemEquipe(request.getTipoOrigemEquipe());
		novoAssociado.setStatusAtivo(request.getStatusAtivo());
		novoAssociado.setEquipeAtual(equipeFound);
		novoAssociado.setEquipeOrigem(equipeFound);
		novoAssociado.setCluster(clusterFound);
		novoAssociado.setAtuacaoEspecifica(atuacaoFound);
		
		associadoRepository.save(novoAssociado);

		AssociadoEnderecoResidencial novoEndereco = new AssociadoEnderecoResidencial();
		novoEndereco.setRua(request.getRua());
		novoEndereco.setNumero(request.getNumero());
		novoEndereco.setComplemento(request.getComplemento());
		novoEndereco.setBairro(request.getBairro());
		novoEndereco.setCidade(request.getCidade());
		novoEndereco.setEstado(request.getEstado());
		novoEndereco.setCep(request.getCep());
		novoEndereco.setAssociado(novoAssociado);

		enderecoResidencialRepository.save(novoEndereco);

		return associadoMapper.montarDtoResposta(novoAssociado);

	}

	public Associado buscarAssociadoEntity(Long idAssociado) {
		return associadoRepository.findById(idAssociado)
				.orElseThrow(() -> new NaoEncontradoException("Associado não encontrado."));
	}

}
