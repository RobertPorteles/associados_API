package com.br.robertmiler.gerenciamento.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.annotations.RequiresLog;
import com.br.robertmiler.gerenciamento.domain.dtos.request.AlterarStatusAssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.RenovacaoAnuidadeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoStatusHistoricoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.view.AssociadoViewDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoCargoLideranca;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoEnderecoResidencial;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoStatusHistorico;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoVisibilidade;
import com.br.robertmiler.gerenciamento.domain.enums.ClassificacaoFinanceira;
import com.br.robertmiler.gerenciamento.domain.enums.Perfil;
import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;
import com.br.robertmiler.gerenciamento.domain.events.AlertaSeguroFuneralEvent;
import com.br.robertmiler.gerenciamento.domain.exceptions.JaCadastradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.RegraNegocioException;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoEnderecoResidencialMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoStatusHistoricoMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.PaginacaoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoCargoLiderancaRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoEnderecoResidencialRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoGrupamentoRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoStatusHistoricoRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoVisibilidadeRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EmpresaEnderecoComercialRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EmpresaRepository;
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

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresaEnderecoComercialRepository empresaEnderecoComercialRepository;

    @Autowired
    private AssociadoGrupamentoRepository grupamentoRepository;

    /** Publica eventos de aplicação (ex: AlertaSeguroFuneralEvent). */
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private VisibilidadeFiltroService visibilidadeFiltroService;

    // ── Cadastro ──────────────────────────────────────────────────────────────────

    @Transactional
    public AssociadoResponseDto cadastrarAssociado(AssociadoRequestDto request) {

        if (associadoRepository.findByCpf(request.getCpf()).isPresent()) {
            throw new JaCadastradoException("CPF já cadastrado para outro associado.");
        }

        // Regra: e-mail único — lançar mensagem específica conforme spec
        if (associadoRepository.findByEmailPrincipal(request.getEmailPrincipal()).isPresent()) {
            throw new JaCadastradoException("E-mail já cadastrado na base de dados.");
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

        var equipeAtual     = equipeService.buscarEquipeEntity(request.getIdEquipe());
        var cluster         = clusterService.buscarClusterEntity(request.getIdCluster());
        var atuacaoEspecifica = atuacaoEspecificaService.buscarAtuacaoEspecificaEntity(request.getIdAtuacaoEspecifica());
        var padrinho        = buscarAssociadoEntity(request.getIdPadrinho());
        var equipeOrigem    = equipeService.buscarEquipeEntity(request.getIdEquipeOrigem());
        var cargoLideranca  = cargoLiderancaService.buscarCargoEntity(request.getIdCargoLideranca());

        var associado = associadoMapper.toEntity(request);
        associado.setEquipeAtual(equipeAtual);
        associado.setCluster(cluster);
        associado.setDataPagamentoPrimeiraAnuidade(request.getDataPagamentoPrimeiraAnuidade());
        associado.setAtuacaoEspecifica(atuacaoEspecifica);
        associado.setEquipeOrigem(equipeOrigem);
        associado.setPadrinho(padrinho);

        // Todo associado inicia como PREATIVO — status nunca muda automaticamente
        associado.setStatusAssociado(StatusAssociado.PREATIVO);

        // Se o cargo inicial for ISENTO, dataVencimento fica null
        boolean isento = ClassificacaoFinanceira.ISENTO.equals(cargoLideranca.getClassificacaoFinanceira());
        associado.setDataVencimento(isento ? null : calcularDataVencimento(associado.getDataIngresso()));

        associadoRepository.save(associado);

        // Visibilidade padrão
        var visibilidade = new AssociadoVisibilidade();
        visibilidade.setAssociado(associado);
        visibilidade.setExibirAniversario(request.isExibirAniversario());
        visibilidade.setExibirEnderecoComercial(false);
        associadoVisibilidadeRepository.save(visibilidade);

        // Cargo inicial obrigatório
        var cargoInicial = new AssociadoCargoLideranca();
        cargoInicial.setAssociado(associado);
        cargoInicial.setCargoLideranca(cargoLideranca);
        cargoInicial.setDataInicio(request.getDataInicioCargo());
        cargoInicial.setAtivo(true);
        associadoCargoLiderancaRepository.save(cargoInicial);

        // Endereço residencial
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

    // ── Edição ────────────────────────────────────────────────────────────────────

    @Transactional
    public AssociadoResponseDto editarAssociado(Long idAssociado, AssociadoRequestDto request) {

        var associado = buscarAssociadoEntity(idAssociado);

        var cpfExistente = associadoRepository.findByCpf(request.getCpf());
        if (cpfExistente.isPresent() && !cpfExistente.get().getIdAssociado().equals(idAssociado)) {
            throw new JaCadastradoException("CPF já cadastrado para outro associado.");
        }

        var emailExistente = associadoRepository.findByEmailPrincipal(request.getEmailPrincipal());
        if (emailExistente.isPresent() && !emailExistente.get().getIdAssociado().equals(idAssociado)) {
            throw new JaCadastradoException("E-mail já cadastrado na base de dados.");
        }

        if (request.getIdPadrinho() == null) {
            throw new RegraNegocioException("Padrinho é obrigatório.");
        }

        if (request.getIdEquipeOrigem() == null) {
            throw new RegraNegocioException("Equipe de origem é obrigatória.");
        }

        validarCamposPausaProgramada(request);

        var equipeAtual       = equipeService.buscarEquipeEntity(request.getIdEquipe());
        var cluster           = clusterService.buscarClusterEntity(request.getIdCluster());
        var atuacaoEspecifica = atuacaoEspecificaService.buscarAtuacaoEspecificaEntity(request.getIdAtuacaoEspecifica());
        var padrinho          = buscarAssociadoEntity(request.getIdPadrinho());
        var equipeOrigem      = equipeService.buscarEquipeEntity(request.getIdEquipeOrigem());

        associado.setCpf(request.getCpf());
        associado.setNomeCompleto(request.getNomeCompleto());
        associado.setEmailPrincipal(request.getEmailPrincipal());
        associado.setTelefonePrincipal(request.getTelefonePrincipal());
        associado.setDataNascimento(request.getDataNascimento());
        associado.setDataIngresso(request.getDataIngresso());
        associado.setDataVencimento(isAssociadoIsento(idAssociado) ? null : calcularDataVencimento(request.getDataIngresso()));
        associado.setDataPagamentoPrimeiraAnuidade(request.getDataPagamentoPrimeiraAnuidade());
        associado.setMotivoStatusInativo(request.getMotivoStatusInativo());
        associado.setTipoOrigemEquipe(request.getTipoOrigemEquipe());
        // Status NÃO é editado aqui — toda mudança de status deve usar alterarStatus()
        associado.setDataInicioPausa(request.getDataInicioPausa());
        associado.setDataPrevisaoRetorno(request.getDataPrevisaoRetorno());
        associado.setEquipeAtual(equipeAtual);
        associado.setCluster(cluster);
        associado.setAtuacaoEspecifica(atuacaoEspecifica);
        associado.setEquipeOrigem(equipeOrigem);
        associado.setPadrinho(padrinho);
        associado.setAtualizadoEm(LocalDateTime.now());

        // Editar endereço residencial junto
        var enderecoResidencial = enderecoResidencialRepository.findByAssociado_IdAssociado(idAssociado)
                .stream().findFirst()
                .orElseThrow(() -> new NaoEncontradoException("Endereço residencial não encontrado."));

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

    // ── Edição de campo individual (endpoint PUT /associados/{cpf}/campo/{nome}) ─

    /**
     * Edita um campo auditável de Associado por CPF.
     * Valida permissão de edição antes de persistir.
     *
     * Parâmetros na ordem: cpf (0), nomeCampo (1), valorAnterior (2), valorNovo (3)
     * — a convenção de índices é usada pelo AuditoriaAspect.
     */
    @RequiresLog(entidade = "Associado")
    @Transactional
    public AssociadoResponseDto editarCampo(String cpf, String nomeCampo,
                                             String valorAnterior, String valorNovo,
                                             Perfil perfilAtual) {
        // Valida permissão antes de qualquer alteração
        visibilidadeFiltroService.validarPermissaoEdicao(AssociadoViewDto.class, nomeCampo, perfilAtual);

        var associado = buscarAssociadoPorCpf(cpf);

        // Aplica a alteração conforme o campo solicitado
        switch (nomeCampo) {
            case "nomeCompleto"     -> associado.setNomeCompleto(valorNovo);
            case "cpf"              -> {
                if (associadoRepository.findByCpf(valorNovo)
                        .filter(a -> !a.getIdAssociado().equals(associado.getIdAssociado()))
                        .isPresent()) {
                    throw new JaCadastradoException("CPF já cadastrado para outro associado.");
                }
                associado.setCpf(valorNovo);
            }
            case "email"            -> {
                if (associadoRepository.findByEmailPrincipal(valorNovo)
                        .filter(a -> !a.getIdAssociado().equals(associado.getIdAssociado()))
                        .isPresent()) {
                    throw new JaCadastradoException("E-mail já cadastrado na base de dados.");
                }
                associado.setEmailPrincipal(valorNovo);
            }
            case "telefone"         -> associado.setTelefonePrincipal(valorNovo);
            case "dataNascimento"   -> associado.setDataNascimento(LocalDate.parse(valorNovo));
            case "dataIngresso"     -> {
                associado.setDataIngresso(LocalDate.parse(valorNovo));
                if (!isAssociadoIsento(associado.getIdAssociado())) {
                    associado.setDataVencimento(calcularDataVencimento(associado.getDataIngresso()));
                }
            }
            case "razaoSocial"      -> empresaRepository
                    .findByAssociado_IdAssociado(associado.getIdAssociado(), PageRequest.of(0, 1))
                    .stream().findFirst()
                    .ifPresent(emp -> { emp.setRazaoSocial(valorNovo); empresaRepository.save(emp); });
            case "equipeAtual"      -> {
                var equipe = equipeService.buscarEquipeEntity(Long.parseLong(valorNovo));
                associado.setEquipeAtual(equipe);
            }
            case "cluster"          -> {
                var cluster = clusterService.buscarClusterEntity(Long.parseLong(valorNovo));
                associado.setCluster(cluster);
            }
            case "atuacaoEspecifica" -> {
                var atu = atuacaoEspecificaService.buscarAtuacaoEspecificaEntity(Long.parseLong(valorNovo));
                associado.setAtuacaoEspecifica(atu);
            }
            default -> throw new RegraNegocioException("Campo '" + nomeCampo + "' não suportado para edição individual.");
        }

        associado.setAtualizadoEm(LocalDateTime.now());
        associadoRepository.save(associado);
        return associadoMapper.toResponse(associado);
    }

    // ── Consulta ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public PaginacaoResponseDto<AssociadoResponseDto> buscarTodosAssociados(Integer number, Integer size) {
        var pageable = PageRequest.of(number, size);
        var page = associadoRepository.findAll(pageable).map(associadoMapper::toResponse);
        return paginacaoMapper.montarDtoResposta(page);
    }

    @Transactional(readOnly = true)
    public AssociadoResponseDto buscarAssociadoPorId(Long idAssociado) {
        return associadoMapper.toResponse(buscarAssociadoEntity(idAssociado));
    }

    // ── Construção do AssociadoViewDto (para filtrar por perfil) ─────────────────

    /**
     * Constrói o AssociadoViewDto completo a partir da entidade e seus relacionamentos.
     * Este objeto é passado para VisibilidadeFiltroService.filtrar().
     */
    @Transactional(readOnly = true)
    public AssociadoViewDto buildViewDto(String cpf) {
        var associado = buscarAssociadoPorCpf(cpf);
        var dto = new AssociadoViewDto();

        // Auxiliares internos
        dto.setCpfAssociado(associado.getCpf());

        // Booleans de visibilidade condicional
        var vis = associadoVisibilidadeRepository.findByAssociado_IdAssociado(associado.getIdAssociado())
                .orElse(null);
        dto.setExibirAniversarioDiaMesNaRede(vis != null && vis.isExibirAniversario());
        dto.setExibirEnderecoComercialNaRede(vis != null && vis.isExibirEnderecoComercial());

        // Campos diretos
        dto.setNomeCompleto(associado.getNomeCompleto());
        dto.setCpf(associado.getCpf());
        dto.setDataNascimento(associado.getDataNascimento());
        // Dia/Mês do aniversário
        if (associado.getDataNascimento() != null) {
            dto.setAniversarioDiaMes(
                String.format("%02d/%02d",
                    associado.getDataNascimento().getDayOfMonth(),
                    associado.getDataNascimento().getMonthValue())
            );
        }
        dto.setEmail(associado.getEmailPrincipal());
        dto.setTelefone(associado.getTelefonePrincipal());
        dto.setDataIngresso(associado.getDataIngresso());
        dto.setDataVencimento(associado.getDataVencimento());
        dto.setDataPrimeiraPagamento(associado.getDataPagamentoPrimeiraAnuidade());
        dto.setStatus(associado.getStatusAssociado());
        dto.setCpfPadrinho(associado.getPadrinho() != null ? associado.getPadrinho().getCpf() : null);
        dto.setEquipeOrigem(associado.getEquipeOrigem() != null ? associado.getEquipeOrigem().getNomeEquipe() : null);
        dto.setEquipeAtual(associado.getEquipeAtual() != null ? associado.getEquipeAtual().getNomeEquipe() : null);
        dto.setCluster(associado.getCluster() != null ? associado.getCluster().getNome() : null);
        dto.setAtuacaoEspecifica(associado.getAtuacaoEspecifica() != null ? associado.getAtuacaoEspecifica().getNome() : null);

        // Endereço residencial
        enderecoResidencialRepository.findByAssociado_IdAssociado(associado.getIdAssociado())
                .stream().findFirst().ifPresent(dto::setEnderecoResidencial);

        // Empresa: CNPJ, razão social, endereço comercial
        empresaRepository.findByAssociado_IdAssociado(associado.getIdAssociado(), PageRequest.of(0, 1))
                .stream().findFirst().ifPresent(emp -> {
                    dto.setCnpj(emp.getCnpj());
                    dto.setRazaoSocial(emp.getRazaoSocial());
                    empresaEnderecoComercialRepository.findByEmpresa_IdEmpresa(emp.getIdEmpresa())
                            .ifPresent(dto::setEnderecoComercial);
                });

        // Cargo de liderança ativo
        associadoCargoLiderancaRepository
                .findFirstByAssociado_IdAssociadoAndAtivoTrueOrderByDataInicioDesc(associado.getIdAssociado())
                .ifPresent(acl -> {
                    dto.setCargoLiderancaNome(acl.getCargoLideranca().getNomeCargo());
                    dto.setCargoLiderancaTipo(acl.getCargoLideranca().getClassificacaoFinanceira());
                });

        // Grupamento estratégico ativo
        grupamentoRepository.findByAssociado_IdAssociado(associado.getIdAssociado())
                .stream()
                .filter(ag -> Boolean.TRUE.equals(ag.getAtivo()))
                .findFirst()
                .ifPresent(ag -> dto.setGrupamento(ag.getGrupamento().getNome()));

        return dto;
    }

    // ── Toggle de boolean condicional ─────────────────────────────────────────────

    /**
     * Alterna o boolean companion do campo condicional informado.
     * Campos suportados: "aniversarioDiaMes" (exibirAniversario) e
     * "enderecoComercial" (exibirEnderecoComercial).
     */
    @Transactional
    public void togglePermissao(String cpf, String campo) {
        var associado = buscarAssociadoPorCpf(cpf);
        var visibilidade = associadoVisibilidadeRepository
                .findByAssociado_IdAssociado(associado.getIdAssociado())
                .orElseThrow(() -> new NaoEncontradoException("Configuração de visibilidade não encontrada."));

        switch (campo) {
            case "aniversarioDiaMes" ->
                visibilidade.setExibirAniversario(!visibilidade.isExibirAniversario());
            case "enderecoComercial" ->
                visibilidade.setExibirEnderecoComercial(!visibilidade.isExibirEnderecoComercial());
            default -> throw new RegraNegocioException(
                    "Campo condicional '" + campo + "' não reconhecido. Use 'aniversarioDiaMes' ou 'enderecoComercial'.");
        }

        associadoVisibilidadeRepository.save(visibilidade);
    }

    // ── Mudança de status (exclusivo ADM_CC) ──────────────────────────────────────

    /**
     * Toda alteração de status DEVE ser feita exclusivamente por ADM_CC.
     * Gera registro imutável em AssociadoStatusHistorico.
     * Dispara AlertaSeguroFuneralEvent quando status = INATIVO_FALECIMENTO.
     */
    @RequiresLog(entidade = "Associado", campo = "status")
    @Transactional
    public AssociadoStatusHistoricoResponseDto alterarStatus(Long idAssociado,
            AlterarStatusAssociadoRequestDto request) {

        var associado = buscarAssociadoEntity(idAssociado);
        var statusAnterior = associado.getStatusAssociado();

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
                        "Para o status 'Inativo - Pausa Programada' é obrigatório informar a data de início e a data prevista de retorno.");
            }
        } else {
            request.setDataInicioPausa(null);
            request.setDataPrevisaoRetorno(null);
        }

        var registradoPor = usuarioRepository.findById(request.getIdRegistradoPor())
                .orElseThrow(() -> new NaoEncontradoException("Usuário responsável não encontrado."));

        associado.setStatusAssociado(request.getStatusNovo());
        associado.setDataInicioPausa(request.getDataInicioPausa());
        associado.setDataPrevisaoRetorno(request.getDataPrevisaoRetorno());
        associado.setAtualizadoEm(LocalDateTime.now());
        associadoRepository.save(associado);

        // Alerta de falecimento: dispara evento de aplicação
        if (StatusAssociado.INATIVO_FALECIMENTO.equals(request.getStatusNovo())) {
            eventPublisher.publishEvent(new AlertaSeguroFuneralEvent(this, associado.getCpf()));
        }

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
        buscarAssociadoEntity(idAssociado);
        return statusHistoricoRepository
                .findByAssociado_IdAssociadoOrderByRegistradoEmDesc(idAssociado)
                .stream()
                .map(statusHistoricoMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ── Confirmação de cadastro (PREATIVO → ATIVO) ───────────────────────────────

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

    // ── Renovação da anuidade ─────────────────────────────────────────────────────

    /**
     * Renova a anuidade somando exatamente 12 meses ao vencimento atual.
     * Somente ADM_CC pode acionar. Gera log via @RequiresLog.
     */
    @RequiresLog(entidade = "Associado", campo = "dataVencimento")
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

        if (associado.getDataPagamentoPrimeiraAnuidade() == null) {
            associado.setDataPagamentoPrimeiraAnuidade(request.getDataPagamento());
        }

        // Spec: somar exatamente 12 meses ao vencimento atual
        LocalDate novaDataVencimento;
        LocalDate hoje = LocalDate.now();
        if (associado.getDataVencimento() != null && associado.getDataVencimento().isAfter(hoje)) {
            novaDataVencimento = associado.getDataVencimento().plusMonths(12);
        } else {
            novaDataVencimento = calcularDataVencimento(request.getDataPagamento());
        }

        associado.setDataVencimento(novaDataVencimento);
        associado.setAtualizadoEm(LocalDateTime.now());
        associadoRepository.save(associado);

        return associadoMapper.toResponse(associado);
    }

    // ── Lookups internos ──────────────────────────────────────────────────────────

    public Associado buscarAssociadoEntity(Long idAssociado) {
        return associadoRepository.findById(idAssociado)
                .orElseThrow(() -> new NaoEncontradoException("Associado não encontrado."));
    }

    public Associado buscarAssociadoPorCpf(String cpf) {
        return associadoRepository.findByCpf(cpf)
                .orElseThrow(() -> new NaoEncontradoException("Associado não encontrado para o CPF informado."));
    }

    // ── Helpers privados ──────────────────────────────────────────────────────────

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
                // fallthrough
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

    private boolean isAssociadoIsento(Long idAssociado) {
        return associadoCargoLiderancaRepository.findByAssociado_IdAssociado(idAssociado)
                .stream()
                .filter(cargo -> Boolean.TRUE.equals(cargo.getAtivo()))
                .anyMatch(cargo -> ClassificacaoFinanceira.ISENTO.equals(
                        cargo.getCargoLideranca().getClassificacaoFinanceira()));
    }

    /**
     * Regra 4: dataVencimento = dia 01 do mês seguinte ao mês de ingresso, do ano seguinte.
     * Exemplo: ingresso 10/02/2026 → vencimento 01/03/2027.
     */
    private LocalDate calcularDataVencimento(LocalDate dataIngresso) {
        if (dataIngresso == null) return null;
        return dataIngresso.plusMonths(1).withDayOfMonth(1).plusYears(1);
    }
}
