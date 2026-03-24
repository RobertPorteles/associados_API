package com.br.robertmiler.gerenciamento.domain.dtos.request;

import java.time.LocalDate;

import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;
import com.br.robertmiler.gerenciamento.domain.enums.TipoOrigemEquipe;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoRequestDto {

    // ── Dados pessoais ──────────────────────────────────────────────────────────

    @NotBlank(message = "O nome completo é obrigatório.")
    @Size(min = 3, max = 150, message = "O nome completo deve ter entre 3 e 150 caracteres.")
    private String nomeCompleto;

    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    private String cpf;

    @NotBlank(message = "O e-mail principal é obrigatório.")
    @Email(message = "O e-mail informado não é válido.")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
    private String emailPrincipal;

    @NotBlank(message = "O telefone principal é obrigatório.")
    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter entre 10 e 11 dígitos numéricos (DDD + número).")
    private String telefonePrincipal;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser uma data passada.")
    private LocalDate dataNascimento;

    // ── Dados administrativos ────────────────────────────────────────────────────

    @NotNull(message = "A data de ingresso é obrigatória.")
    @PastOrPresent(message = "A data de ingresso não pode ser uma data futura.")
    private LocalDate dataIngresso;

    @FutureOrPresent(message = "A data de vencimento deve ser atual ou futura.")
    private LocalDate dataVencimento;

    @NotNull(message = "O tipo de origem da equipe é obrigatório.")
    private TipoOrigemEquipe tipoOrigemEquipe;

    @NotNull(message = "O status do associado é obrigatório.")
    private StatusAssociado statusAssociado;

    /**
     * Obrigatório quando statusAssociado = INATIVO_PAUSA_PROGRAMADA.
     * Validado em nível de service.
     */
    private LocalDate dataInicioPausa;

    /**
     * Obrigatório quando statusAssociado = INATIVO_PAUSA_PROGRAMADA.
     * Validado em nível de service.
     */
    private LocalDate dataPrevisaoRetorno;

    // ── Vínculos (FKs) ───────────────────────────────────────────────────────────

    @NotNull(message = "A equipe atual é obrigatória.")
    @Positive(message = "O ID da equipe deve ser um número positivo.")
    private Long idEquipe;

    @NotNull(message = "O cluster é obrigatório.")
    @Positive(message = "O ID do cluster deve ser um número positivo.")
    private Long idCluster;

    @NotNull(message = "A atuação específica é obrigatória.")
    @Positive(message = "O ID da atuação específica deve ser um número positivo.")
    private Long idAtuacaoEspecifica;

    @Positive(message = "O ID do padrinho deve ser um número positivo.")
    private Long idPadrinho;

    @Positive(message = "O ID da equipe de origem deve ser um número positivo.")
    private Long idEquipeOrigem;

    /**
     * Cargo de liderança inicial. Obrigatório no cadastro — todo associado
     * deve ter ao menos 1 cargo (PRD §2.1 Cargo de Liderança).
     */
    @NotNull(message = "O cargo de liderança inicial é obrigatório.")
    @Positive(message = "O ID do cargo de liderança deve ser um número positivo.")
    private Long idCargoLideranca;

    /**
     * Data de início do cargo inicial. Obrigatória no cadastro.
     */
    @NotNull(message = "A data de início do cargo é obrigatória.")
    @PastOrPresent(message = "A data de início do cargo não pode ser uma data futura.")
    private LocalDate dataInicioCargo;

    // ── Visibilidade ─────────────────────────────────────────────────────────────

    private boolean exibirAniversario;

    // ── Endereço residencial ─────────────────────────────────────────────────────

    @NotBlank(message = "A rua é obrigatória.")
    @Size(max = 150, message = "A rua deve ter no máximo 150 caracteres.")
    private String rua;

    @NotBlank(message = "O número é obrigatório.")
    @Size(max = 20, message = "O número deve ter no máximo 20 caracteres.")
    private String numero;

    @Size(max = 80, message = "O complemento deve ter no máximo 80 caracteres.")
    private String complemento;

    @NotBlank(message = "O bairro é obrigatório.")
    @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres.")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória.")
    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres.")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório.")
    @Size(min = 2, max = 2, message = "O estado deve ser a sigla UF com exatamente 2 caracteres.")
    private String estado;

    @NotBlank(message = "O CEP é obrigatório.")
    @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos numéricos.")
    private String cep;

}
