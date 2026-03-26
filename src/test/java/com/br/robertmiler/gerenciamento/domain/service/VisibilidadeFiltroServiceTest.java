package com.br.robertmiler.gerenciamento.domain.service;

import com.br.robertmiler.gerenciamento.domain.dtos.view.AssociadoViewDto;
import com.br.robertmiler.gerenciamento.domain.enums.ClassificacaoFinanceira;
import com.br.robertmiler.gerenciamento.domain.enums.Perfil;
import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testes unitários do VisibilidadeFiltroService.
 * Cobre todas as combinações de perfil × campo conforme a tabela de visibilidade.
 */
@DisplayName("VisibilidadeFiltroService")
class VisibilidadeFiltroServiceTest {

    private VisibilidadeFiltroService service;
    private AssociadoViewDto viewDto;

    @BeforeEach
    void setUp() {
        service = new VisibilidadeFiltroService();

        // DTO base com todos os campos preenchidos
        viewDto = new AssociadoViewDto();
        viewDto.setCpfAssociado("12345678901");
        viewDto.setNomeCompleto("João da Silva");
        viewDto.setCpf("12345678901");
        viewDto.setEmail("joao@email.com");
        viewDto.setTelefone("11987654321");
        viewDto.setDataNascimento(LocalDate.of(1990, 5, 15));
        viewDto.setAniversarioDiaMes("15/05");
        viewDto.setDataIngresso(LocalDate.of(2023, 3, 10));
        viewDto.setDataVencimento(LocalDate.of(2025, 4, 1));
        viewDto.setDataPrimeiraPagamento(LocalDate.of(2023, 3, 10));
        viewDto.setCpfPadrinho("99988877766");
        viewDto.setEquipeAtual("C+C Alpha");
        viewDto.setEquipeOrigem("C+C Beta");
        viewDto.setStatus(StatusAssociado.ATIVO);
        viewDto.setCluster("Tecnologia");
        viewDto.setAtuacaoEspecifica("Desenvolvimento de Software");
        viewDto.setCargoLiderancaNome("Membro Fundador");
        viewDto.setCargoLiderancaTipo(ClassificacaoFinanceira.NORMAL);
        viewDto.setGrupamento("Construção Civil");
        viewDto.setCnpj("12345678000195");
        viewDto.setRazaoSocial("Empresa LTDA");
        // condicionais: false por padrão
        viewDto.setExibirAniversarioDiaMesNaRede(false);
        viewDto.setExibirEnderecoComercialNaRede(false);
    }

    // ────────────────────────────────────────────────────────────────────────────
    // ADM_CC — deve ver TODOS os campos
    // ────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("ADM_CC")
    class AdmCcTestes {

        @Test
        @DisplayName("deve retornar todos os campos, incluindo condicionais com boolean=false")
        void admVeTudoIndependenteDeCondicional() {
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ADM_CC, "adm-sem-cpf");

            // Campos exclusivos do ADM_CC
            assertThat(resultado).containsKey("cpfPadrinho");
            assertThat(resultado).containsKey("dataPrimeiraPagamento");
            assertThat(resultado).containsKey("equipeOrigem");
            assertThat(resultado).containsKey("cargoLiderancaTipo");

            // Campos condicionais (boolean=false, mas ADM_CC sempre vê)
            assertThat(resultado).containsKey("aniversarioDiaMes");
            assertThat(resultado).containsKey("enderecoComercial");

            // Campos REDE_CC
            assertThat(resultado).containsKeys("equipeAtual", "cluster", "dataIngresso",
                    "dataVencimento", "grupamento", "cargoLiderancaNome");
        }

