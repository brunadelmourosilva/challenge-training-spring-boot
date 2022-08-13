package com.brunadelmouro.challengespring.controllers;

import com.brunadelmouro.challengespring.ChallengeWithSpringBootApplication;
import com.brunadelmouro.challengespring.dto.AlunoResponseDTO;
import com.brunadelmouro.challengespring.dto.CursoResponseDTO;
import com.brunadelmouro.challengespring.dto.UniversidadeResponseDTO;
import com.brunadelmouro.challengespring.enums.Status;
import com.brunadelmouro.challengespring.mappers.AlunoMapper;
import com.brunadelmouro.challengespring.models.Aluno;
import com.brunadelmouro.challengespring.models.Job;
import com.brunadelmouro.challengespring.repositories.AlunoRepository;
import com.brunadelmouro.challengespring.repositories.CursoRepository;
import com.brunadelmouro.challengespring.repositories.JobRepository;
import com.brunadelmouro.challengespring.repositories.UniversidadeRepository;
import com.brunadelmouro.challengespring.service.impl.AlunoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ChallengeWithSpringBootApplication.class)
@ActiveProfiles(value = "test")
class AlunoControllerTest {

    private static final String CHALLENGE_API = "/alunos/";

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @Autowired AlunoController alunoController;
    @Autowired AlunoServiceImpl alunoService;
    @MockBean JobRepository jobRepository;
    @MockBean AlunoRepository alunoRepository;
    @MockBean CursoRepository cursoRepository;
    @MockBean UniversidadeRepository universidadeRepository;
    @MockBean AlunoMapper alunoMapper;

    Aluno aluno1;
    Aluno aluno2;
    Job job1;
    AlunoResponseDTO alunoResponseDTO;
    CursoResponseDTO cursoResponseDTO;
    UniversidadeResponseDTO universidadeResponseDTO;

    Page<Aluno> alunosFiltradosPage;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(alunoController).build();
        Date dataMatriculaDate = new Date("06/02/2020");

        aluno1 = new Aluno(1, "2021001809", dataMatriculaDate, "Bruna", 2.82, 4.89, 10.0);
        aluno2 = new Aluno(2, "2022001932", dataMatriculaDate, "Vitória", 2.82, 4.89, 10.0);

        cursoResponseDTO = new CursoResponseDTO(1, "Sistemas de Informação", "SIN");
        universidadeResponseDTO = new UniversidadeResponseDTO(1, "Universidade Federal de Itajubá", "UNIFEI");
        alunoResponseDTO = new AlunoResponseDTO("Bruna", 5.90, cursoResponseDTO, universidadeResponseDTO);

        alunosFiltradosPage = new PageImpl<>(List.of(aluno1), PageRequest.of(0, 2, Sort.Direction.DESC, "media"), 1);

        job1 = new Job(1, "testeExcelReader.xlsx", "desktop/brunadelmouro/", Status.AGUARDANDO_PROCESSAMENTO);

        //creating a new instance for the classes
        objectMapper = new ObjectMapper();
        alunoController = new AlunoController(alunoService, jobRepository, alunoMapper);
        alunoService = new AlunoServiceImpl(alunoRepository, cursoRepository, universidadeRepository, alunoMapper);
    }

    @Test
    void importTransactionsFromExcelToDbWithSuccessTest() throws Exception {
        //given
        given(jobRepository.save(job1)).willReturn(job1);

        MockMultipartFile file = new MockMultipartFile("file", "testeExcelReader.xlsx", "text/plain", "some xml".getBytes());
        //when-then
        mockMvc
                .perform(MockMvcRequestBuilders.multipart(CHALLENGE_API.concat("import-to-db"))
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Sheet imported!"));
    }

    @Test
    void getStudentByIdWithSuccessTest() throws Exception {
        //given
        given(alunoRepository.findById(aluno1.getId())).willReturn(Optional.of(aluno1));

        //when - then
        mockMvc
                .perform(MockMvcRequestBuilders.get(CHALLENGE_API.concat("1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(alunoResponseDTO)))
                .andDo(print())
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.nome").value("Bruna"));
    }

    @Test
    void getStudentsByFilterQueryWithSuccessTest() throws Exception {
        //given
        given(alunoRepository.findAllBy(anyInt(), anyInt(), any(Pageable.class))).willReturn(alunosFiltradosPage);
        //given(alunoRepository.findAll(any(Pageable.class))).willReturn(alunosFiltradosPage);

        //when - then
        mockMvc
                .perform(MockMvcRequestBuilders.get(CHALLENGE_API.concat("filter"))
                        .param("pageNo", String.valueOf(0))
                        .param("pageSize", String.valueOf(10))
                        .param("sortBy", "media")
                        .param("sortDir", "desc")
                        .param("cursoId", String.valueOf(1))
                        .param("universidadeId", String.valueOf(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(alunoResponseDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
                //.andExpect(jsonPath("$.content[0].nome", is("Bruna")));
    }
}