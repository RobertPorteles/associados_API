package com.br.robertmiler.gerenciamento.domain.helpers;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FormataString {

    public static String primeiraLetraMaiuscula(String nome) {
        return Arrays.stream(nome.trim().toLowerCase().split("\\s+"))
                .map(palavra -> palavra.substring(0, 1).toUpperCase() + palavra.substring(1))
                .collect(Collectors.joining(" "));
    }

}
