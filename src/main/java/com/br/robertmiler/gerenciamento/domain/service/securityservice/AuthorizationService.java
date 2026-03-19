package com.br.robertmiler.gerenciamento.domain.service.securityservice;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.br.robertmiler.gerenciamento.infrastructure.repositories.UsuarioRepository;



@Service
public class AuthorizationService implements UserDetailsService{


    @Autowired
    private UsuarioRepository usuarioRepository;

   
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        return usuarioRepository.findByEmail(email)
            // se não achar, tenta como Associado (email no Associado vinculado)
            .or(() -> usuarioRepository.findByAssociado_EmailPrincipal(email))
            .orElseThrow(() -> new UsernameNotFoundException(
                    "Usuário não encontrado: " + email));
     
    }

    
}
