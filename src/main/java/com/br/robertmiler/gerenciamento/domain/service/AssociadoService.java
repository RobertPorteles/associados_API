package com.br.robertmiler.gerenciamento.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AlterarStatusAssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.RenovacaoAnuidadeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoStatusHistoricoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoCargoLideranca;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoEnderecoResidencial;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoStatusHistorico;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoVisibilidade;
import com.br.robertmiler.gerenciamento.domain.enums.ClassificacaoFinanceira;
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
	private AssociadoEnderecoResidencialService enderecoResidencialService;

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

		if (request.getIdPadrinho() == null) {
			throw new RegraNegocioException("Padrinho é obrigatório.");
		}

		if (request.getIdEquipeOrigem() == null) {
			throw new RegraNegocioException("Equipe de origem é obrigatória.");
		}

		if (request.getIdCargoLideranca() == null) {
			throw new RegraNegocioException("Cargo de liderança é obrigatório.");
		}

		validarCamposPausaProgramada(request);

		// Resolver FKs
		var equipeAtual = equipeService.buscarEquipeEntity(request.getIdEquipe());
		var cluster = clusterService.buscarClusterEntity(request.getIdCluster());
		var atuacaoEspecifica = atuacaoEspecificaService.buscarAtuacaoEspecificaEntity(request.getIdAtuacaoEspecifica());
		var padrinho = buscarAssociadoEntity(request.getIdPadrinho());
		var equipeOrigem = equipeService.buscarEquipeEntity(request.getIdEquipeOrigem());
		var cargoLideranca = cargoLiderancaService.buscarCargoEntity(request.getIdCargoLideranca());

		var associado = associadoMapper.toEntity(request);
		associado.setEquipeAtual(equipeAtual);
		associado.setCluster(cluster);
		associado.setDataPagamentoPrimeiraAnuidade(request.getDataPagamentoPrimeiraAnuidade());
		associado.setAtuacaoEspecifica(atuacaoEspecifica);
		associado.setEquipeOrigem(equipeOrigem);
		associado.setPadrinho(padrinho);

		// 3 - Aguardando: todo associado sempre inicia como PREATIVO, independente do valor enviado no DTO
		associado.setStatusAssociado(StatusAssociado.PREATIVO);

		// 4 - ISENTO: se o cargo inicial for isento, não aplica data de vencimento
		boolean isento = ClassificacaoFinanceira.ISENTO.equals(cargoLideranca.getClassificacaoFinanceira());
		associado.setDataVencimento(isento ? null : calcularDataVencimento(associado.getDataIngresso()));

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
		cargoInicial.setCargoLideranca(cargoLideranca);
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

		AssociadoEnderecoResidencial endereco = associadoEnderecoResidencialMapper.toEntity(requestEndereco);
		endereco.setAssociado(associado);
		enderecoResidencialRepository.save(endereco);

		return associadoMapper.toResponse(associado);
	}

	@Transactional
	public AssociadoResponseDto editarAssociado(Long idAssociado, AssociadoRequestDto request) {

		var associado = buscarAssociadoEntity(idAssociado);

		var cpfExistente = associadoRepository.findByCpf(request.getCpf());
		if (cpfExistente.isPresent() && !cpfExistente.get().getIdAssociado().equals(idAssociado)) {
			throw new JaCadastradoException("CPF já cadastrado para outro associado.");
		}

		var emailExistente = associadoRepository.findByEmailPrincipal(request.getEmailPrincipal());
		if (emailExistente.isPresent() && !emailExistente.get().getIdAssociado().equals(idAssociado)) {
			throw new JaCadastradoException("E-mail já cadastrado para outro associado.");
		}

		if (request.getIdPadrinho() == null) {
			throw new RegraNegocioException("Padrinho é obrigatório.");
		}

		if (request.getIdEquipeOrigem() == null) {
			throw new RegraNegocioException("Equipe de origem é obrigatória.");
		}

		validarCamposPausaProgramada(request);

		var equipeAtual = equipeService.buscarEquipeEntity(request.getIdEquipe());
		var cluster = clusterService.buscarClusterEntity(request.getIdCluster());
		var atuacaoEspecifica = atuacaoEspecificaService.buscarAtuacaoEspecificaEntity(request.getIdAtuacaoEspecifica());
		var padrinho = buscarAssociadoEntity(request.getIdPadrinho());
		var equipeOrigem = equipeService.buscarEquipeEntity(request.getIdEquipeOrigem());

		// 2.2 - CPF editável pela ADM
		associado.setCpf(request.getCpf());
		associado.setNomeCompleto(request.getNomeCompleto());
		associado.setEmailPrincipal(request.getEmailPrincipal());
		associado.setTelefonePrincipal(request.getTelefonePrincipal());
		associado.setDataNascimento(request.getDataNascimento());
		associado.setDataIngresso(request.getDataIngresso());
		// 12.2 + 4 - Recalcula vencimento apenas se não for isento
		associado.setDataVencimento(isAssociadoIsento(idAssociado) ? null : calcularDataVencimento(request.getDataIngresso()));
		associado.setDataPagamentoPrimeiraAnuidade(request.getDataPagamentoPrimeiraAnuidade());
		associado.setMotivoStatusInativo(request.getMotivoStatusInativo());
		associado.setTipoOrigemEquipe(request.getTipoOrigemEquipe());
		associado.setStatusAssociado(request.getStatusAssociado());
		associado.setDataInicioPausa(request.getDataInicioPausa());
		associado.setDataPrevisaoRetorno(request.getDataPrevisaoRetorno());
		associado.setEquipeAtual(equipeAtual);
		associado.setCluster(cluster);
		associado.setAtuacaoEspecifica(atuacaoEspecifica);
		associado.setEquipeOrigem(equipeOrigem);
		associado.setPadrinho(padrinho);
		associado.setAtualizadoEm(LocalDateTime.now());

		// 6.2 - Editar Endereço Residencial
		var enderecoResidencial = enderecoResidencialRepository.findByAssociado_IdAssociado(idAssociado)
				.stream().findFirst()
				.orElseThrow(() -> new NaoEncontradoException("Endereço residencial não encontrado para o associado."));

		var requestEndereco = new AssociadoEnderecoResidencialRequestDto();
		requestEndereco.setRua(request.getRua());
		requestEndereco.setNumero(request.getNumero());
		requestEndereco.setComplemento(request.getComplemento());
		requestEndereco.setBairro(request.getBairro());
		requestEndereco.setCidade(request.getCidade());
		requestEndereco.setEstado(request.getEstado());
		requestEndereco.setCep(request.getCep());
		enderecoResidencialService.editarEnderecoResidencial(enderecoResidencial.getIdEndereco(), requestEndereco);

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
		switch (request.getStatusAssociado()) {
			case INATIVO_PAUSA_PROGRAMADA:
				if (request.getDataInicioPausa() == null || request.getDataPrevisaoRetorno() == null) {
					throw new RegraNegocioException(
							"Para o status 'Inativo - Pausa Programada' é obrigatório informar a data de início da pausa e a data prevista de retorno.");
				}
				if (request.getMotivoStatusInativo() != null) {
					throw new RegraNegocioException("O campo de motivo não deve ser preenchido para o status 'Inativo - Pausa Programada'.");
				}
				break;
			case INATIVO_DESISTENCIA:
			case INATIVO_DESLIGADO:
				if (request.getMotivoStatusInativo() == null || request.getMotivoStatusInativo().isBlank()) {
					throw new RegraNegocioException("Para os status 'Inativo - Desistência' e 'Inativo - Desligado' é obrigatório informar o motivo.");
				}
				// Fallthrough to check for pausa programada fields
			default:
				if (request.getDataInicioPausa() != null || request.getDataPrevisaoRetorno() != null) {
					throw new RegraNegocioException(
							"Os campos de pausa programada só podem ser preenchidos quando o status for 'Inativo - Pausa Programada'.");
				}
				if (!StatusAssociado.INATIVO_DESISTENCIA.equals(request.getStatusAssociado()) &&
						!StatusAssociado.INATIVO_DESLIGADO.equals(request.getStatusAssociado()) &&
						request.getMotivoStatusInativo() != null) {
					throw new RegraNegocioException("O campo de motivo só pode ser preenchido para os status 'Inativo - Desistência' ou 'Inativo - Desligado'.");
				}
				break;
		}
	}

	// ── Item 3: Confirmação de Cadastro (PREATIVO → ATIVO) ──────────────────────

	/**
	 * Confirma o cadastro de um associado pré-ativo, transicionando seu status
	 * para ATIVO. Apenas associados com status PREATIVO podem ser confirmados.
	 */
	@Transactional
	public AssociadoResponseDto confirmarCadastro(Long idAssociado) {
		var associado = buscarAssociadoEntity(idAssociado);

		if (!StatusAssociado.PREATIVO.equals(associado.getStatusAssociado())) {
			throw new RegraNegocioException(
					"Somente associados com status 'Pré-ativo' podem ter o cadastro confirmado.");
		}

		associado.setStatusAssociado(StatusAssociado.ATIVO);
		associado.setAtualizadoEm(LocalDateTime.now());
		associadoRepository.save(associado);

		return associadoMapper.toResponse(associado);
	}

	// ── Item 2: Renovação da Anuidade ────────────────────────────────────────────

	/**
	 * Registra a renovação da anuidade do associado.
	 * Estende dataVencimento em +1 ano (ou recalcula se já vencida).
	 * Lança exceção se o associado for isento ou ainda estiver como PREATIVO.
	 */
	@Transactional
	public AssociadoResponseDto renovarAnuidade(Long idAssociado, RenovacaoAnuidadeRequestDto request) {
		var associado = buscarAssociadoEntity(idAssociado);

		if (StatusAssociado.PREATIVO.equals(associado.getStatusAssociado())) {
			throw new RegraNegocioException(
					"Cadastro ainda não confirmado. Confirme o cadastro antes de renovar a anuidade.");
		}

		if (isAssociadoIsento(idAssociado)) {
			throw new RegraNegocioException("Associado isento de anuidade. Renovação não aplicável.");
		}

		// Registra primeiro pagamento se ainda não definido
		if (associado.getDataPagamentoPrimeiraAnuidade() == null) {
			associado.setDataPagamentoPrimeiraAnuidade(request.getDataPagamento());
		}

		// Se ainda vigente, estende a partir do vencimento atual; se vencido, recalcula
		LocalDate novaDataVencimento;
		LocalDate hoje = LocalDate.now();
		if (associado.getDataVencimento() != null && associado.getDataVencimento().isAfter(hoje)) {
			novaDataVencimento = associado.getDataVencimento().plusYears(1);
		} else {
			novaDataVencimento = calcularDataVencimento(request.getDataPagamento());
		}

		associado.setDataVencimento(novaDataVencimento);
		associado.setAtualizadoEm(LocalDateTime.now());
		associadoRepository.save(associado);

		return associadoMapper.toResponse(associado);
	}

	// ── Item 4: Helper ISENTO ─────────────────────────────────────────────────────

	/**
	 * Retorna true se o associado possuir ao menos um cargo ativo com
	 * classificação financeira ISENTO.
	 */
	private boolean isAssociadoIsento(Long idAssociado) {
		return associadoCargoLiderancaRepository.findByAssociado_IdAssociado(idAssociado)
				.stream()
				.filter(cargo -> Boolean.TRUE.equals(cargo.getAtivo()))
				.anyMatch(cargo -> ClassificacaoFinanceira.ISENTO.equals(
						cargo.getCargoLideranca().getClassificacaoFinanceira()));
	}

	// ── 12.2 - Regra de Cálculo para Data de Vencimento ─────────────────────────

	private LocalDate calcularDataVencimento(LocalDate dataIngresso) {
		if (dataIngresso == null) {
			return null;
		}
		LocalDate proximoMes = dataIngresso.plusMonths(1);
		LocalDate primeiroDiaProximoMes = proximoMes.withDayOfMonth(1);
		return primeiroDiaProximoMes.plusYears(1);
	}
}
