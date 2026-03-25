package com.br.robertmiler.gerenciamento.domain.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilAssociadoRequestDto {

    @NotNull(message = "O associado é obrigatório.")
    @Positive(message = "O ID do associado deve ser um número positivo.")
    private Long idAssociado;

    // ── Campos obrigatórios ───────────────────────────────────────────────────

    @NotBlank(message = "A foto profissional é obrigatória.")
    private String fotoProfissional;

    @NotBlank(message = "O nome profissional é obrigatório.")
    @Size(max = 40, message = "O nome profissional deve ter no máximo 40 caracteres.")
    private String nomeProfissional;

    @NotBlank(message = "O nome da empresa é obrigatório.")
    @Size(max = 80, message = "O nome da empresa deve ter no máximo 80 caracteres.")
    private String nomeEmpresa;

    @NotBlank(message = "O telefone principal é obrigatório.")
    @Size(max = 20, message = "O telefone principal deve ter no máximo 20 caracteres.")
    private String telefonePrincipal;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail informado não é válido.")
    @Size(max = 60, message = "O e-mail deve ter no máximo 60 caracteres.")
    private String email;

    @NotBlank(message = "O campo 'O que eu faço' é obrigatório.")
    @Size(max = 200, message = "O campo 'O que eu faço' deve ter no máximo 200 caracteres.")
    private String oQueFaco;

    @NotBlank(message = "O público ideal é obrigatório.")
    @Size(max = 150, message = "O público ideal deve ter no máximo 150 caracteres.")
    private String publicoIdeal;

    @NotBlank(message = "O campo 'Principal problema que resolvo' é obrigatório.")
    @Size(max = 200, message = "O campo 'Principal problema que resolvo' deve ter no máximo 200 caracteres.")
    private String principalProblemaResolvo;

    @NotBlank(message = "As conexões estratégicas são obrigatórias.")
    @Size(max = 150, message = "As conexões estratégicas devem ter no máximo 150 caracteres.")
    private String conexoesEstrategicas;

    @NotBlank(message = "Os interesses pessoais são obrigatórios.")
    @Size(max = 200, message = "Os interesses pessoais devem ter no máximo 200 caracteres.")
    private String interessesPessoais;

    // ── Campos opcionais ──────────────────────────────────────────────────────

    private String logomarcaEmpresa;

    @Size(max = 20, message = "O telefone secundário deve ter no máximo 20 caracteres.")
    private String telefoneSecundario;

    @Size(max = 60, message = "O site deve ter no máximo 60 caracteres.")
    private String site;

    @Size(max = 60, message = "O LinkedIn deve ter no máximo 60 caracteres.")
    private String linkedIn;

    @Size(max = 60, message = "O Instagram deve ter no máximo 60 caracteres.")
    private String instagram;

    @Size(max = 60, message = "O YouTube deve ter no máximo 60 caracteres.")
    private String youTube;

    @Size(max = 60, message = "A outra rede social deve ter no máximo 60 caracteres.")
    private String outraRedeSocial;

}
