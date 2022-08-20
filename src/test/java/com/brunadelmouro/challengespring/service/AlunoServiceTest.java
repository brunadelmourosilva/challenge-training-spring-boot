package com.brunadelmouro.challengespring.service;

import com.brunadelmouro.challengespring.models.Aluno;
import com.brunadelmouro.challengespring.repositories.AlunoRepository;
import com.brunadelmouro.challengespring.service.impl.AlunoServiceImpl;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AlunoServiceTest {

    private static final String MATRICULA = "2021001809";

    @InjectMocks
    private AlunoServiceImpl alunoService;

    @Mock
    private AlunoRepository alunoRepository;

    private Aluno aluno1;
    private Page<Aluno> alunosFiltradosPage;

    @BeforeEach
    void setUp() {
        Date dataMatriculaDate = new Date("06/02/2020");
        aluno1 = new Aluno(1, "2021001809", dataMatriculaDate, "Bruna", 2.82, 4.89, 10.0);

        alunosFiltradosPage = new PageImpl<>(List.of(aluno1), PageRequest.of(0, 1, Sort.Direction.DESC, "media"), 1);
    }

    @Test
    void importSheetToDatabase() {
    }

    @Test
    void getStudentByIdWhenIdWasFoundTest() {
        //given
        given(alunoRepository.findById(anyInt())).willReturn(Optional.of(aluno1));

        //when
        Aluno response = alunoService.getStudentById(anyInt());

        //then
        then(alunoRepository).should().findById(anyInt());

        //asserts
        assertEquals(MATRICULA, response.getMatricula());
    }

    @Test
    void getStudentByIdWhenIdWasNotFoundTest() {
        //given
        given(alunoRepository.findById(anyInt())).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class, () ->
                alunoService.getStudentById(0));

        //then
        then(alunoRepository).should().findById(anyInt());
    }
}