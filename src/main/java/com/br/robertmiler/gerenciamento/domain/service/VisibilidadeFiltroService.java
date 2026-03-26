package com.br.robertmiler.gerenciamento.domain.service;

import com.br.robertmiler.gerenciamento.domain.annotations.Visibilidade;
import com.br.robertmiler.gerenciamento.domain.enums.Perfil;
import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço central de controle de visibilidade.
 *
 * filtrar() — recebe qualquer objeto com campos anotados com @Visibilidade,
 * percorre-os via reflection e devolve somente os permitidos para o perfil/solicitante.
 *
 * validarPermissaoEdicao() — lança AccessDeniedException se o perfil não
 * tiver permissão de edição para o campo solicitado.
 */
@Service
public class VisibilidadeFiltroService {

    /**
     * Filtra os campos de {@code entidade} conforme o perfil do solicitante.
     *
     * @param entidade       Objeto com campos anotados com {@code @Visibilidade}
     *                       (tipicamente {@code AssociadoViewDto}).
     * @param perfilAtual    Perfil do usuário que está fazendo a requisição.
     * @param cpfSolicitante CPF do usuário autenticado (para checar "leitura própria").
     * @return Map com apenas os campos permitidos, preservando a ordem de declaração.
     */
    public Map<String, Object> filtrar(Object entidade, Perfil perfilAtual, String cpfSolicitante) {
        Map<String, Object> resultado = new LinkedHashMap<>();

        // a) Ler o CPF do associado dono do dado (campo auxiliar não anotado)
        String cpfAssociado = lerCpfAssociado(entidade);

        // b) Percorrer todos os campos declarados na classe
        for (Field field : entidade.getClass().getDeclaredFields()) {

            // b) Ignorar campos sem @Visibilidade
            Visibilidade v = field.getAnnotation(Visibilidade.class);
            if (v == null) continue;

            field.setAccessible(true);
            Object valor;
            try {
                valor = field.get(entidade);
            } catch (IllegalAccessException e) {
                continue;
            }

            String nomeCampo = field.getName();

            // Tratamento especial: status — visibilidade varia pelo valor ▼
            if ("status".equals(nomeCampo) && valor instanceof StatusAssociado status) {
                if (isStatusRestrito(status) && perfilAtual != Perfil.ADM_CC) {
                    // INATIVO_DESISTENCIA / INATIVO_FALECIMENTO / INATIVO_DESLIGADO
                    // visíveis apenas para ADM_CC
                    continue;
                }
            }

            // c) Campos condicionais: o associado controla via boolean companion
            if (v.condicional()) {
                if (!podeVerCampoCondicional(entidade, nomeCampo, perfilAtual, cpfSolicitante, cpfAssociado)) {
                    continue;
                }
            }

            // d) Verificar se perfilAtual está autorizado a ler este campo
            if (!podeVisualizar(v.leitura(), perfilAtual, cpfSolicitante, cpfAssociado)) {
                continue;
            }

            resultado.put(nomeCampo, valor);
        }

        return resultado;
    }

    /**
     * Valida se o perfil tem permissão de edição para o campo informado.
     * Lança {@link AccessDeniedException} caso negado.
     *
     * @param entidadeClass Classe com os campos anotados (ex: AssociadoViewDto.class).
     * @param nomeCampo     Nome exato do campo a editar.
     * @param perfilAtual   Perfil do usuário solicitante.
     */
    public void validarPermissaoEdicao(Class<?> entidadeClass, String nomeCampo, Perfil perfilAtual) {
        for (Field field : entidadeClass.getDeclaredFields()) {
            if (!field.getName().equals(nomeCampo)) continue;

            Visibilidade v = field.getAnnotation(Visibilidade.class);
            if (v == null) {
                throw new AccessDeniedException(
                        "Campo '" + nomeCampo + "' não possui controle de visibilidade configurado.");
            }

            List<Perfil> perfisPermitidos = Arrays.asList(v.edicao());

            // ADM_CC sempre pode editar campos em que está listado
            if (!perfisPermitidos.contains(perfilAtual)) {
                throw new AccessDeniedException(
                        "Perfil '" + perfilAtual + "' não tem permissão para editar o campo '" + nomeCampo + "'.");
            }
            return;
        }
        throw new AccessDeniedException("Campo '" + nomeCampo + "' não encontrado na entidade.");
    }

