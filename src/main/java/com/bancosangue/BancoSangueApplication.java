package com.bancosangue;

import com.bancosangue.model.Pessoa;
import com.bancosangue.repository.PessoaRepository;
import com.bancosangue.service.PessoaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EntityScan(basePackages = "com.bancosangue.model")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class BancoSangueApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BancoSangueApplication.class, args);

		PessoaRepository pessoaRepository = context.getBean(PessoaRepository.class);
		Page<Pessoa> pessoas = pessoaRepository.findAll(PageRequest.of(0, 1));

		if (pessoas.isEmpty()) {
			PessoaService pessoaService = context.getBean(PessoaService.class);
			pessoaService.importDataFromJSON();
		} else {
			System.out.println("Registros já existem na tabela Pessoa. Não é necessário importar.");
		}
	}
}
