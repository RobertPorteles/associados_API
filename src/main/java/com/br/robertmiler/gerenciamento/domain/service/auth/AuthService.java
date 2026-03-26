package com.br.robertmiler.gerenciamento.domain.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.br.robertmiler.gerenciamento.domain.dtos.request.LoginRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.LoginResponseDTO;
import com.br.robertmiler.gerenciamento.domain.entities.Usuario;
import com.br.robertmiler.gerenciamento.domain.enums.AtribuicoesIsentas;
import com.br.robertmiler.gerenciamento.domain.enums.Perfil;
import com.br.robertmiler.gerenciamento.domain.enums.UserRule;
import com.br.robertmiler.gerenciamento.domain.mappers.AuthMapper;
import com.br.robertmiler.gerenciamento.domain.service.securityservice.TokenService;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoCargoLiderancaRepository;

import java.util.EnumSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private AssociadoCargoLiderancaRepository cargoLiderancaRepository;

    /** Atribuições que classificam o associado como DIRETOR no sistema C+C. */
    private static final Set<AtribuicoesIsentas> ATRIBUICOES_DIRETOR = EnumSet.of(
        AtribuicoesIsentas.DE_DIRETOR_DE_EQUIPE,
        AtribuicoesIsentas.DT3_DIRETOR_DE_TERRITORIO3,
        AtribuicoesIsentas.DT2_DIRETOR_DE_TERRITORIO2,
        AtribuicoesIsentas.DT1_DIRETOR_DE_TERRITORIO1,
        AtribuicoesIsentas.DM3_DIRETOR_MASTER3,
        AtribuicoesIsentas.DM2_DIRETOR_MASTER2,
        AtribuicoesIsentas.DM1_DIRETOR_MASTER1
    );

    public LoginResponseDTO autenticar(LoginRequestDto request) {

        // 1 — monta as credenciais
        var credenciais = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getSenha()
        );

        // 2 — Spring verifica email + senha via AuthorizationService
        var auth = authenticationManager.authenticate(credenciais);

        // 3 — busca o Usuario autenticado
        var usuario = (Usuario) auth.getPrincipal();

        // 4 — determina o Perfil C+C para incluir no JWT
        Perfil perfil = determinarPerfil(usuario);

        // 5 — gera o JWT com o perfil
        var token = tokenService.gerarToken(usuario, perfil);

        // 6 — monta a resposta via mapper
        return authMapper.toLoginResponse(token, usuario);
    }

    /**
     * Mapeia UserRule → Perfil C+C.
     *
     * ROLE_ADM           → ADM_CC
     * ROLE_ASSOCIADO com cargo diretor ativo → DIRETOR
     * ROLE_ASSOCIADO regular → ASSOCIADO
     */
    private Perfil determinarPerfil(Usuario usuario) {
        if (UserRule.ROLE_ADM.equals(usuario.getRole())) {
            return Perfil.ADM_CC;
        }

        // Verifica se o associado possui cargo ativo com atribuição de diretor
        if (usuario.getAssociado() != null) {
            Long idAssociado = usuario.getAssociado().getIdAssociado();
            boolean isDiretor = cargoLiderancaRepository
                    .findByAssociado_IdAssociado(idAssociado)
                    .stream()
                    .filter(acl -> Boolean.TRUE.equals(acl.getAtivo()))
                    .anyMatch(acl -> acl.getCargoLideranca().getAtribuicaoIsenta() != null
                            && ATRIBUICOES_DIRETOR.contains(
                                    acl.getCargoLideranca().getAtribuicaoIsenta()));

            return isDiretor ? Perfil.DIRETOR : Perfil.ASSOCIADO;
        }

        return Perfil.ASSOCIADO;
    }
}
