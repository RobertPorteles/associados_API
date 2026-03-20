package com.br.robertmiler.gerenciamento.domain.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.br.robertmiler.gerenciamento.domain.enums.UserRule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity                          // ✅ faltava
@Table(name = "usuarios")        // ✅ faltava
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRule role;

    

    // implementações corretas — sem exception
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRole()));
    }

     // usado APENAS para ROLE_ADM — null para ROLE_ASSOCIADO
    @Column(unique = true, nullable = true)  // ← não basta
    private String email;

    // usado APENAS para ROLE_ASSOCIADO — null para ROLE_ADM
    @OneToOne
    @JoinColumn(name = "id_associado", nullable = true)
    private Associado associado;

    @Override
    public String getPassword() {
        return senha;
    }
    //UserName será o email de usuario!
    @Override
    public String getUsername() {
        return email;
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}