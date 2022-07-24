package com.brunadelmouro.challengespring.repositories;

import com.brunadelmouro.challengespring.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {

    Curso findBySigla(String sigla);
}