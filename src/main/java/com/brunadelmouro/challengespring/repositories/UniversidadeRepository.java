package com.brunadelmouro.challengespring.repositories;

import com.brunadelmouro.challengespring.models.Universidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversidadeRepository extends JpaRepository<Universidade, Integer> {
    Universidade findBySigla(String sigla);
}
