package com.brunadelmouro.challengespring.repositories;

import com.brunadelmouro.challengespring.models.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Integer> {
    //using JPQL
    @Query("FROM Aluno a WHERE (a.curso.id = :cursoId) AND (a.universidade.id = :universidadeId) OR" +
            "(:cursoId = NULL) AND (a.universidade.id = :universidadeId) OR" +
            "(:universidadeId = NULL) AND (a.curso.id = :cursoId)")
    Page<Aluno> findAllBy(@Param("cursoId") Integer cursoId, @Param("universidadeId") Integer universidadeId, Pageable pageable);
}
