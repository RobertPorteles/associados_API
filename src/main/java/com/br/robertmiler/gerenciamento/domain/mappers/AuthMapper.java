package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.response.LoginResponseDTO;
import com.br.robertmiler.gerenciamento.domain.entities.Usuario;

@Component
public class AuthMapper {


     public LoginResponseDTO toLoginResponse(String token, Usuario usuario) {

        // nome: ADM não tem Associado — usa o email como identificador
        String nome = usuario.getAssociado() != null
                ? usuario.getAssociado().getNomeCompleto()
                : usuario.getEmail();

        // email: ADM usa o próprio, Associado usa o do vínculo
        String email = usuario.getUsername();

        // idAssociado: null para ADM
        Long idAssociado = usuario.getAssociado() != null
                ? usuario.getAssociado().getIdAssociado()
                : null;

        return new LoginResponseDTO(token, nome, email,
                usuario.getRole(), idAssociado);
    }
}
