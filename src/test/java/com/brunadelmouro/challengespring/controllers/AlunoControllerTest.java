package com.brunadelmouro.challengespring.controllers;

import com.brunadelmouro.challengespring.ChallengeWithSpringBootApplication;
import com.brunadelmouro.challengespring.enums.Status;
import com.brunadelmouro.challengespring.mappers.AlunoMapper;
import com.brunadelmouro.challengespring.mappers.CursoMapper;
import com.brunadelmouro.challengespring.mappers.UniversidadeMapper;
import com.brunadelmouro.challengespring.models.Aluno;
import com.brunadelmouro.challengespring.models.Curso;
import com.brunadelmouro.challengespring.models.Job;
import com.brunadelmouro.challengespring.models.Universidade;
import com.brunadelmouro.challengespring.repositories.AlunoRepository;
import com.brunadelmouro.challengespring.repositories.CursoRepository;
import com.brunadelmouro.challengespring.repositories.JobRepository;
import com.brunadelmouro.challengespring.repositories.UniversidadeRepository;
import com.brunadelmouro.challengespring.service.AlunoService;
import com.brunadelmouro.challengespring.service.impl.AlunoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ChallengeWithSpringBootApplication.class)
class AlunoControllerTest {

    private final String CHALLENGE_API = "/alunos/";

    @Autowired
    AlunoController alunoController;

    AlunoService alunoService;

    @MockBean
    JobRepository jobRepository;

    @MockBean
    AlunoRepository alunoRepository;
    @MockBean
    CursoRepository cursoRepository;
    @MockBean
    UniversidadeRepository universidadeRepository;
    AlunoMapper alunoMapper;
    MockMvc mockMvc;
    ModelMapper modelMapper;
    CursoMapper cursoMapper;
    UniversidadeMapper universidadeMapper;
    Aluno aluno;
    Curso curso;
    Job job;
    Universidade universidade;
    Page<Aluno> alunosFiltradosPage;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        cursoMapper = new CursoMapper(modelMapper);
        universidadeMapper = new UniversidadeMapper(modelMapper);
        alunoMapper = new AlunoMapper(modelMapper, cursoMapper, universidadeMapper);
        alunoService = new AlunoServiceImpl(alunoRepository, cursoRepository, universidadeRepository, alunoMapper);
        alunoController = new AlunoController(alunoService, jobRepository, alunoMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(alunoController).build();

        aluno = new Aluno(1, "209329299", Date.from(Instant.now()), "Alex", 8.7, 10.0, 3.0);
        curso = new Curso(1, "Sistemas de Info", "SIN");
        universidade = new Universidade(1, "Federal de Itajubá", "UNIFEI");

        aluno.setCurso(curso);
        aluno.setUniversidade(universidade);

        alunosFiltradosPage = new PageImpl<>(List.of(aluno), PageRequest.of(0, 1, Sort.Direction.DESC, "media"), 1);

        job = new Job(1, "testeExcelReader.xlsx", "desktop/brunadelmouro/", Status.AGUARDANDO_PROCESSAMENTO);
    }

    @Test
    void importTransactionsFromExcelToDbWithSuccessTest() throws Exception {
        //given
        given(jobRepository.save(job)).willReturn(job);

        MockMultipartFile file = new MockMultipartFile("file", "testeExcelReader.xlsx", "text/plain", "some xml".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart(CHALLENGE_API.concat("import-to-db"))
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getStudentByIdWithSuccessTest() throws Exception {
        //given
        given(alunoRepository.findById(aluno.getId())).willReturn(Optional.of(aluno));

        //when - then
        mockMvc.perform(MockMvcRequestBuilders.get(CHALLENGE_API.concat("1"))
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nome").value("Alex"));
    }

    @Test
    void getStudentsByFilterWhenCourseOrUniversityIsDifferentToNullWithSuccess() throws Exception {
        //given
        given(alunoRepository.findAllBy(anyInt(), anyInt(), any(Pageable.class))).willReturn(alunosFiltradosPage);

        //when - then
        mockMvc
                .perform(MockMvcRequestBuilders.get(CHALLENGE_API.concat("filter"))
                        .param("pageNo", String.valueOf(0))
                        .param("pageSize", String.valueOf(1))
                        .param("sortBy", "media")
                        .param("sortDir", "desc")
                        .param("cursoId", String.valueOf(1))
                        .param("universidadeId", String.valueOf(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }
}