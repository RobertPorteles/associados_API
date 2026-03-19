package com.br.robertmiler.gerenciamento.domain.dtos.response;

import com.br.robertmiler.gerenciamento.domain.enums.UserRule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {

    private Long idUsuario;
    private Long idAssociado;   // null se for ADM
    private String nome;
    private String email;
    private UserRule role;

    public RegisterResponse(Long idUsuario, Long idAssociado,
                            String nome, String email, UserRule role) {
        this.idUsuario   = idUsuario;
        this.idAssociado = idAssociado;
        this.nome        = nome;
        this.email       = email;
        this.role        = role;
    }
}
