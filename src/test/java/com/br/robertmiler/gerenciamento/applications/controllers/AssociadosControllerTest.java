package com.br.robertmiler.gerenciamento.applications.controllers;

import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.view.AssociadoViewDto;
import com.br.robertmiler.gerenciamento.domain.enums.Perfil;
import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;
import com.br.robertmiler.gerenciamento.domain.exceptions.RegraNegocioException;
import com.br.robertmiler.gerenciamento.domain.service.AssociadoService;
import com.br.robertmiler.gerenciamento.domain.service.VisibilidadeFiltroService;
import com.br.robertmiler.gerenciamento.infrastructure.security.UsuarioAutenticado;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes unitários do AssociadosController.
 * Verifica delegação ao service, lógica de perfil e respostas HTTP.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AssociadosController")
class AssociadosControllerTest {

    @Mock
    private AssociadoService associadoService;

    @Mock
    private VisibilidadeFiltroService visibilidadeFiltroService;

    @InjectMocks
    private AssociadosController controller;

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Authentication authAdm() {
        var auth = new UsernamePasswordAuthenticationToken(
                "admin@hotmail.com", null,
                List.of(new SimpleGrantedAuthority("ROLE_ADM"))
        );
        auth.setDetails(new UsuarioAutenticado(null, Perfil.ADM_CC));
        return auth;
    }

