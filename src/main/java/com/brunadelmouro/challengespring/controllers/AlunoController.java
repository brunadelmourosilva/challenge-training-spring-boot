package com.brunadelmouro.challengespring.controllers;

import com.brunadelmouro.challengespring.dto.AlunoResponseDTO;
import com.brunadelmouro.challengespring.dto.PageResponse;
import com.brunadelmouro.challengespring.helpers.Constants;
import com.brunadelmouro.challengespring.mappers.AlunoMapper;
import com.brunadelmouro.challengespring.models.Aluno;
import com.brunadelmouro.challengespring.repositories.AlunoRepository;
import com.brunadelmouro.challengespring.service.AlunoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/alunos")
@Slf4j
public class AlunoController {

    private final AlunoService alunoService;

    private final AlunoMapper alunoMapper;

    private AlunoRepository alunoRepository;

    public AlunoController(final AlunoService alunoService, final AlunoMapper alunoMapper, final AlunoRepository alunoRepository) {
        this.alunoService = alunoService;
        this.alunoMapper = alunoMapper;
        this.alunoRepository = alunoRepository;
    }

    @PostMapping(path = "/import-to-db")
    public void importTransactionsFromExcelToDb(@RequestPart(required = true) List<MultipartFile> files) {
        alunoService.importSheetToDatabase(files);
        log.info("/alunos/import-to-db -> sheet imported");
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AlunoResponseDTO> getStudentById(@PathVariable(value = "id") Integer id){
        Aluno aluno = alunoService.getStudentById(id);

        log.info("/alunos/{}", id);
        return ResponseEntity.ok().body(alunoMapper.domainToResponseDTO(aluno));
    }

    @GetMapping
    public PageResponse getAllStudents(
            @RequestParam(value = "pageNo", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(value = "cursoId", required = false) Integer cursoId,
            @RequestParam(value = "universidadeId", required = false) Integer universidadeId
    ){
        log.info("/alunos -> {}, {}, {}, {}", pageNo, pageSize, sortBy, sortDir);


        return alunoService.getAllStudents(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping(value = "/filter")
    public Page<AlunoResponseDTO> getStudentsByFilter(
            @RequestParam(value = "pageNo", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(value = "cursoId", required = false) Integer cursoId,
            @RequestParam(value = "universidadeId", required = false) Integer universidadeId
    ){
        log.info("/alunos -> {}, {}, {}, {}", pageNo, pageSize, sortBy, sortDir);

        return alunoService.getStudentsByCursoAndUniversidadeFilter(cursoId, universidadeId, pageNo, pageSize, sortBy, sortDir);
    }
}



//procura universidade pelo id
//procura curso pelo id