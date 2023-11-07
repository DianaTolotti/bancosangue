package com.bancosangue.controllers;

import com.bancosangue.enums.SexoCadastro;
import com.bancosangue.exception.BancoSangueException;
import com.bancosangue.model.domain.FaixaEtaria;
import com.bancosangue.service.PessoaService;
import com.bancosangue.util.ErroResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/portal")
@RequiredArgsConstructor
@Slf4j
class BancoSangueController {
    private final PessoaService pessoaService;

    @GetMapping(value = "/quantidadePorEstado", produces = "application/json")
    public ResponseEntity<Map<String, Integer>> quantidadePorEstado() {
        try {
            Map<String, Integer> pessoas = pessoaService.listarPessoasPorEstado();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            return ResponseEntity.ok(pessoas);
        } catch (BancoSangueException e) {
            ErroResponse erroResponse = new ErroResponse("Erro ao buscar pessoas por estado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((Map<String, Integer>) erroResponse);
        }
    }

    @GetMapping(value = "/IMCMedioPorFaixaDeIdade", produces = "application/json")
    public ResponseEntity<Map<FaixaEtaria, Double>> IMCMedioPorFaixaDeIdade() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            Map<FaixaEtaria, Double> imcMedio = pessoaService.calcularIMCMedioPorFaixaDeIdade();
            return ResponseEntity.ok(imcMedio);
        } catch (BancoSangueException e) {
            ErroResponse erroResponse = new ErroResponse("Erro ao calcular IMC médio por faixa de idade: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((Map<FaixaEtaria, Double>) erroResponse);
        }
    }


    @GetMapping(value = "/percentualObesos", produces = "application/json")
    public ResponseEntity<?> percentualObesos() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        try {
            double percentualMasculino = pessoaService.calcularPercentualDeObesos(SexoCadastro.MASCULINO.getDescricao());
            double percentualFeminino = pessoaService.calcularPercentualDeObesos(SexoCadastro.FEMININO.getDescricao());

            DecimalFormat df = new DecimalFormat("0.00");
            String percentualMasculinoFormatted = df.format(percentualMasculino) + "%";
            String percentualFemininoFormatted = df.format(percentualFeminino) + "%";

            Map<String, String> percentuais = new HashMap<>();
            percentuais.put("Masculino", percentualMasculinoFormatted);
            percentuais.put("Feminino", percentualFemininoFormatted);

            return ResponseEntity.ok(percentuais);
        } catch (BancoSangueException e) {
            ErroResponse erroResponse = new ErroResponse("Erro ao calcular percentual de obesos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erroResponse);
        }
    }

    @GetMapping(value = "/mediaIdadePorTipoSanguineo", produces = "application/json")
    public ResponseEntity<Map<String, Integer>> mediaIdadePorTipoSanguineo() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        try {
            Map<String, Integer> mediaIdade = pessoaService.calcularMediaDeIdadePorTipoSanguineo();
            return ResponseEntity.ok(mediaIdade);
        } catch (BancoSangueException e) {
            ErroResponse erroResponse = new ErroResponse("Erro ao calcular média de idade por tipo sanguíneo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((Map<String, Integer>) erroResponse);
        }
    }

    @GetMapping(value = "/calcularQuantidadePossiveisDoadores", produces = "application/json")
    public ResponseEntity<?> calcularQuantidadePossiveisDoadores() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        try {
            Map<String, Map<String, Integer>> resultado = pessoaService.calcularQuantidadePossiveisDoadores();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            ErroResponse erroResponse = new ErroResponse("Erro ao calcular quantidade de possíveis doadores: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erroResponse);
        }
    }

}