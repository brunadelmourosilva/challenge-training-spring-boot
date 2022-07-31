package com.brunadelmouro.challengespring.service;

import com.brunadelmouro.challengespring.Status;
import com.brunadelmouro.challengespring.models.Job;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BaseService {
    void importSheetToDatabase(MultipartFile multipartfile);
}
