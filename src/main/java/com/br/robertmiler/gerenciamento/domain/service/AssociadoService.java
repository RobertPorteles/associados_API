package com.br.robertmiler.gerenciamento.domain.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AlterarStatusAssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoStatusHistoricoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoCargoLideranca;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoStatusHistorico;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoVisibilidade;
import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;
import com.br.robertmiler.gerenciamento.domain.exceptions.JaCadastradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.RegraNegocioException;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoEnderecoResidencialMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoStatusHistoricoMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.PaginacaoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoCargoLiderancaRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoEnderecoResidencialRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoStatusHistoricoRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoVisibilidadeRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.UsuarioRepository;

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
	private AssociadoStatusHistoricoRepository statusHistoricoRepository;

	@Autowired
	private AssociadoStatusHistoricoMapper statusHistoricoMapper;

	@Autowired
	private UsuarioRepository usuarioRepository;

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

		var emailExistente = associadoRepository.findByEmailPrincipal(request.getEmailPrincipal());
		if (emailExistente.isPresent() && !emailExistente.get().getIdAssociado().equals(idAssociado)) {
			throw new JaCadastradoException("E-mail já cadastrado para outro associado.");
		}

		validarCamposPausaProgramada(request);

		var equipeAtual = equipeService.buscarEquipeEntity(request.getIdEquipe());
		var cluster = clusterService.buscarClusterEntity(request.getIdCluster());
		var atuacaoEspecifica = atuacaoEspecificaService.buscarAtuacaoEspecificaEntity(request.getIdAtuacaoEspecifica());

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

	/**
	 * Endpoint dedicado de mudança de status.
	 * Toda alteração gera um registro imutável em AssociadoStatusHistorico.
	 * Exclusivo da ADM — nenhuma transição ocorre automaticamente.
	 */
	@Transactional
	public AssociadoStatusHistoricoResponseDto alterarStatus(Long idAssociado,
			AlterarStatusAssociadoRequestDto request) {

		var associado = buscarAssociadoEntity(idAssociado);
		var statusAnterior = associado.getStatusAssociado();

		// Regras de negócio por status
		if (StatusAssociado.INATIVO_DESISTENCIA.equals(request.getStatusNovo())
				|| StatusAssociado.INATIVO_DESLIGADO.equals(request.getStatusNovo())) {
			if (request.getMotivo() == null || request.getMotivo().isBlank()) {
				throw new RegraNegocioException(
						"O motivo é obrigatório para os status 'Inativo - Desistência' e 'Inativo - Desligado'.");
			}
		}

		if (StatusAssociado.INATIVO_PAUSA_PROGRAMADA.equals(request.getStatusNovo())) {
			if (request.getDataInicioPausa() == null || request.getDataPrevisaoRetorno() == null) {
				throw new RegraNegocioException(
						"Para o status 'Inativo - Pausa Programada' é obrigatório informar a data de início da pausa e a data prevista de retorno.");
			}
		} else {
			// Ao sair da pausa programada, limpa os campos
			request.setDataInicioPausa(null);
			request.setDataPrevisaoRetorno(null);
		}

		// Busca o usuário responsável pela alteração
		var registradoPor = usuarioRepository.findById(request.getIdRegistradoPor())
				.orElseThrow(() -> new NaoEncontradoException("Usuário responsável não encontrado."));

		// Atualiza o associado
		associado.setStatusAssociado(request.getStatusNovo());
		associado.setDataInicioPausa(request.getDataInicioPausa());
		associado.setDataPrevisaoRetorno(request.getDataPrevisaoRetorno());
		associado.setAtualizadoEm(LocalDateTime.now());
		associadoRepository.save(associado);

		// Grava o log imutável
		var historico = new AssociadoStatusHistorico();
		historico.setAssociado(associado);
		historico.setStatusAnterior(statusAnterior);
		historico.setStatusNovo(request.getStatusNovo());
		historico.setMotivo(request.getMotivo());
		historico.setDataInicioPausa(request.getDataInicioPausa());
		historico.setDataPrevisaoRetorno(request.getDataPrevisaoRetorno());
		historico.setRegistradoPor(registradoPor);
		statusHistoricoRepository.save(historico);

		return statusHistoricoMapper.toResponse(historico);
	}

	@Transactional(readOnly = true)
	public List<AssociadoStatusHistoricoResponseDto> buscarHistoricoStatusPorAssociado(Long idAssociado) {
		buscarAssociadoEntity(idAssociado); // garante que o associado existe
		return statusHistoricoRepository
				.findByAssociado_IdAssociadoOrderByRegistradoEmDesc(idAssociado)
				.stream()
				.map(statusHistoricoMapper::toResponse)
				.collect(Collectors.toList());
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
