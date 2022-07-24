package com.brunadelmouro.challengespring;

import com.brunadelmouro.challengespring.models.Curso;
import com.brunadelmouro.challengespring.models.Universidade;
import com.brunadelmouro.challengespring.repositories.CursoRepository;
import com.brunadelmouro.challengespring.repositories.UniversidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@SpringBootApplication
@EnableJpaAuditing
public class ChallengeWithSpringBootApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeWithSpringBootApplication.class, args);
	}

	@Autowired
	CursoRepository cursoRepository;

	@Autowired
	UniversidadeRepository universidadeRepository;

	@Override
	public void run(final String... args) throws Exception {

		Curso curso1 = new Curso(null, "Sistemas de Informação", "SIN");
		Curso curso2 = new Curso(null, "Engenharia da Computação", "ECO");
		Curso curso3 = new Curso(null, "Matemática Bacharelado", "MAT");

		//TODO relacionamento com as tabelas

		cursoRepository.saveAll(Arrays.asList(curso1, curso2, curso3));

		Universidade universidade1 = new Universidade(null, "Instituto Federal do Sul de Minas Gerais", "IFSULDEMINAS");
		Universidade universidade2 = new Universidade(null, "Universidade Federal de Itajubá", "UNIFEI");
		Universidade universidade3 = new Universidade(null, "Fundação de Ensino e Pesquisa de Itajubá", "FEPI");

		universidadeRepository.saveAll(Arrays.asList(universidade1, universidade2, universidade3));
	}
}
