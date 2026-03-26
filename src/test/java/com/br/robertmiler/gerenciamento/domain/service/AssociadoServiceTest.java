package com.br.robertmiler.gerenciamento.domain.service;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AlterarStatusAssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.RenovacaoAnuidadeRequestDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoCargoLideranca;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoStatusHistorico;
import com.br.robertmiler.gerenciamento.domain.entities.AssociadoVisibilidade;
import com.br.robertmiler.gerenciamento.domain.entities.CargoLideranca;
import com.br.robertmiler.gerenciamento.domain.entities.Usuario;
import com.br.robertmiler.gerenciamento.domain.enums.ClassificacaoFinanceira;
import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;
import com.br.robertmiler.gerenciamento.domain.events.AlertaSeguroFuneralEvent;
import com.br.robertmiler.gerenciamento.domain.exceptions.RegraNegocioException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes unitários das regras de negócio do AssociadoService.
 * Usa Mockito para isolar o serviço dos repositórios e eventos.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AssociadoService")
class AssociadoServiceTest {

    @Mock private AssociadoRepository associadoRepository;
    @Mock private AssociadoCargoLiderancaRepository cargoLiderancaRepository;
    @Mock private AssociadoStatusHistoricoRepository statusHistoricoRepository;
    @Mock private AssociadoVisibilidadeRepository associadoVisibilidadeRepository;
    @Mock private AssociadoEnderecoResidencialRepository enderecoResidencialRepository;
    @Mock private EmpresaRepository empresaRepository;
    @Mock private EmpresaEnderecoComercialRepository empresaEnderecoComercialRepository;
    @Mock private AssociadoGrupamentoRepository grupamentoRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private AssociadoMapper associadoMapper;
    @Mock private AssociadoStatusHistoricoMapper statusHistoricoMapper;
    @Mock private PaginacaoMapper paginacaoMapper;
    @Mock private VisibilidadeFiltroService visibilidadeFiltroService;
    @Mock private AssociadoEnderecoResidencialService enderecoResidencialService;
    @Mock private EquipeService equipeService;
    @Mock private ClusterService clusterService;
    @Mock private AtuacaoEspecificaService atuacaoEspecificaService;
    @Mock private CargoLiderancaService cargoLiderancaService;

    @InjectMocks
    private AssociadoService service;

    private Associado associado;
    private Usuario adm;

    @BeforeEach
    void setUp() {
        associado = new Associado();
        associado.setIdAssociado(1L);
        associado.setCpf("12345678901");
        associado.setNomeCompleto("João da Silva");
        associado.setEmailPrincipal("joao@email.com");
        associado.setDataIngresso(LocalDate.of(2024, 1, 15));
        associado.setStatusAssociado(StatusAssociado.ATIVO);

        adm = new Usuario();
        adm.setId(99L);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Cálculo de Data de Vencimento
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("calcularDataVencimento — regra: dia 01 do mês seguinte + 1 ano")
    class DataVencimento {

        @Test
        @DisplayName("ingresso 10/02/2026 → vencimento 01/03/2027")
        void fevereiro() {
            // Testamos indiretamente via confirmarCadastro → renovarAnuidade
            associado.setStatusAssociado(StatusAssociado.ATIVO);
            associado.setDataVencimento(LocalDate.of(2024, 3, 1)); // vencido

            var request = new RenovacaoAnuidadeRequestDto();
            request.setDataPagamento(LocalDate.of(2026, 2, 10));

            when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));
            when(cargoLiderancaRepository.findByAssociado_IdAssociado(1L)).thenReturn(List.of());
            when(associadoRepository.save(any())).thenReturn(associado);
            when(associadoMapper.toResponse(any())).thenReturn(null);

            service.renovarAnuidade(1L, request);

            // Captura o associado salvo e verifica a data calculada
            ArgumentCaptor<Associado> captor = ArgumentCaptor.forClass(Associado.class);
            verify(associadoRepository).save(captor.capture());
            assertThat(captor.getValue().getDataVencimento())
                    .isEqualTo(LocalDate.of(2027, 3, 1));
        }

