package com.brunadelmouro.challengespring.controllers;

import com.brunadelmouro.challengespring.service.AlunoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/transactions")
public class AlunoController {

    private AlunoService alunoService;

    public AlunoController(final AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping(path = "/import-to-db")
    public void importTransactionsFromExcelToDb(@RequestPart(required = true) List<MultipartFile> files) {

        alunoService.importSheetToDatabase(files);

    }
}