package com.br.robertmiler.gerenciamento.domain.dtos.view;

import com.br.robertmiler.gerenciamento.domain.annotations.Visibilidade;
import com.br.robertmiler.gerenciamento.domain.enums.ClassificacaoFinanceira;
import com.br.robertmiler.gerenciamento.domain.enums.Perfil;
import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO de visualização do Associado com TODOS os campos anotados com @Visibilidade.
 * É a entrada do VisibilidadeFiltroService.filtrar(): o serviço percorre os campos
 * via reflection e devolve somente os permitidos para o perfil solicitante.
 *
 * Campos não anotados (cpfAssociado, exibir*NaRede) são auxiliares internos
 * usados pelo serviço de filtragem — nunca aparecem no resultado.
 */
@Getter
@Setter
public class AssociadoViewDto {

    // ── Auxiliares internos (sem @Visibilidade — nunca expostos no Map) ──────────

    /** CPF do associado dono deste perfil. Usado para checar "leitura própria". */
    private String cpfAssociado;

    /**
     * Boolean companion de aniversarioDiaMes.
     * O próprio associado controla se expõe dia/mês do aniversário na rede.
     */
    private boolean exibirAniversarioDiaMesNaRede;

    /**
     * Boolean companion de enderecoComercial.
     * O próprio associado controla se expõe o endereço comercial na rede.
     */
    private boolean exibirEnderecoComercialNaRede;

    // ── Campos com regras de visibilidade ────────────────────────────────────────

    @Visibilidade(
        leitura  = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    private String nomeCompleto;

    @Visibilidade(
        leitura  = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    private String cpf;

    @Visibilidade(
        leitura  = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC, Perfil.ASSOCIADO}
    )
    private LocalDate dataNascimento;

    /**
     * Apenas Dia/Mês do aniversário.
     * CONDICIONAL ★: ADM_CC e o próprio associado sempre enxergam;
     * demais veem somente se exibirAniversarioDiaMesNaRede = true.
     */
    @Visibilidade(
        leitura     = {Perfil.REDE_CC},
        insercao    = {Perfil.ADM_CC},
        edicao      = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        condicional = true
    )
    private String aniversarioDiaMes; // formato "DD/MM"

    @Visibilidade(
        leitura  = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC, Perfil.ASSOCIADO}
    )
    private String email;

    @Visibilidade(
        leitura  = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC, Perfil.ASSOCIADO}
    )
    private String telefone;

    @Visibilidade(
        leitura  = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC, Perfil.ASSOCIADO}
    )
    private Object enderecoResidencial;

    @Visibilidade(
        leitura  = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    private String cnpj;

    @Visibilidade(
        leitura  = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC, Perfil.ASSOCIADO}
    )
    private String razaoSocial;

    /**
     * Endereço comercial da empresa.
     * CONDICIONAL ★: ADM_CC e o próprio associado sempre enxergam;
     * demais veem somente se exibirEnderecoComercialNaRede = true.
     */
    @Visibilidade(
        leitura     = {Perfil.REDE_CC},
        insercao    = {Perfil.ADM_CC},
        edicao      = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        condicional = true
    )
    private Object enderecoComercial;

    @Visibilidade(
        leitura  = {Perfil.ADM_CC},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    private LocalDate dataPrimeiraPagamento;

    @Visibilidade(
        leitura  = {Perfil.REDE_CC},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    private LocalDate dataIngresso;

    /**
     * Calculada automaticamente — nunca recebida do cliente.
     * Edição restrita: somente ADM_CC via endpoint de renovação.
     */
    @Visibilidade(
        leitura  = {Perfil.REDE_CC},
        insercao = {},        // AUTOMÁTICO
        edicao   = {Perfil.ADM_CC}
    )
    private LocalDate dataVencimento;

    @Visibilidade(
        leitura  = {Perfil.ADM_CC},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    private String cpfPadrinho;

    @Visibilidade(
        leitura  = {Perfil.ADM_CC},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    private String equipeOrigem;

    @Visibilidade(
        leitura  = {Perfil.REDE_CC},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    private String equipeAtual;

    /**
     * Visibilidade varia por valor ▼:
     *  - PREATIVO / ATIVO / INATIVO_PAUSA_PROGRAMADA → REDE_CC (todos veem)
     *  - INATIVO_DESISTENCIA / INATIVO_FALECIMENTO / INATIVO_DESLIGADO → ADM_CC apenas
     * O VisibilidadeFiltroService aplica a lógica adicional para esses valores.
     */
    @Visibilidade(
        leitura  = {Perfil.REDE_CC, Perfil.ADM_CC}, // service aplica filtro especial por valor
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    private StatusAssociado status;

    @Visibilidade(
        leitura  = {Perfil.REDE_CC},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    private String cluster;

    @Visibilidade(
        leitura  = {Perfil.REDE_CC},
        insercao = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        edicao   = {Perfil.ADM_CC}
    )
    private String atuacaoEspecifica;

    /** Nome do cargo de liderança ativo — visível para toda a rede. */
    @Visibilidade(
        leitura  = {Perfil.REDE_CC},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    private String cargoLiderancaNome;

    /**
     * Tipo financeiro do cargo (NORMAL/ISENTO).
     * Invisível para ASSOCIADO e REDE_CC — apenas ADM_CC enxerga.
     */
    @Visibilidade(
        leitura  = {Perfil.ADM_CC},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    private ClassificacaoFinanceira cargoLiderancaTipo;

    @Visibilidade(
        leitura  = {Perfil.REDE_CC},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC, Perfil.DIRETOR}
    )
    private String grupamento;
}
