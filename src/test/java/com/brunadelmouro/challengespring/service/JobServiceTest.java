package com.brunadelmouro.challengespring.service;

import com.brunadelmouro.challengespring.enums.Status;
import com.brunadelmouro.challengespring.mappers.AlunoMapper;
import com.brunadelmouro.challengespring.models.Aluno;
import com.brunadelmouro.challengespring.models.Curso;
import com.brunadelmouro.challengespring.models.Job;
import com.brunadelmouro.challengespring.models.Universidade;
import com.brunadelmouro.challengespring.repositories.AlunoRepository;
import com.brunadelmouro.challengespring.repositories.CursoRepository;
import com.brunadelmouro.challengespring.repositories.JobRepository;
import com.brunadelmouro.challengespring.repositories.UniversidadeRepository;
import com.brunadelmouro.challengespring.service.impl.AlunoServiceImpl;
import com.brunadelmouro.challengespring.service.impl.JobServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
class JobServiceTest {

    private static final String SIGLA_CURSO = "SIN";
    private static final String SIGLA_UNIVERSIDADE = "UNIFEI";
    @InjectMocks private JobServiceImpl jobService;

    @Autowired private AlunoServiceImpl alunoService;

    @Mock private AlunoRepository alunoRepository;

    @Mock private CursoRepository cursoRepository;
    @Mock private UniversidadeRepository universidadeRepository;
    @Mock private JobRepository jobRepository;
    @Mock private AlunoMapper alunoMapper;

    private Curso curso1;
    private Universidade universidade1;
    private Aluno aluno1;
    private Job job1;
    private List<Job> jobs;

    @BeforeEach
    void setUp() {
        job1 = new Job(1, "mockitoTeste.xlsx", "dummy/bruna", Status.AGUARDANDO_PROCESSAMENTO);
        jobs = List.of(job1);

        Date dataMatriculaDate = new Date("06/02/2020");

        curso1 = new Curso(1, "Sistemas de Informação", SIGLA_CURSO);

        universidade1 = new Universidade(1, "Universidade Federal de Itajubá", SIGLA_UNIVERSIDADE);

        aluno1 = new Aluno(1, "2021001809", dataMatriculaDate, "Bruna", 2.82, 4.89, 10.0);

        alunoService = new AlunoServiceImpl(alunoRepository, cursoRepository, universidadeRepository, alunoMapper);
        jobService = new JobServiceImpl(jobRepository, alunoService);
    }

    @Test
    void saveSheetToArchiveInformationWithSuccessTest() {
        //given - given a mocked value
        given(jobRepository.findByStatus(Status.AGUARDANDO_PROCESSAMENTO)).willReturn(jobs);

        given(cursoRepository.findBySigla(SIGLA_CURSO)).willReturn(curso1);
        given(universidadeRepository.findBySigla(SIGLA_UNIVERSIDADE)).willReturn(universidade1);
        given(alunoRepository.save(aluno1)).willReturn(aluno1);
        given(cursoRepository.save(curso1)).willReturn(curso1);
        given(universidadeRepository.save(universidade1)).willReturn(universidade1);

        given(jobRepository.save(job1)).willReturn(job1);

        //when - when the return of this function
        jobService.saveSheetToArchiveInformation();

        //then - then the result should be equals
    }
}
//https://www.baeldung.com/bdd-mockito