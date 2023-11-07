package com.bancosangue.model;

import com.bancosangue.util.CustomEmailDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @Builder)
@Entity
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "rg")
    private String rg;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "data_nasc")
    @JsonProperty("data_nasc")
    private Date dataNasc;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "mae")
    private String mae;

    @Column(name = "pai")
    private String pai;

    @JsonDeserialize(using = CustomEmailDeserializer.class)
    @Column(name = "email")
    private String email;

    @Column(name = "cep")
    private String cep;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "telefone_fixo")
    @JsonProperty("telefone_fixo")
    private String telefoneFixo;

    @Column(name = "celular")
    private String celular;

    @Column(name = "altura")
    private Double altura;

    @Column(name = "peso")
    private Integer peso;

    @Column(name = "tipo_sanguineo")
    @JsonProperty("tipo_sanguineo")
    private String tipoSanguineo;
}

