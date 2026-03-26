package com.br.robertmiler.gerenciamento.infrastructure.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.br.robertmiler.gerenciamento.domain.entities.Usuario;
import com.br.robertmiler.gerenciamento.domain.enums.Perfil;
import com.br.robertmiler.gerenciamento.domain.service.securityservice.TokenService;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarToken(request);

        if (token != null) {
            String email = tokenService.validarToken(token);

            if (!email.isEmpty()) {

                /*
                 * ROLE_ADM tem email na própria entidade Usuario.
                 * ROLE_ASSOCIADO tem email null em Usuario — o email está em Associado.emailPrincipal.
                 */
                UserDetails usuario = usuarioRepository.findByEmail(email)
                    .or(() -> usuarioRepository.findByAssociado_EmailPrincipal(email))
                    .orElseThrow(() -> new RuntimeException("Usuário do token não encontrado"));

                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                // Extrai o Perfil do claim JWT e o CPF do associado (se disponível),
                // armazenando como details para uso nos controllers e no AuditoriaAspect.
                Perfil perfil = tokenService.extrairPerfil(token);
                String cpf = resolverCpf((Usuario) usuario);
                auth.setDetails(new UsuarioAutenticado(cpf, perfil));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }

    /** Retorna o CPF do associado vinculado ao usuário, ou null para ADM_CC puro. */
    private String resolverCpf(Usuario usuario) {
        if (usuario.getAssociado() != null) {
            return usuario.getAssociado().getCpf();
        }
        return null; // ADM_CC sem associado vinculado
    }
}
