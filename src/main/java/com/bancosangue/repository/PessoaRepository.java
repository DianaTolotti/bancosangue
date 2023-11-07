package com.bancosangue.repository;

import com.bancosangue.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

    List<Pessoa> findByTipoSanguineo(String tipoSanguineo);


    @Query("SELECT COUNT(p) FROM Pessoa p " +
            "WHERE p.tipoSanguineo = :receptor " +
            "AND DATEDIFF(CURRENT_DATE(), p.dataNasc) >= 16*365 " + // Maior ou igual a 16 anos em dias
            "AND DATEDIFF(CURRENT_DATE(), p.dataNasc) <= 69*365 " + // Menor ou igual a 69 anos em dias
            "AND p.peso > 50")
    int countPossiveisDoadores(@Param("receptor") String receptor);
}