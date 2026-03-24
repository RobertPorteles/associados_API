package com.br.robertmiler.gerenciamento.domain.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoCargoLideranca;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoVisibilidade;
import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;
import com.br.robertmiler.gerenciamento.domain.exceptions.JaCadastradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.RegraNegocioException;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoEnderecoResidencialMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.PaginacaoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoCargoLiderancaRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoEnderecoResidencialRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoVisibilidadeRepository;

import com.br.robertmiler.gerenciamento.infrastructure.repositories.EquipeRepository;

@Service
public class AssociadoService {

	@Autowired
	private AssociadoRepository associadoRepository;

	@Autowired
	private AssociadoEnderecoResidencialRepository enderecoResidencialRepository;

	@Autowired
	private AssociadoMapper associadoMapper;

	@Autowired
	private AssociadoEnderecoResidencialMapper associadoEnderecoResidencialMapper;

	@Autowired
	private EquipeService equipeService;

	@Autowired
	private ClusterService clusterService;

	@Autowired
	private AtuacaoEspecificaService atuacaoEspecificaService;

	@Autowired
	private AssociadoVisibilidadeRepository associadoVisibilidadeRepository;

	@Autowired
	private AssociadoCargoLiderancaRepository associadoCargoLiderancaRepository;

	@Autowired
	private CargoLiderancaService cargoLiderancaService;

	@Autowired
	private PaginacaoMapper paginacaoMapper;

	@Transactional
	public AssociadoResponseDto cadastrarAssociado(AssociadoRequestDto request) {

		if (associadoRepository.findByCpf(request.getCpf()).isPresent()) {
			throw new JaCadastradoException("CPF já cadastrado para outro associado.");
		}

		if (associadoRepository.findByEmailPrincipal(request.getEmailPrincipal()).isPresent()) {
			throw new JaCadastradoException("Email já cadastrado para outro associado.");
		}

		validarCamposPausaProgramada(request);

		// Resolver FKs
		var equipeAtual = equipeService.buscarEquipeEntity(request.getIdEquipe());
		var cluster = clusterService.buscarClusterEntity(request.getIdCluster());
		var atuacaoEspecifica = atuacaoEspecificaService.buscarAtuacaoEspecificaEntity(request.getIdAtuacaoEspecifica());

		var associado = associadoMapper.toEntity(request);
		associado.setEquipeAtual(equipeAtual);
		associado.setCluster(cluster);
		associado.setAtuacaoEspecifica(atuacaoEspecifica);

		if (request.getIdEquipeOrigem() != null) {
			associado.setEquipeOrigem(equipeService.buscarEquipeEntity(request.getIdEquipeOrigem()));
		}

		if (request.getIdPadrinho() != null) {
			associado.setPadrinho(buscarAssociadoEntity(request.getIdPadrinho()));
		}

		associadoRepository.save(associado);

		// Criar registro de visibilidade automaticamente
		var visibilidade = new AssociadoVisibilidade();
		visibilidade.setAssociado(associado);
		visibilidade.setExibirAniversario(request.isExibirAniversario());
		visibilidade.setExibirEnderecoComercial(false);
		associadoVisibilidadeRepository.save(visibilidade);

		// Criar cargo inicial obrigatório (PRD §2.1 — todo associado deve ter ao menos 1 cargo)
		var cargoInicial = new AssociadoCargoLideranca();
		cargoInicial.setAssociado(associado);
		cargoInicial.setCargoLideranca(cargoLiderancaService.buscarCargoEntity(request.getIdCargoLideranca()));
		cargoInicial.setDataInicio(request.getDataInicioCargo());
		cargoInicial.setAtivo(true);
		associadoCargoLiderancaRepository.save(cargoInicial);

		// Criar endereço residencial automaticamente
		var requestEndereco = new AssociadoEnderecoResidencialRequestDto();
		requestEndereco.setRua(request.getRua());
		requestEndereco.setNumero(request.getNumero());
		requestEndereco.setComplemento(request.getComplemento());
		requestEndereco.setBairro(request.getBairro());
		requestEndereco.setCidade(request.getCidade());
		requestEndereco.setEstado(request.getEstado());
		requestEndereco.setCep(request.getCep());

		var endereco = associadoEnderecoResidencialMapper.toEntity(requestEndereco);
		endereco.setAssociado(associado);
		enderecoResidencialRepository.save(endereco);

		return associadoMapper.toResponse(associado);
	}