    private Authentication authAssociado(String cpf) {
        var auth = new UsernamePasswordAuthenticationToken(
                "joao@email.com", null,
                List.of(new SimpleGrantedAuthority("ROLE_ASSOCIADO"))
        );
        auth.setDetails(new UsuarioAutenticado(cpf, Perfil.ASSOCIADO));
        return auth;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /{cpf}/perfil
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /{cpf}/perfil — retorna JSON filtrado por perfil")
    class GetPerfilFiltrado {

        @Test
        @DisplayName("ADM_CC recebe todos os campos que o service filtrar retornar")
        void admRecebeCamposFiltrados() {
            var viewDto = new AssociadoViewDto();
            viewDto.setCpfAssociado("12345678901");

            Map<String, Object> filtrado = Map.of(
                    "nomeCompleto", "João da Silva",
                    "cpf", "12345678901",
                    "equipeAtual", "C+C Alpha",
                    "status", StatusAssociado.ATIVO
            );

            when(associadoService.buildViewDto("12345678901")).thenReturn(viewDto);
            when(visibilidadeFiltroService.filtrar(viewDto, Perfil.ADM_CC, null))
                    .thenReturn(filtrado);

            ResponseEntity<Map<String, Object>> resposta =
                    controller.getAssociadoFiltradoPorPerfil("12345678901", authAdm());

            assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(resposta.getBody()).containsKeys("nomeCompleto", "cpf", "equipeAtual");
        }

        @Test
        @DisplayName("ASSOCIADO recebe apenas campos filtrados para seu perfil")
        void associadoRecebeCamposRestritos() {
            var viewDto = new AssociadoViewDto();

            Map<String, Object> filtrado = Map.of(
                    "equipeAtual", "C+C Alpha",
                    "dataIngresso", LocalDate.of(2023, 3, 10)
            );

            when(associadoService.buildViewDto("12345678901")).thenReturn(viewDto);
            when(visibilidadeFiltroService.filtrar(eq(viewDto), eq(Perfil.ASSOCIADO), eq("99988877766")))
                    .thenReturn(filtrado);

            ResponseEntity<Map<String, Object>> resposta =
                    controller.getAssociadoFiltradoPorPerfil("12345678901", authAssociado("99988877766"));

            assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(resposta.getBody()).containsKey("equipeAtual");
            assertThat(resposta.getBody()).doesNotContainKey("nomeCompleto");
        }

        @Test
        @DisplayName("delega buildViewDto e filtrar para os serviços corretos")
        void delegaParaServicos() {
            var viewDto = new AssociadoViewDto();
            when(associadoService.buildViewDto("12345678901")).thenReturn(viewDto);
            when(visibilidadeFiltroService.filtrar(any(), any(), any())).thenReturn(Map.of());

            controller.getAssociadoFiltradoPorPerfil("12345678901", authAdm());

            verify(associadoService).buildViewDto("12345678901");
            verify(visibilidadeFiltroService).filtrar(viewDto, Perfil.ADM_CC, null);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PUT /{cpf}/campo/{nomeCampo}
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("PUT /{cpf}/campo/{nomeCampo}")
    class PutEditarCampo {

        @Test
        @DisplayName("edição bem-sucedida retorna 200 com o DTO atualizado")
        void edicaoRetorna200() {
            var response = new AssociadoResponseDto();
            response.setNomeCompleto("João Atualizado");

            when(associadoService.editarCampo("12345678901", "nomeCompleto",
                    "João da Silva", "João Atualizado", Perfil.ADM_CC))
                .thenReturn(response);

            Map<String, String> body = Map.of(
                    "valorAnterior", "João da Silva",
                    "valorNovo", "João Atualizado"
            );

            ResponseEntity<AssociadoResponseDto> resposta =
                    controller.putEditarCampo("12345678901", "nomeCompleto", body, authAdm());

            assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(resposta.getBody()).isNotNull();
            assertThat(resposta.getBody().getNomeCompleto()).isEqualTo("João Atualizado");
        }

        @Test
        @DisplayName("AccessDeniedException propagada quando perfil não tem permissão")
        void accessDeniedPropagado() {
            doThrow(new AccessDeniedException("Sem permissão para editar 'status'"))
                    .when(associadoService).editarCampo(any(), eq("status"), any(), any(), eq(Perfil.ASSOCIADO));

            assertThatThrownBy(() ->
                controller.putEditarCampo("12345678901", "status",
                        Map.of("valorNovo", "ATIVO"), authAssociado("12345678901"))
            ).isInstanceOf(AccessDeniedException.class);
        }

        @Test
        @DisplayName("RegraNegocioException propagada para campo não suportado")
        void campoNaoSuportadoLancaExcecao() {
            doThrow(new RegraNegocioException("Campo 'xpto' não suportado"))
                    .when(associadoService).editarCampo(any(), eq("xpto"), any(), any(), eq(Perfil.ADM_CC));

            assertThatThrownBy(() ->
                controller.putEditarCampo("12345678901", "xpto",
                        Map.of("valorNovo", "algo"), authAdm())
            ).isInstanceOf(RegraNegocioException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /{cpf}/permissao/{campo}
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("POST /{cpf}/permissao/{campo} — toggle de visibilidade")
    class PostTogglePermissao {

        @Test
        @DisplayName("associado faz toggle do próprio campo → 204")
        void associadoToggleProprio() {
            ResponseEntity<Void> resposta = controller.postTogglePermissao(
                    "12345678901", "aniversarioDiaMes", authAssociado("12345678901"));

            assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            verify(associadoService).togglePermissao("12345678901", "aniversarioDiaMes");
        }

        @Test
        @DisplayName("associado tentando alterar perfil alheio → 403")
        void associadoNaoPodeToggleAlheio() {
            // CPF do solicitante ≠ CPF da URL
            ResponseEntity<Void> resposta = controller.postTogglePermissao(
                    "99999999999", "aniversarioDiaMes", authAssociado("12345678901"));

            assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("ADM_CC pode fazer toggle em qualquer CPF → 204")
        void admToggleQualquerCpf() {
            ResponseEntity<Void> resposta = controller.postTogglePermissao(
                    "99999999999", "enderecoComercial", authAdm());

            assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            verify(associadoService).togglePermissao("99999999999", "enderecoComercial");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /{idAssociado} — endpoint legado
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /{idAssociado} — endpoint legado")
    class GetPorId {

        @Test
        @DisplayName("retorna 200 com dados do associado")
        void retorna200ComDados() {
            var response = new AssociadoResponseDto();
            response.setIdAssociado(1L);
            response.setNomeCompleto("João da Silva");
            response.setStatusAssociado(StatusAssociado.ATIVO);

            when(associadoService.buscarAssociadoPorId(1L)).thenReturn(response);

            ResponseEntity<AssociadoResponseDto> resposta = controller.getAssociadoPorId(1L);

            assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(resposta.getBody()).isNotNull();
            assertThat(resposta.getBody().getNomeCompleto()).isEqualTo("João da Silva");
            assertThat(resposta.getBody().getStatusAssociado()).isEqualTo(StatusAssociado.ATIVO);
        }
    }
}