    // ── Helpers privados ─────────────────────────────────────────────────────────

    /**
     * Decide se o perfilAtual pode ler um campo com base nos perfis declarados
     * em {@code leitura}.
     *
     * Regras:
     * - ADM_CC sempre pode ler (acesso total, nível 1).
     * - REDE_CC em leitura → qualquer autenticado pode ler.
     * - ASSOCIADO em leitura → o próprio associado pode ler (cpfSolicitante == cpfAssociado).
     *   Diretor também pode ler seus próprios dados se ASSOCIADO estiver listado.
     * - DIRETOR em leitura → qualquer diretor pode ler.
     */
    private boolean podeVisualizar(Perfil[] leitura, Perfil perfilAtual,
                                    String cpfSolicitante, String cpfAssociado) {
        // ADM_CC tem acesso total
        if (perfilAtual == Perfil.ADM_CC) return true;

        List<Perfil> perfis = Arrays.asList(leitura);

        // REDE_CC = público para todos os autenticados
        if (perfis.contains(Perfil.REDE_CC)) return true;

        // Leitura do próprio dado (ASSOCIADO ou DIRETOR lendo seu próprio perfil)
        if (perfis.contains(Perfil.ASSOCIADO)
                && cpfSolicitante != null
                && cpfSolicitante.equals(cpfAssociado)) {
            return true;
        }

        // Campo explicitamente liberado para DIRETOR
        if (perfilAtual == Perfil.DIRETOR && perfis.contains(Perfil.DIRETOR)) return true;

        return false;
    }

    /**
     * Para campos condicionais (condicional = true), verifica se deve exibir:
     *  - ADM_CC → sempre inclui.
     *  - cpfSolicitante == cpfAssociado → sempre inclui (o próprio associado).
     *  - Demais → inclui apenas se boolean companion exibir<FieldName>NaRede = true.
     */
    private boolean podeVerCampoCondicional(Object entidade, String nomeCampo,
                                             Perfil perfilAtual,
                                             String cpfSolicitante, String cpfAssociado) {
        // ADM_CC sempre enxerga
        if (perfilAtual == Perfil.ADM_CC) return true;

        // O próprio associado sempre enxerga
        if (cpfSolicitante != null && cpfSolicitante.equals(cpfAssociado)) return true;

        // Demais: checar boolean companion "exibir<FieldName>NaRede"
        return lerBooleanCompanion(entidade, nomeCampo);
    }

    /**
     * Lê o boolean companion do campo condicional.
     * Convenção: exibir + capitalize(nomeCampo) + NaRede
     * Ex: "aniversarioDiaMes" → "exibirAniversarioDiaMesNaRede"
     *     "enderecoComercial"  → "exibirEnderecoComercialNaRede"
     */
    private boolean lerBooleanCompanion(Object entidade, String nomeCampo) {
        String companionName = "exibir" + capitalize(nomeCampo) + "NaRede";
        try {
            Field companion = entidade.getClass().getDeclaredField(companionName);
            companion.setAccessible(true);
            Object val = companion.get(entidade);
            return Boolean.TRUE.equals(val) || (val instanceof Boolean b && b);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Companion não encontrado → comportamento conservador: não exibir
            return false;
        }
    }

    /** Lê o campo auxiliar {@code cpfAssociado} do objeto, se existir. */
    private String lerCpfAssociado(Object entidade) {
        try {
            Field f = entidade.getClass().getDeclaredField("cpfAssociado");
            f.setAccessible(true);
            return (String) f.get(entidade);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Status restritos: visíveis apenas para ADM_CC.
     * INATIVO_DESISTENCIA, INATIVO_FALECIMENTO, INATIVO_DESLIGADO.
     */
    private boolean isStatusRestrito(StatusAssociado status) {
        return status == StatusAssociado.INATIVO_DESISTENCIA
                || status == StatusAssociado.INATIVO_FALECIMENTO
                || status == StatusAssociado.INATIVO_DESLIGADO;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
