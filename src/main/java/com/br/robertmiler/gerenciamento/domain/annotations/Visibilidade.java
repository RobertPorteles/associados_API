package com.br.robertmiler.gerenciamento.domain.annotations;

import com.br.robertmiler.gerenciamento.domain.enums.Perfil;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controla quais perfis podem ler, inserir e editar um campo.
 *
 * condicional = true: o próprio associado expõe o campo via boolean companion
 *   (exibir<NomeCampo>NaRede). ADM_CC e o próprio associado sempre enxergam,
 *   independentemente do boolean.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Visibilidade {

    /** Perfis que podem ler o campo. REDE_CC = qualquer membro autenticado. */
    Perfil[] leitura() default {};

    /** Perfis que podem inserir o campo. */
    Perfil[] insercao() default {};

    /** Perfis que podem editar o campo após criação. */
    Perfil[] edicao() default {};

    /**
     * Quando true, a visibilidade para REDE_CC depende de um boolean companion
     * chamado exibir<FieldName>NaRede no mesmo objeto.
     */
    boolean condicional() default false;
}