	@Transactional
	public AssociadoResponseDto editarAssociado(Long idAssociado, AssociadoRequestDto request) {

		var associado = buscarAssociadoEntity(idAssociado);


		//Ve se o email existe
		var emailExistente = associadoRepository.findByEmailPrincipal(request.getEmailPrincipal());
		if (emailExistente.isPresent() && !emailExistente.get().getIdAssociado().equals(idAssociado)) {
			throw new JaCadastradoException("E-mail já cadastrado para outro associado.");
		}

		validarCamposPausaProgramada(request);

		var equipeAtual = equipeService.buscarEquipeEntity(request.getIdEquipe());
		//area de atuação
		var cluster = clusterService.buscarClusterEntity(request.getIdCluster());
		//especialização
		var atuacaoEspecifica = atuacaoEspecificaService.buscarAtuacaoEspecificaEntity(request.getIdAtuacaoEspecifica());
		//cadastro comum
		associado.setNomeCompleto(request.getNomeCompleto());
		associado.setEmailPrincipal(request.getEmailPrincipal());
		associado.setTelefonePrincipal(request.getTelefonePrincipal());
		associado.setDataNascimento(request.getDataNascimento());
		associado.setDataIngresso(request.getDataIngresso());
		associado.setDataVencimento(request.getDataVencimento());
		associado.setTipoOrigemEquipe(request.getTipoOrigemEquipe());
		associado.setStatusAssociado(request.getStatusAssociado());
		associado.setDataInicioPausa(request.getDataInicioPausa());
		associado.setDataPrevisaoRetorno(request.getDataPrevisaoRetorno());
		associado.setEquipeAtual(equipeAtual);
		associado.setCluster(cluster);

		//No futuro teremos que fazer um calculo doido
		associado.setAtuacaoEspecifica(atuacaoEspecifica);

		if (request.getIdEquipeOrigem() != null) {
			associado.setEquipeOrigem(equipeService.buscarEquipeEntity(request.getIdEquipeOrigem()));
		} else {
			associado.setEquipeOrigem(null);
		}

		if (request.getIdPadrinho() != null) {
			associado.setPadrinho(buscarAssociadoEntity(request.getIdPadrinho()));
		} else {
			associado.setPadrinho(null);
		}

		associado.setAtualizadoEm(LocalDateTime.now());

		associadoRepository.save(associado);

		return associadoMapper.toResponse(associado);
	}

	@Transactional(readOnly = true)
	public PaginacaoResponseDto<AssociadoResponseDto> buscarTodosAssociados(Integer number, Integer size) {
		var pageable = PageRequest.of(number, size);
		var page = associadoRepository.findAll(pageable).map(associadoMapper::toResponse);
		return paginacaoMapper.montarDtoResposta(page);
	}

	@Transactional(readOnly = true)
	public AssociadoResponseDto buscarAssociadoPorId(Long idAssociado) {
		var associadoFound = buscarAssociadoEntity(idAssociado);
		return associadoMapper.toResponse(associadoFound);
	}

	public Associado buscarAssociadoEntity(Long idAssociado) {
		return associadoRepository.findById(idAssociado)
				.orElseThrow(() -> new NaoEncontradoException("Associado não encontrado."));
	}

	/**
	 * Valida que os campos de pausa programada são informados quando o status
	 * INATIVO_PAUSA_PROGRAMADA for selecionado, e que não são informados nos
	 * demais status.
	 */
	private void validarCamposPausaProgramada(AssociadoRequestDto request) {
		if (StatusAssociado.INATIVO_PAUSA_PROGRAMADA.equals(request.getStatusAssociado())) {
			if (request.getDataInicioPausa() == null || request.getDataPrevisaoRetorno() == null) {
				throw new RegraNegocioException(
						"Para o status 'Inativo - Pausa Programada' é obrigatório informar a data de início da pausa e a data prevista de retorno.");
			}
		} else {
			if (request.getDataInicioPausa() != null || request.getDataPrevisaoRetorno() != null) {
				throw new RegraNegocioException(
						"Os campos de pausa programada só podem ser preenchidos quando o status for 'Inativo - Pausa Programada'.");
			}
		}
	}
}
