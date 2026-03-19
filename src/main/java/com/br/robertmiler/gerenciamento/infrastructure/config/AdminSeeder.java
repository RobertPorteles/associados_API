package com.br.robertmiler.gerenciamento.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.entities.Usuario;
import com.br.robertmiler.gerenciamento.domain.enums.UserRule;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.UsuarioRepository;

@Component
public class AdminSeeder implements CommandLineRunner{


     @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //aqui faço para pegar no properties, Quando for para produção, define as variáveis de ambiente no servidor:
  
    
    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.senha}")
    private String adminSenha;

    @Override
    public void run(String... args) throws Exception {

        // só cria se não existir nenhum ADM
        boolean admJaExiste = usuarioRepository.existsByRole(UserRule.ROLE_ADM);
        if (admJaExiste) return;

        Usuario adm = new Usuario();
        adm.setEmail(adminEmail);
        adm.setSenha(passwordEncoder.encode(adminSenha));
        adm.setRole(UserRule.ROLE_ADM);
        adm.setAssociado(null);
        usuarioRepository.save(adm);

        System.out.println("✅ ADM padrão criado: " + adminEmail);
    }

    }




