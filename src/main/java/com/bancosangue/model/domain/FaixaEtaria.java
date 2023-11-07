package com.bancosangue.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @Builder)
public class FaixaEtaria {
    private int idadeMinima;
    private int idadeMaxima;

    public String toString() {
        return idadeMinima + "-" + idadeMaxima;
    }
}
