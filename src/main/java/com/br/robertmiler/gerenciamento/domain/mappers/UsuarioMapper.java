package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.RegisterRequest;
import com.br.robertmiler.gerenciamento.domain.dtos.response.RegisterResponse;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.Usuario;
import com.br.robertmiler.gerenciamento.domain.enums.UserRule;

@Component
public class UsuarioMapper {

    @Autowired
    PasswordEncoder encoder;

     public Usuario toUsuarioAdm(RegisterRequest request) {
        Usuario adm = new Usuario();

        adm.setEmail(request.getEmail());
        adm.setRole(UserRule.ROLE_ADM);
        adm.setSenha(encoder.encode(request.getSenha()));
        adm.setAssociado(null); //nulo

        return adm;
     }

     

     public Usuario toUsuarioAssociado(RegisterRequest request, Associado associado) {
         Usuario usuario = new Usuario();
        usuario.setEmail(null);           // sem email aqui
        usuario.setSenha(encoder.encode(request.getSenha()));
        usuario.setRole(request.getRole());
        usuario.setAssociado(associado);  // ✅ email está AQUI dentro
        return usuario;
     }

     // ✅ Entity → Response (ADM)
    public RegisterResponse toResponseAdm(Usuario usuario, String nomeCompleto) {
        return new RegisterResponse(
                usuario.getId(),
                null,
                nomeCompleto,
                usuario.getEmail(),
                usuario.getRole()
        );
    }

    // ✅ Entity → Response (Associado)
    public RegisterResponse toResponseAssociado(Usuario usuario, Associado associado) {
        return new RegisterResponse(
                usuario.getId(),
                associado.getIdAssociado(),
                associado.getNomeCompleto(),
                associado.getEmailPrincipal(),
                usuario.getRole()
        );
    }
}
