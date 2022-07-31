package com.brunadelmouro.challengespring.controllers;

import com.brunadelmouro.challengespring.Status;
import com.brunadelmouro.challengespring.dto.AlunoResponseDTO;
import com.brunadelmouro.challengespring.helpers.Constants;
import com.brunadelmouro.challengespring.mappers.AlunoMapper;
import com.brunadelmouro.challengespring.models.Aluno;
import com.brunadelmouro.challengespring.models.Job;
import com.brunadelmouro.challengespring.repositories.JobRepository;
import com.brunadelmouro.challengespring.service.AlunoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/alunos")
@Slf4j
public class AlunoController {

    private static final String URL_ARQUIVO = "/home/bdelmouro/IdeaProjects/My Projects/challenge-training-spring-boot/";
    private final AlunoService alunoService;
    private final JobRepository jobRepository;
    private final AlunoMapper alunoMapper;

    public AlunoController(final AlunoService alunoService, final JobRepository jobRepository, final AlunoMapper alunoMapper) {
        this.alunoService = alunoService;
        this.jobRepository = jobRepository;
        this.alunoMapper = alunoMapper;
    }

    @PostMapping(path = "/import-to-db")
    public ResponseEntity<String> importTransactionsFromExcelToDb(@RequestPart(required = true) MultipartFile file) {

        //initial process
        final var job = new Job(null,
                file.getOriginalFilename(),
                URL_ARQUIVO,
                Status.AGUARDANDO_PROCESSAMENTO);

        jobRepository.save(job);

        log.info("/alunos/import-to-db -> sheet imported");

        return ResponseEntity.ok().body("Sheet imported!");
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AlunoResponseDTO> getStudentById(@PathVariable(value = "id") Integer id) {
        Aluno aluno = alunoService.getStudentById(id);

        log.info("/alunos/{}", id);

        return ResponseEntity.ok().body(alunoMapper.domainToResponseDTO(aluno));
    }

    @GetMapping(value = "/filter")
    public Page<AlunoResponseDTO> getStudentsByFilter(
            @RequestParam(value = "pageNo", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(value = "cursoId", required = false) Integer cursoId,
            @RequestParam(value = "universidadeId", required = false) Integer universidadeId
    ) {
        log.info("/alunos -> {}, {}, {}, {}, {}, {}", pageNo, pageSize, sortBy, sortDir, cursoId, universidadeId);

        return alunoService.getStudentsByCursoAndUniversidadeFilter(cursoId, universidadeId, pageNo, pageSize, sortBy, sortDir);
    }
}