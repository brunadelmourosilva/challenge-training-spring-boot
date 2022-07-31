package com.brunadelmouro.challengespring.service;

import org.springframework.scheduling.annotation.Scheduled;

public interface JobService {

    @Scheduled(fixedDelay = 10000)
    void saveSheetToArchiveInformation();
}