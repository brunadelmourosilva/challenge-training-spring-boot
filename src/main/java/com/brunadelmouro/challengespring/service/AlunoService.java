package com.brunadelmouro.challengespring.service;

import com.brunadelmouro.challengespring.dto.AlunoResponseDTO;
import com.brunadelmouro.challengespring.models.Aluno;
import org.springframework.data.domain.Page;

public interface AlunoService extends BaseService {

    Aluno getStudentById(Integer id);
    Page<AlunoResponseDTO> getStudentsByCursoAndUniversidadeFilter(Integer cursoId, Integer universidadeId, int pageNo, int pageSize, String sortBy, String sortDir);
}