        @Test
        @DisplayName("ingresso 31/12/2025 → vencimento 01/02/2027 (dezembro + 1 mês = janeiro)")
        void dezembro() {
            associado.setDataVencimento(LocalDate.of(2024, 1, 1)); // vencido

            var request = new RenovacaoAnuidadeRequestDto();
            request.setDataPagamento(LocalDate.of(2025, 12, 31));

            when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));
            when(cargoLiderancaRepository.findByAssociado_IdAssociado(1L)).thenReturn(List.of());
            when(associadoRepository.save(any())).thenReturn(associado);
            when(associadoMapper.toResponse(any())).thenReturn(null);

            service.renovarAnuidade(1L, request);

            ArgumentCaptor<Associado> captor = ArgumentCaptor.forClass(Associado.class);
            verify(associadoRepository).save(captor.capture());
            assertThat(captor.getValue().getDataVencimento())
                    .isEqualTo(LocalDate.of(2027, 2, 1));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Renovação de Anuidade
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("renovarAnuidade")
    class RenovacaoAnuidade {

        @Test
        @DisplayName("vencimento vigente → soma 12 meses ao vencimento atual")
        void vencimentoVigenteEstendePorDozesMeses() {
            associado.setDataVencimento(LocalDate.of(2030, 6, 1)); // vigente

            var request = new RenovacaoAnuidadeRequestDto();
            request.setDataPagamento(LocalDate.now());

            when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));
            when(cargoLiderancaRepository.findByAssociado_IdAssociado(1L)).thenReturn(List.of());
            when(associadoRepository.save(any())).thenReturn(associado);
            when(associadoMapper.toResponse(any())).thenReturn(null);

            service.renovarAnuidade(1L, request);

            ArgumentCaptor<Associado> captor = ArgumentCaptor.forClass(Associado.class);
            verify(associadoRepository).save(captor.capture());
            assertThat(captor.getValue().getDataVencimento())
                    .isEqualTo(LocalDate.of(2031, 6, 1));
        }

        @Test
        @DisplayName("PREATIVO não pode renovar anuidade")
        void preativoNaoPodeRenovar() {
            associado.setStatusAssociado(StatusAssociado.PREATIVO);
            when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));

            var request = new RenovacaoAnuidadeRequestDto();
            request.setDataPagamento(LocalDate.now());

            assertThatThrownBy(() -> service.renovarAnuidade(1L, request))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessageContaining("Pré-ativo");
        }

        @Test
        @DisplayName("associado ISENTO não pode renovar anuidade")
        void isentoNaoPodeRenovar() {
            var cargo = new AssociadoCargoLideranca();
            var cargoLideranca = new CargoLideranca();
            cargoLideranca.setClassificacaoFinanceira(ClassificacaoFinanceira.ISENTO);
            cargo.setCargoLideranca(cargoLideranca);
            cargo.setAtivo(true);

            when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));
            when(cargoLiderancaRepository.findByAssociado_IdAssociado(1L))
                    .thenReturn(List.of(cargo));

            var request = new RenovacaoAnuidadeRequestDto();
            request.setDataPagamento(LocalDate.now());

            assertThatThrownBy(() -> service.renovarAnuidade(1L, request))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessageContaining("isento");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Confirmação de Cadastro (PREATIVO → ATIVO)
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("confirmarCadastro")
    class ConfirmarCadastro {

        @Test
        @DisplayName("PREATIVO → ATIVO com sucesso")
        void confirmaPreativoParaAtivo() {
            associado.setStatusAssociado(StatusAssociado.PREATIVO);

            when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));
            when(associadoRepository.save(any())).thenReturn(associado);
            when(associadoMapper.toResponse(any())).thenReturn(null);

            service.confirmarCadastro(1L);

            ArgumentCaptor<Associado> captor = ArgumentCaptor.forClass(Associado.class);
            verify(associadoRepository).save(captor.capture());
            assertThat(captor.getValue().getStatusAssociado()).isEqualTo(StatusAssociado.ATIVO);
        }

        @Test
        @DisplayName("ATIVO não pode ser confirmado novamente")
        void ativoNaoPodeConfirmarNovamente() {
            associado.setStatusAssociado(StatusAssociado.ATIVO);
            when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));

            assertThatThrownBy(() -> service.confirmarCadastro(1L))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessageContaining("Pré-ativo");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Alteração de Status
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("alterarStatus")
    class AlterarStatus {

        @Test
        @DisplayName("INATIVO_FALECIMENTO dispara AlertaSeguroFuneralEvent")
        void falecimentoDisparaEvento() {
            when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));
            when(usuarioRepository.findById(99L)).thenReturn(Optional.of(adm));
            when(associadoRepository.save(any())).thenReturn(associado);
            when(statusHistoricoRepository.save(any())).thenReturn(new AssociadoStatusHistorico());
            when(statusHistoricoMapper.toResponse(any())).thenReturn(null);

            var request = new AlterarStatusAssociadoRequestDto();
            request.setStatusNovo(StatusAssociado.INATIVO_FALECIMENTO);
            request.setIdRegistradoPor(99L);

            service.alterarStatus(1L, request);

            // Verifica que o evento foi publicado com o CPF correto
            ArgumentCaptor<AlertaSeguroFuneralEvent> captor =
                    ArgumentCaptor.forClass(AlertaSeguroFuneralEvent.class);
            verify(eventPublisher).publishEvent(captor.capture());
            assertThat(captor.getValue().getCpfAssociado()).isEqualTo("12345678901");
        }

        @Test
        @DisplayName("status não-FALECIMENTO NÃO dispara AlertaSeguroFuneralEvent")
        void statusNaoFalecimentoNaoDisparaEvento() {
            when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));
            when(usuarioRepository.findById(99L)).thenReturn(Optional.of(adm));
            when(associadoRepository.save(any())).thenReturn(associado);
            when(statusHistoricoRepository.save(any())).thenReturn(new AssociadoStatusHistorico());
            when(statusHistoricoMapper.toResponse(any())).thenReturn(null);

            var request = new AlterarStatusAssociadoRequestDto();
            request.setStatusNovo(StatusAssociado.INATIVO_DESISTENCIA);
            request.setMotivo("Motivo qualquer");
            request.setIdRegistradoPor(99L);

            service.alterarStatus(1L, request);

            verify(eventPublisher, never()).publishEvent(any(AlertaSeguroFuneralEvent.class));
        }

        @Test
        @DisplayName("INATIVO_DESISTENCIA sem motivo lança RegraNegocioException")
        void desistenciaSemMotivoLancaExcecao() {
            when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));

            var request = new AlterarStatusAssociadoRequestDto();
            request.setStatusNovo(StatusAssociado.INATIVO_DESISTENCIA);
            request.setMotivo(null); // sem motivo
            request.setIdRegistradoPor(99L);

            assertThatThrownBy(() -> service.alterarStatus(1L, request))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessageContaining("motivo");
        }

        @Test
        @DisplayName("INATIVO_PAUSA_PROGRAMADA sem datas lança RegraNegocioException")
        void pausaSemDatasLancaExcecao() {
            when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));

            var request = new AlterarStatusAssociadoRequestDto();
            request.setStatusNovo(StatusAssociado.INATIVO_PAUSA_PROGRAMADA);
            request.setDataInicioPausa(null); // sem data
            request.setIdRegistradoPor(99L);

            assertThatThrownBy(() -> service.alterarStatus(1L, request))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessageContaining("Pausa Programada");
        }

        @Test
        @DisplayName("alterarStatus grava histórico com statusAnterior correto")
        void gravaHistoricoComStatusAnteriorCorreto() {
            associado.setStatusAssociado(StatusAssociado.ATIVO);

            when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));
            when(usuarioRepository.findById(99L)).thenReturn(Optional.of(adm));
            when(associadoRepository.save(any())).thenReturn(associado);
            when(statusHistoricoRepository.save(any())).thenReturn(new AssociadoStatusHistorico());
            when(statusHistoricoMapper.toResponse(any())).thenReturn(null);

            var request = new AlterarStatusAssociadoRequestDto();
            request.setStatusNovo(StatusAssociado.INATIVO_PAUSA_PROGRAMADA);
            request.setDataInicioPausa(LocalDate.now());
            request.setDataPrevisaoRetorno(LocalDate.now().plusMonths(3));
            request.setIdRegistradoPor(99L);

            service.alterarStatus(1L, request);

            ArgumentCaptor<AssociadoStatusHistorico> captor =
                    ArgumentCaptor.forClass(AssociadoStatusHistorico.class);
            verify(statusHistoricoRepository).save(captor.capture());

            assertThat(captor.getValue().getStatusAnterior()).isEqualTo(StatusAssociado.ATIVO);
            assertThat(captor.getValue().getStatusNovo())
                    .isEqualTo(StatusAssociado.INATIVO_PAUSA_PROGRAMADA);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Toggle de Permissão (visibilidade condicional)
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("togglePermissao")
    class TogglePermissao {

        @Test
        @DisplayName("toggle de aniversarioDiaMes alterna exibirAniversario")
        void toggleAniversario() {
            var vis = new AssociadoVisibilidade();
            vis.setExibirAniversario(false);

            when(associadoRepository.findByCpf("12345678901")).thenReturn(Optional.of(associado));
            when(associadoVisibilidadeRepository.findByAssociado_IdAssociado(1L))
                    .thenReturn(Optional.of(vis));
            when(associadoVisibilidadeRepository.save(any())).thenReturn(vis);

            service.togglePermissao("12345678901", "aniversarioDiaMes");

            ArgumentCaptor<AssociadoVisibilidade> captor =
                    ArgumentCaptor.forClass(AssociadoVisibilidade.class);
            verify(associadoVisibilidadeRepository).save(captor.capture());
            assertThat(captor.getValue().isExibirAniversario()).isTrue();
        }

        @Test
        @DisplayName("campo desconhecido lança RegraNegocioException")
        void campoDesconhecidoLancaExcecao() {
            when(associadoRepository.findByCpf("12345678901")).thenReturn(Optional.of(associado));

            var vis = new AssociadoVisibilidade();
            when(associadoVisibilidadeRepository.findByAssociado_IdAssociado(1L))
                    .thenReturn(Optional.of(vis));

            assertThatThrownBy(() -> service.togglePermissao("12345678901", "campoInexistente"))
                    .isInstanceOf(RegraNegocioException.class);
        }
    }
}
