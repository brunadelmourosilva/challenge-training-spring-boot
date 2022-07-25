package com.brunadelmouro.challengespring.service;

import com.brunadelmouro.challengespring.models.Aluno;

import java.util.Optional;

public interface AlunoService extends BaseService {

    Aluno getStudentById(Integer id);
}
