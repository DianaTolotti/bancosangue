package com.bancosangue.service;

import com.bancosangue.exception.BancoSangueException;
import com.bancosangue.model.Pessoa;
import com.bancosangue.model.domain.FaixaEtaria;
import com.bancosangue.repository.PessoaRepository;
import com.bancosangue.util.CustomEmailDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PessoaServiceImpl implements PessoaService {

    @Autowired
    PessoaRepository pessoaRepository;

    @Autowired
    ObjectMapper objectMapper;


    @Override
    public void importDataFromJSON() {
        try {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(String.class, new CustomEmailDeserializer());
            objectMapper.registerModule(module);
            ClassPathResource resource = new ClassPathResource("pessoa.json");
            List<Pessoa> pessoas = Arrays.asList(objectMapper.readValue(resource.getInputStream(), Pessoa[].class));
            pessoaRepository.saveAll(pessoas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> listarPessoasPorEstado() {
        List<Pessoa> pessoas = null;

        try {
            pessoas = pessoaRepository.findAll();
        } catch (Exception e) {
            throw new BancoSangueException("Erro ao buscar pessoas por estado. Detalhes:", e);
        }

        Map<String, Integer> candidatosPorEstado = new HashMap<>();

        for (Pessoa pessoa : pessoas) {
            String estado = pessoa.getEstado();
            candidatosPorEstado.put(estado, candidatosPorEstado.getOrDefault(estado, 0) + 1);
        }

        return candidatosPorEstado;
    }

    public Map<FaixaEtaria, Double> calcularIMCMedioPorFaixaDeIdade() {
        try {
            Map<FaixaEtaria, Double> imcMedioPorFaixa = new TreeMap<>((faixa1, faixa2) -> {
                int idadeMinima1 = faixa1.getIdadeMinima();
                int idadeMinima2 = faixa2.getIdadeMinima();
                return Integer.compare(idadeMinima1, idadeMinima2);
            });

            for (int idadeMinima = 0; idadeMinima < 100; idadeMinima += 10) {
                int idadeMaxima = idadeMinima + 10;
                FaixaEtaria faixa = new FaixaEtaria(idadeMinima+1, idadeMaxima);
                double imcTotal = 0.0;
                int count = 0;

                for (Pessoa pessoa : pessoaRepository.findAll()) {
                    Date dataNasc = pessoa.getDataNasc();
                    int idadePessoa = calcularIdade(dataNasc);

                    if (idadePessoa >= idadeMinima && idadePessoa < idadeMaxima) {
                        double peso = pessoa.getPeso();
                        double altura = pessoa.getAltura();
                        double imcPessoa = peso / (altura * altura);
                        imcTotal += imcPessoa;
                        count++;
                    }
                }

                double imcMedio = count > 0 ? imcTotal / count : 0.0;
                imcMedioPorFaixa.put(faixa, imcMedio);
            }
            return imcMedioPorFaixa;
        } catch (Exception e) {
            throw new BancoSangueException("Erro ao calcular IMC médio por faixa de idade. Detalhes:", e);
        }
    }

    public int calcularIdade(Date dataNasc) {
        Calendar dataNascimento = Calendar.getInstance();
        dataNascimento.setTime(dataNasc);
        Calendar dataAtual = Calendar.getInstance();

        int idade = dataAtual.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR);

        if (dataAtual.get(Calendar.DAY_OF_YEAR) < dataNascimento.get(Calendar.DAY_OF_YEAR)) {
            idade--;
        }

        return idade;
    }

    public double calcularPercentualDeObesos(String sexo) {
        int totalPessoas = 0;
        int obesos = 0;

        try {
            for (Pessoa pessoa : pessoaRepository.findAll()) {
                if (pessoa.getSexo().equalsIgnoreCase(sexo)) {
                    double imc = pessoa.getPeso() / (pessoa.getAltura() * pessoa.getAltura());
                    if (imc > 30) {
                        obesos++;
                    }
                    totalPessoas++;
                }
            }
        } catch (Exception e) {
            throw new BancoSangueException("Erro ao calcular percentual de obesos. Detalhes: ", e);
        }

        return totalPessoas > 0 ? (obesos * 100.0) / totalPessoas : 0.0;
    }

    public Map<String, Integer> calcularMediaDeIdadePorTipoSanguineo() {
        Map<String, Integer> mediaIdadePorTipoSanguineo = new HashMap<>();
        List<String> tiposSanguineos = Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        try {
            for (String tipo : tiposSanguineos) {
                int totalPessoas = 0;
                int idadeTotal = 0;
                for (Pessoa pessoa : pessoaRepository.findByTipoSanguineo(tipo)) {
                    int idade = calcularIdade(pessoa.getDataNasc());
                    idadeTotal += idade;
                    totalPessoas++;
                }
                int mediaIdade = totalPessoas > 0 ? idadeTotal / totalPessoas : 0;
                mediaIdadePorTipoSanguineo.put(tipo, mediaIdade);
            }
        } catch (Exception e) {
            throw new BancoSangueException("Erro ao calcular média de idade por tipo sanguíneo. Detalhes: ", e);
        }
        return mediaIdadePorTipoSanguineo;
    }

    public Map<String, Map<String, Integer>> calcularQuantidadePossiveisDoadores() {
        Map<String, Map<String, Integer>> quantidadeDoadoresPorTipoSanguineo = new HashMap<>();
        List<String> tiposSanguineos = Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");

        try {
            for (String receptor : tiposSanguineos) {
                Map<String, Integer> possiveisDoadores = new HashMap<>();
                for (String doador : tiposSanguineos) {
                    if (tipoSanguineoCompativel(doador, receptor)) {
                        int quantidadeDoadores = pessoaRepository.countPossiveisDoadores(doador);
                        possiveisDoadores.put("Pode receber de: " + doador + ". Quantidade de doadores ", quantidadeDoadores);
                    }
                }
                quantidadeDoadoresPorTipoSanguineo.put("Receptor " +receptor + ": ", possiveisDoadores);
            }
        } catch (Exception e) {
            throw new BancoSangueException("Erro ao calcular quantidade de possíveis doadores. Detalhes: ", e);
        }

        return quantidadeDoadoresPorTipoSanguineo;
    }

    // Verifica se o tipo sanguíneo do doador é compatível com o receptor
    private boolean tipoSanguineoCompativel(String doador, String receptor) {
        switch (receptor) {
            case "A+":
                return doador.equals("A+") || doador.equals("A-") || doador.equals("O+") || doador.equals("O-");
            case "A-":
                return doador.equals("A+") || doador.equals("A-") || doador.equals("AB+") || doador.equals("AB-");
            case "B+":
                return doador.equals("B+") || doador.equals("AB+");
            case "B-":
                return doador.equals("B+") || doador.equals("B-") || doador.equals("AB+") || doador.equals("AB-");
            case "AB+":
                return doador.equals("AB+");
            case "AB-":
                return doador.equals("AB+") || doador.equals("AB-");
            case "O+":
                return doador.equals("A+") || doador.equals("B+") || doador.equals("O+") || doador.equals("AB+");
            case "O-":
                return doador.equals("A+") || doador.equals("B+") || doador.equals("O+") || doador.equals("AB+") ||
                        doador.equals("A-") || doador.equals("B-") || doador.equals("O-") || doador.equals("AB-");
            default:
                return false; // Tipo sanguíneo não reconhecido
        }
    }
}