        @Test
        @DisplayName("deve ver status INATIVO_DESISTENCIA")
        void admVeStatusRestrito() {
            viewDto.setStatus(StatusAssociado.INATIVO_DESISTENCIA);
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ADM_CC, null);
            assertThat(resultado).containsKey("status");
            assertThat(resultado.get("status")).isEqualTo(StatusAssociado.INATIVO_DESISTENCIA);
        }

        @Test
        @DisplayName("deve ver status INATIVO_FALECIMENTO")
        void admVeStatusFalecimento() {
            viewDto.setStatus(StatusAssociado.INATIVO_FALECIMENTO);
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ADM_CC, null);
            assertThat(resultado.get("status")).isEqualTo(StatusAssociado.INATIVO_FALECIMENTO);
        }
    }

    // ────────────────────────────────────────────────────────────────────────────
    // ASSOCIADO lendo o PRÓPRIO perfil
    // ────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("ASSOCIADO — leitura do próprio perfil")
    class AssociadoProprioPerfil {

        @Test
        @DisplayName("deve ver seus dados pessoais (nomeCompleto, cpf, email, telefone)")
        void vesDadosPessoais() {
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, "12345678901");

            assertThat(resultado).containsKeys("nomeCompleto", "cpf", "email", "telefone",
                    "dataNascimento");
        }

        @Test
        @DisplayName("NÃO deve ver cpfPadrinho (restrito a ADM_CC)")
        void naoVeCpfPadrinho() {
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, "12345678901");
            assertThat(resultado).doesNotContainKey("cpfPadrinho");
        }

        @Test
        @DisplayName("NÃO deve ver dataPrimeiraPagamento (restrito a ADM_CC)")
        void naoVeDataPrimeiraPagamento() {
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, "12345678901");
            assertThat(resultado).doesNotContainKey("dataPrimeiraPagamento");
        }

        @Test
        @DisplayName("NÃO deve ver cargoLiderancaTipo (restrito a ADM_CC)")
        void naoVeCargoTipo() {
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, "12345678901");
            assertThat(resultado).doesNotContainKey("cargoLiderancaTipo");
        }

        @Test
        @DisplayName("deve ver aniversarioDiaMes mesmo com boolean=false (próprio associado)")
        void veAniversarioProprioMesmoBooleanFalse() {
            viewDto.setExibirAniversarioDiaMesNaRede(false);
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, "12345678901");
            assertThat(resultado).containsKey("aniversarioDiaMes");
        }

        @Test
        @DisplayName("deve ver enderecoComercial mesmo com boolean=false (próprio associado)")
        void veEnderecoComercialProprioMesmoBooleanFalse() {
            viewDto.setExibirEnderecoComercialNaRede(false);
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, "12345678901");
            assertThat(resultado).containsKey("enderecoComercial");
        }

        @Test
        @DisplayName("NÃO deve ver status INATIVO_DESISTENCIA do próprio perfil")
        void naoVeStatusRestritoProprio() {
            viewDto.setStatus(StatusAssociado.INATIVO_DESISTENCIA);
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, "12345678901");
            assertThat(resultado).doesNotContainKey("status");
        }
    }

    // ────────────────────────────────────────────────────────────────────────────
    // ASSOCIADO lendo o perfil de OUTRO associado
    // ────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("ASSOCIADO — leitura de perfil alheio")
    class AssociadoPerfilAlheio {

        private static final String OUTRO_CPF = "99999999999";

        @Test
        @DisplayName("deve ver apenas campos REDE_CC (públicos)")
        void veApenasRedeCC() {
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, OUTRO_CPF);

            // REDE_CC: visíveis
            assertThat(resultado).containsKeys("equipeAtual", "dataIngresso", "dataVencimento",
                    "cluster", "atuacaoEspecifica", "cargoLiderancaNome", "grupamento");

            // Privados: NÃO visíveis
            assertThat(resultado).doesNotContainKeys("nomeCompleto", "cpf", "email",
                    "telefone", "dataNascimento", "cpfPadrinho",
                    "dataPrimeiraPagamento", "equipeOrigem", "cargoLiderancaTipo");
        }

        @Test
        @DisplayName("NÃO deve ver aniversarioDiaMes quando boolean=false")
        void naoVeAniversarioQuandoBooleanFalso() {
            viewDto.setExibirAniversarioDiaMesNaRede(false);
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, OUTRO_CPF);
            assertThat(resultado).doesNotContainKey("aniversarioDiaMes");
        }

        @Test
        @DisplayName("deve ver aniversarioDiaMes quando boolean=true")
        void veAniversarioQuandoBooleanVerdadeiro() {
            viewDto.setExibirAniversarioDiaMesNaRede(true);
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, OUTRO_CPF);
            assertThat(resultado).containsKey("aniversarioDiaMes");
            assertThat(resultado.get("aniversarioDiaMes")).isEqualTo("15/05");
        }

        @Test
        @DisplayName("NÃO deve ver enderecoComercial quando boolean=false")
        void naoVeEnderecoComercialBooleanFalso() {
            viewDto.setExibirEnderecoComercialNaRede(false);
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, OUTRO_CPF);
            assertThat(resultado).doesNotContainKey("enderecoComercial");
        }

        @Test
        @DisplayName("deve ver enderecoComercial quando boolean=true")
        void veEnderecoComercialBooleanVerdadeiro() {
            viewDto.setExibirEnderecoComercialNaRede(true);
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, OUTRO_CPF);
            assertThat(resultado).containsKey("enderecoComercial");
        }

        @Test
        @DisplayName("NÃO deve ver status INATIVO_DESISTENCIA de outro associado")
        void naoVeStatusRestritoDeOutro() {
            viewDto.setStatus(StatusAssociado.INATIVO_DESISTENCIA);
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, OUTRO_CPF);
            assertThat(resultado).doesNotContainKey("status");
        }

        @Test
        @DisplayName("deve ver status ATIVO de outro associado")
        void veStatusAtivoDeOutro() {
            viewDto.setStatus(StatusAssociado.ATIVO);
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, OUTRO_CPF);
            assertThat(resultado).containsKey("status");
            assertThat(resultado.get("status")).isEqualTo(StatusAssociado.ATIVO);
        }

        @Test
        @DisplayName("deve ver status INATIVO_PAUSA_PROGRAMADA de outro associado")
        void veStatusPausaDeOutro() {
            viewDto.setStatus(StatusAssociado.INATIVO_PAUSA_PROGRAMADA);
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.ASSOCIADO, OUTRO_CPF);
            assertThat(resultado).containsKey("status");
        }
    }

    // ────────────────────────────────────────────────────────────────────────────
    // DIRETOR
    // ────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("DIRETOR")
    class DiretorTestes {

        @Test
        @DisplayName("deve ver campos REDE_CC como qualquer membro")
        void veRedeCC() {
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.DIRETOR, "88877766655");

            assertThat(resultado).containsKeys("equipeAtual", "dataIngresso", "dataVencimento",
                    "cluster", "cargoLiderancaNome", "grupamento");
        }

        @Test
        @DisplayName("NÃO deve ver cpfPadrinho (restrito a ADM_CC)")
        void naoVeCpfPadrinho() {
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.DIRETOR, "88877766655");
            assertThat(resultado).doesNotContainKey("cpfPadrinho");
        }

        @Test
        @DisplayName("NÃO deve ver status INATIVO_DESLIGADO")
        void naoVeStatusRestritoDesligado() {
            viewDto.setStatus(StatusAssociado.INATIVO_DESLIGADO);
            Map<String, Object> resultado = service.filtrar(viewDto, Perfil.DIRETOR, "88877766655");
            assertThat(resultado).doesNotContainKey("status");
        }
    }

    // ────────────────────────────────────────────────────────────────────────────
    // validarPermissaoEdicao
    // ────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("validarPermissaoEdicao")
    class ValidarEdicao {

        @Test
        @DisplayName("ADM_CC pode editar grupamento")
        void admPodeEditarGrupamento() {
            // Não deve lançar exceção
            service.validarPermissaoEdicao(AssociadoViewDto.class, "grupamento", Perfil.ADM_CC);
        }

        @Test
        @DisplayName("DIRETOR pode editar grupamento")
        void diretorPodeEditarGrupamento() {
            service.validarPermissaoEdicao(AssociadoViewDto.class, "grupamento", Perfil.DIRETOR);
        }

        @Test
        @DisplayName("ASSOCIADO NÃO pode editar grupamento")
        void associadoNaoPodeEditarGrupamento() {
            assertThatThrownBy(() ->
                service.validarPermissaoEdicao(AssociadoViewDto.class, "grupamento", Perfil.ASSOCIADO)
            ).isInstanceOf(AccessDeniedException.class)
             .hasMessageContaining("grupamento");
        }

        @Test
        @DisplayName("ASSOCIADO pode editar dataNascimento (campo pessoal)")
        void associadoPodeEditarDataNascimento() {
            service.validarPermissaoEdicao(AssociadoViewDto.class, "dataNascimento", Perfil.ASSOCIADO);
        }

        @Test
        @DisplayName("ASSOCIADO NÃO pode editar status")
        void associadoNaoPodeEditarStatus() {
            assertThatThrownBy(() ->
                service.validarPermissaoEdicao(AssociadoViewDto.class, "status", Perfil.ASSOCIADO)
            ).isInstanceOf(AccessDeniedException.class);
        }

        @Test
        @DisplayName("ADM_CC pode editar cpf")
        void admPodeEditarCpf() {
            service.validarPermissaoEdicao(AssociadoViewDto.class, "cpf", Perfil.ADM_CC);
        }

        @Test
        @DisplayName("ASSOCIADO NÃO pode editar cpf")
        void associadoNaoPodeEditarCpf() {
            assertThatThrownBy(() ->
                service.validarPermissaoEdicao(AssociadoViewDto.class, "cpf", Perfil.ASSOCIADO)
            ).isInstanceOf(AccessDeniedException.class);
        }

        @Test
        @DisplayName("campo inexistente lança AccessDeniedException")
        void campoInexistenteLancaExcecao() {
            assertThatThrownBy(() ->
                service.validarPermissaoEdicao(AssociadoViewDto.class, "campoQueNaoExiste", Perfil.ADM_CC)
            ).isInstanceOf(AccessDeniedException.class);
        }
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Status com visibilidade variável por valor
    // ────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Visibilidade do campo status por valor")
    class StatusVisibilidade {

        @Test
        @DisplayName("PREATIVO é visível para ASSOCIADO alheio")
        void preativoVisivelParaAssociado() {
            viewDto.setStatus(StatusAssociado.PREATIVO);
            Map<String, Object> res = service.filtrar(viewDto, Perfil.ASSOCIADO, "outro");
            assertThat(res).containsKey("status");
        }

        @Test
        @DisplayName("ATIVO é visível para ASSOCIADO alheio")
        void ativoVisivelParaAssociado() {
            viewDto.setStatus(StatusAssociado.ATIVO);
            Map<String, Object> res = service.filtrar(viewDto, Perfil.ASSOCIADO, "outro");
            assertThat(res).containsKey("status");
        }

        @Test
        @DisplayName("INATIVO_DESISTENCIA NÃO é visível para ASSOCIADO alheio")
        void desistenciaNaoVisivelParaAssociado() {
            viewDto.setStatus(StatusAssociado.INATIVO_DESISTENCIA);
            Map<String, Object> res = service.filtrar(viewDto, Perfil.ASSOCIADO, "outro");
            assertThat(res).doesNotContainKey("status");
        }

        @Test
        @DisplayName("INATIVO_FALECIMENTO NÃO é visível para DIRETOR")
        void falecimentoNaoVisivelParaDiretor() {
            viewDto.setStatus(StatusAssociado.INATIVO_FALECIMENTO);
            Map<String, Object> res = service.filtrar(viewDto, Perfil.DIRETOR, "outro");
            assertThat(res).doesNotContainKey("status");
        }

        @Test
        @DisplayName("INATIVO_DESLIGADO NÃO é visível para ASSOCIADO próprio")
        void desligadoNaoVisivelNemParaSiMesmo() {
            viewDto.setStatus(StatusAssociado.INATIVO_DESLIGADO);
            // Mesmo sendo o próprio associado, status restrito não passa pelo filtro de REDE_CC
            Map<String, Object> res = service.filtrar(viewDto, Perfil.ASSOCIADO, "12345678901");
            assertThat(res).doesNotContainKey("status");
        }
    }
}
