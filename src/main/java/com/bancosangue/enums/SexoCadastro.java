package com.bancosangue.enums;

public enum SexoCadastro {
    FEMININO("Feminino"),
    MASCULINO("Masculino");

    String descricao;

    SexoCadastro(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
