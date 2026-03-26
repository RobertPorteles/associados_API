package com.br.robertmiler.gerenciamento.infrastructure.aspect;

import com.br.robertmiler.gerenciamento.domain.annotations.RequiresLog;
import com.br.robertmiler.gerenciamento.domain.entities.LogAlteracao;
import com.br.robertmiler.gerenciamento.domain.entities.Usuario;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.LogAlteracaoRepository;
import com.br.robertmiler.gerenciamento.infrastructure.security.UsuarioAutenticado;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Aspecto de auditoria: intercepta qualquer método anotado com @RequiresLog,
 * captura o responsável do SecurityContext e persiste um LogAlteracao.
 *
 * O valorAnterior é capturado antes da execução (primeiro String[] arg se existir);
 * o valorNovo é capturado dos parâmetros do método após a execução.
 *
 * Convenção de assinatura recomendada para métodos auditados:
 *   metodo(String cpfAssociado, String campo, String valorNovo)
 * O valorAnterior é preenchido pelo chamador via parâmetro adicional ou deixado null.
 */
@Aspect
@Component
public class AuditoriaAspect {

    @Autowired
    private LogAlteracaoRepository logAlteracaoRepository;

    @Around("@annotation(requiresLog)")
    public Object registrarLog(ProceedingJoinPoint joinPoint, RequiresLog requiresLog) throws Throwable {

        // a) Captura o responsável via SecurityContextHolder
        String responsavelCpf = resolverResponsavelCpf();

        // Extrai args para deduzir valorAnterior / valorNovo por convenção
        Object[] args = joinPoint.getArgs();
        String valorAnterior = extrairArg(args, 2); // índice 2 = valorAnterior por convenção
        String valorNovo     = extrairArg(args, 3); // índice 3 = valorNovo por convenção

        // Executa o método original
        Object resultado = joinPoint.proceed();

        // b) Persiste o log de auditoria
        LogAlteracao log = new LogAlteracao();
        log.setEntidade(requiresLog.entidade());
        log.setCampoAlterado(requiresLog.campo());
        log.setValorAnterior(valorAnterior);
        log.setValorNovo(valorNovo != null ? valorNovo : resumirArgs(args));
        log.setResponsavelCpf(responsavelCpf != null ? responsavelCpf : "SISTEMA");
        log.setDataHora(LocalDateTime.now());
        logAlteracaoRepository.save(log);

        return resultado;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────────

    /**
     * Resolve o CPF do responsável a partir do SecurityContext.
     * Tenta: details (UsuarioAutenticado) → principal (Usuario) → null.
     */
    private String resolverResponsavelCpf() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        // details é UsuarioAutenticado quando o SecurityFilter foi atualizado
        if (auth.getDetails() instanceof UsuarioAutenticado ua) {
            return ua.getCpf();
        }

        // Fallback: principal é Usuario (ADM não tem CPF, usa email como identificador)
        if (auth.getPrincipal() instanceof Usuario usuario) {
            if (usuario.getAssociado() != null) {
                return usuario.getAssociado().getCpf();
            }
            return usuario.getEmail(); // ADM_CC: usa e-mail como identificador
        }

        return auth.getName();
    }

    /** Retorna args[index].toString() se existir, ou null. */
    private String extrairArg(Object[] args, int index) {
        if (args != null && args.length > index && args[index] != null) {
            return args[index].toString();
        }
        return null;
    }

    /** Resumo dos argumentos quando não há posição específica disponível. */
    private String resumirArgs(Object[] args) {
        if (args == null || args.length == 0) return null;
        return Arrays.stream(args)
                .map(a -> a != null ? a.toString() : "null")
                .collect(Collectors.joining(", "));
    }
}
