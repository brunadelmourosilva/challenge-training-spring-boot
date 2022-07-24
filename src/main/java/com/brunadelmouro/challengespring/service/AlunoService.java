package com.brunadelmouro.challengespring.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AlunoService {

    //TODO replace to BaseService
    void importSheetToDatabase(List<MultipartFile> multipartfiles);
}
