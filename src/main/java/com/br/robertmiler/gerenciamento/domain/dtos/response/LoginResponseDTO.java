package com.br.robertmiler.gerenciamento.domain.dtos.response;

import com.br.robertmiler.gerenciamento.domain.enums.UserRule;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String nome;
    private String email;
    private UserRule role;
    private Long idAssociado; // null se for ADM

    
    public LoginResponseDTO(String token, String nome, String email, UserRule role, Long idAssociado) {
        this.token = token;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.idAssociado = idAssociado;
    }


    
}
