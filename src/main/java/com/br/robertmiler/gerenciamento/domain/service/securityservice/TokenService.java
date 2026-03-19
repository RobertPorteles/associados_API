package com.br.robertmiler.gerenciamento.domain.service.securityservice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.br.robertmiler.gerenciamento.domain.entities.Usuario;

@Service
public class TokenService {
    
    @Value("${api.security.token.secret}")
    private String secret;

    private static final String ISSUER = "gerenciamento-api";

    public String gerarToken(Usuario usuario){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getUsername())        // e-mail do usuário
                    .withClaim("role", usuario.getRole().getRole()) // ROLE_ADM, ROLE_ASSOCIADO...
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(gerarExpiracao())
                    .sign(algorithm);

        }catch(Exception e){
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    public String validarToken(String token){ // ✅ Nome corrigido
        try{
            // ✅ CORREÇÃO AQUI: usar a variável "secret" (a mesma de cima)
            Algorithm algorithm = Algorithm.HMAC256(secret); 
            
            return JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build()
                .verify(token)
                .getSubject();

        }catch(JWTVerificationException e){
            return "";
        }
    }

     private Instant gerarExpiracao() {
        return LocalDateTime.now()
                .plusHours(8)
                .toInstant(ZoneOffset.of("-03:00")); // fuso de Brasília
    }
}