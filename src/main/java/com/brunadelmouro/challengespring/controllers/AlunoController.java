package com.brunadelmouro.challengespring.controllers;

import com.brunadelmouro.challengespring.dto.AlunoResponseDTO;
import com.brunadelmouro.challengespring.mappers.AlunoMapper;
import com.brunadelmouro.challengespring.models.Aluno;
import com.brunadelmouro.challengespring.service.AlunoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    private final AlunoMapper alunoMapper;

    public AlunoController(final AlunoService alunoService, AlunoMapper alunoMapper) {
        this.alunoService = alunoService;
        this.alunoMapper = alunoMapper;
    }

    @PostMapping(path = "/import-to-db")
    public void importTransactionsFromExcelToDb(@RequestPart(required = true) List<MultipartFile> files) {

        alunoService.importSheetToDatabase(files);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AlunoResponseDTO> getStudentById(@PathVariable(value = "id") Integer id){
        Aluno aluno = alunoService.getStudentById(id);

        return ResponseEntity.ok().body(alunoMapper.domainToResponseDTO(aluno));
    }
}