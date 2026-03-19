package com.br.robertmiler.gerenciamento.domain.enums;

public enum UserRule {
    ROLE_ADM("ROLE_ADM"),
    ROLE_ASSOCIADO("ROLE_ASSOCIADO");
    

    private final String role;

    private UserRule(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
    
    
}
