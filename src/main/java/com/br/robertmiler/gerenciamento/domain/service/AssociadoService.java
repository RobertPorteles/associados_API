package com.br.robertmiler.gerenciamento.domain.service;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoCargoLideranca;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoEnderecoResidencial;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

		associado.setDataVencimento(calcularDataVencimento(associado.getDataIngresso()));
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

		//Ve se o email existe
		var emailExistente = associadoRepository.findByEmailPrincipal(request.getEmailPrincipal());
		if (emailExistente.isPresent() && !emailExistente.get().getIdAssociado().equals(idAssociado)) {
			// TODO: Consider if emailPrincipal can be changed by the associate via APP. If so, this check needs to be more nuanced.
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
		//area de atuação
		var cluster = clusterService.buscarClusterEntity(request.getIdCluster());
		//especialização
		var atuacaoEspecifica = atuacaoEspecificaService.buscarAtuacaoEspecificaEntity(request.getIdAtuacaoEspecifica());
		var padrinho = buscarAssociadoEntity(request.getIdPadrinho());
		var equipeOrigem = equipeService.buscarEquipeEntity(request.getIdEquipeOrigem());

		//cadastro comum
		associado.setCpf(request.getCpf()); // 2.2 - CPF editável pela ADM
		associado.setNomeCompleto(request.getNomeCompleto());
		associado.setEmailPrincipal(request.getEmailPrincipal());
		associado.setTelefonePrincipal(request.getTelefonePrincipal());
		associado.setDataNascimento(request.getDataNascimento());
		associado.setDataIngresso(request.getDataIngresso());
		associado.setDataVencimento(calcularDataVencimento(request.getDataIngresso())); // 12.2 - Recalcular data de vencimento
		associado.setDataPagamentoPrimeiraAnuidade(request.getDataPagamentoPrimeiraAnuidade()); // 10.1 - Data Pagamento Primeira Anuidade
		associado.setMotivoStatusInativo(request.getMotivoStatusInativo()); // 16.6.4, 16.6.6 - Motivo para status inativo
		associado.setTipoOrigemEquipe(request.getTipoOrigemEquipe());
		associado.setStatusAssociado(request.getStatusAssociado());
		associado.setDataInicioPausa(request.getDataInicioPausa());
		associado.setDataPrevisaoRetorno(request.getDataPrevisaoRetorno());
		associado.setEquipeAtual(equipeAtual);
		associado.setCluster(cluster);

		//No futuro teremos que fazer um calculo doido
		associado.setAtuacaoEspecifica(atuacaoEspecifica);
		associado.setEquipeOrigem(equipeOrigem);
		associado.setPadrinho(padrinho);
		associado.setAtualizadoEm(LocalDateTime.now());

		// 6.2 - Editar Endereço Residencial
		// Assuming there's only one residential address per associate for simplicity.
		// In a real scenario, you might have multiple addresses or a primary address.
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

	// 12.2 - Regra de Cálculo para Data de Vencimento
	private LocalDate calcularDataVencimento(LocalDate dataIngresso) {
		if (dataIngresso == null) {
			return null; // Or throw an exception, depending on business rule
		}
		// Add 1 month to dataIngresso
		LocalDate proximoMes = dataIngresso.plusMonths(1);
		// Set day to 1st
		LocalDate primeiroDiaProximoMes = proximoMes.withDayOfMonth(1);
		// Add 1 year
		return primeiroDiaProximoMes.plusYears(1);
	}
}
