package com.br.robertmiler.gerenciamento.domain.service.securityservice;

import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.Usuario;
import com.br.robertmiler.gerenciamento.domain.enums.Perfil;
import com.br.robertmiler.gerenciamento.domain.enums.UserRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários do TokenService.
 * Usa ReflectionTestUtils para injetar o secret sem precisar do contexto Spring.
 */
@DisplayName("TokenService")
class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        // Injeta o secret diretamente (simulando @Value)
        ReflectionTestUtils.setField(tokenService, "secret", "chave-de-teste-segura-para-testes-unitarios");
    }

    // ── Geração de token ─────────────────────────────────────────────────────

    @Test
    @DisplayName("gerarToken retorna string não-vazia para ADM_CC")
    void gerarTokenAdm() {
        var usuario = criarUsuarioAdm();
        String token = tokenService.gerarToken(usuario, Perfil.ADM_CC);
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("gerarToken retorna string não-vazia para ASSOCIADO")
    void gerarTokenAssociado() {
        var usuario = criarUsuarioAssociado("joao@email.com", "12345678901");
        String token = tokenService.gerarToken(usuario, Perfil.ASSOCIADO);
        assertThat(token).isNotBlank();
    }

    // ── Validação de token ───────────────────────────────────────────────────

    @Test
    @DisplayName("validarToken retorna o subject (e-mail) após geração")
    void validarTokenRetornaEmail() {
        var usuario = criarUsuarioAdm();
        String token = tokenService.gerarToken(usuario, Perfil.ADM_CC);

        String subject = tokenService.validarToken(token);
        assertThat(subject).isEqualTo("admin@hotmail.com");
    }

    @Test
    @DisplayName("validarToken retorna string vazia para token inválido")
    void tokenInvalidoRetornaVazio() {
        String resultado = tokenService.validarToken("token.invalido.qualquer");
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("validarToken retorna string vazia para token expirado/adulterado")
    void tokenAdulteradoRetornaVazio() {
        var usuario = criarUsuarioAdm();
        String token = tokenService.gerarToken(usuario, Perfil.ADM_CC);
        // Adultera o token trocando alguns caracteres
        String tokenAdulterado = token.substring(0, token.length() - 5) + "XXXXX";

        String resultado = tokenService.validarToken(tokenAdulterado);
        assertThat(resultado).isEmpty();
    }

    // ── Extração de Perfil ───────────────────────────────────────────────────

    @Test
    @DisplayName("extrairPerfil retorna ADM_CC quando claim 'perfil' = ADM_CC")
    void extrairPerfilAdm() {
        var usuario = criarUsuarioAdm();
        String token = tokenService.gerarToken(usuario, Perfil.ADM_CC);

        Perfil perfil = tokenService.extrairPerfil(token);
        assertThat(perfil).isEqualTo(Perfil.ADM_CC);
    }

    @Test
    @DisplayName("extrairPerfil retorna DIRETOR quando claim 'perfil' = DIRETOR")
    void extrairPerfilDiretor() {
        var usuario = criarUsuarioAssociado("dir@email.com", "55544433322");
        String token = tokenService.gerarToken(usuario, Perfil.DIRETOR);

        Perfil perfil = tokenService.extrairPerfil(token);
        assertThat(perfil).isEqualTo(Perfil.DIRETOR);
    }

    @Test
    @DisplayName("extrairPerfil retorna ASSOCIADO quando claim 'perfil' = ASSOCIADO")
    void extrairPerfilAssociado() {
        var usuario = criarUsuarioAssociado("joao@email.com", "12345678901");
        String token = tokenService.gerarToken(usuario, Perfil.ASSOCIADO);

        Perfil perfil = tokenService.extrairPerfil(token);
        assertThat(perfil).isEqualTo(Perfil.ASSOCIADO);
    }

    @Test
    @DisplayName("extrairPerfil retorna ASSOCIADO como fallback para token malformado")
    void extrairPerfilFallback() {
        Perfil perfil = tokenService.extrairPerfil("token.invalido");
        assertThat(perfil).isEqualTo(Perfil.ASSOCIADO);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Usuario criarUsuarioAdm() {
        var usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("admin@hotmail.com");
        usuario.setRole(UserRule.ROLE_ADM);
        usuario.setSenha("senha-hash");
        return usuario;
    }

    private Usuario criarUsuarioAssociado(String email, String cpf) {
        var associado = new Associado();
        associado.setIdAssociado(10L);
        associado.setCpf(cpf);
        associado.setEmailPrincipal(email);

        var usuario = new Usuario();
        usuario.setId(2L);
        usuario.setRole(UserRule.ROLE_ASSOCIADO);
        usuario.setSenha("senha-hash");
        usuario.setAssociado(associado);
        return usuario;
    }
}
