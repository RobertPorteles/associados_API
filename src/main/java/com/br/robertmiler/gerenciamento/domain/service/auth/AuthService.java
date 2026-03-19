package com.br.robertmiler.gerenciamento.domain.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.br.robertmiler.gerenciamento.domain.dtos.request.LoginRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.LoginResponseDTO;
import com.br.robertmiler.gerenciamento.domain.entities.Usuario;
import com.br.robertmiler.gerenciamento.domain.mappers.AuthMapper;
import com.br.robertmiler.gerenciamento.domain.service.securityservice.TokenService;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthMapper authMapper;

    public LoginResponseDTO autenticar(LoginRequestDto request) {

        // 1 — monta as credenciais
        var credenciais = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getSenha()
        );

        // 2 — Spring verifica email + senha
        //     chama AuthorizationService.loadUserByUsername()
        //     compara senha com BCrypt
        //     lança BadCredentialsException se errado
        var auth = authenticationManager.authenticate(credenciais);

        // 3 — busca o Usuario autenticado
        var usuario = (Usuario) auth.getPrincipal();

        // 4 — gera o JWT
        var token = tokenService.gerarToken(usuario);

        // 5 — monta a resposta via mapper
        return authMapper.toLoginResponse(token, usuario);
    }
}
