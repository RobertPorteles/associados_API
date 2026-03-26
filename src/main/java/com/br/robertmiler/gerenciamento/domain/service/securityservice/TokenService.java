package com.br.robertmiler.gerenciamento.domain.service.securityservice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.br.robertmiler.gerenciamento.domain.entities.Usuario;
import com.br.robertmiler.gerenciamento.domain.enums.Perfil;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private static final String ISSUER = "gerenciamento-api";

    /**
     * Gera o JWT incluindo o claim "perfil" (ADM_CC / DIRETOR / ASSOCIADO).
     * O Perfil é determinado pelo AuthService no momento do login.
     */
    public String gerarToken(Usuario usuario, Perfil perfil) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getUsername())        // e-mail do usuário
                    .withClaim("role", usuario.getRole().getRole()) // ROLE_ADM, ROLE_ASSOCIADO...
                    .withClaim("perfil", perfil.name())        // ADM_CC, DIRETOR, ASSOCIADO
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(gerarExpiracao())
                    .sign(algorithm);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    /** Valida o token e retorna o subject (e-mail). Retorna "" se inválido. */
    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build()
                .verify(token)
                .getSubject();

        } catch (JWTVerificationException e) {
            return "";
        }
    }

    /**
     * Extrai o claim "perfil" diretamente do token decodificado.
     * Retorna ASSOCIADO como fallback seguro se o claim estiver ausente.
     */
    public Perfil extrairPerfil(String token) {
        try {
            DecodedJWT decoded = JWT.decode(token);
            String perfilStr = decoded.getClaim("perfil").asString();
            if (perfilStr != null && !perfilStr.isBlank()) {
                return Perfil.valueOf(perfilStr);
            }
        } catch (Exception ignored) {
            // claim ausente ou token malformado → fallback
        }
        return Perfil.ASSOCIADO;
    }

    private Instant gerarExpiracao() {
        return LocalDateTime.now()
                .plusHours(8)
                .toInstant(ZoneOffset.of("-03:00")); // fuso de Brasília
    }
}
