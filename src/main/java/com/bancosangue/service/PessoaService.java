package com.bancosangue.service;

import com.bancosangue.exception.BancoSangueException;
import com.bancosangue.model.Pessoa;
import com.bancosangue.model.domain.FaixaEtaria;

import java.util.List;
import java.util.Map;

public interface PessoaService {
    void importDataFromJSON();

    public Map<String, Integer> listarPessoasPorEstado() throws BancoSangueException;

    public Map<FaixaEtaria, Double> calcularIMCMedioPorFaixaDeIdade() throws BancoSangueException;

    public double calcularPercentualDeObesos(String sexo) throws BancoSangueException;

    public Map<String, Integer> calcularMediaDeIdadePorTipoSanguineo() throws BancoSangueException;

    public Map<String, Map<String, Integer>> calcularQuantidadePossiveisDoadores() throws BancoSangueException;
}
