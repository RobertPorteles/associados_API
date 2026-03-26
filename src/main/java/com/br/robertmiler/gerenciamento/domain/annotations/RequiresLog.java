package com.br.robertmiler.gerenciamento.domain.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marca um método de edição para auditoria automática via AuditoriaAspect.
 * O aspect intercepta a chamada, captura o responsável do SecurityContext e
 * persiste um LogAlteracao.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresLog {

    /** Nome da entidade sendo alterada (ex: "Associado"). */
    String entidade() default "";

    /** Nome do campo sendo alterado (ex: "status"). */
    String campo() default "";
}
