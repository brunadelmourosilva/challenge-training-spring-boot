package com.brunadelmouro.challengespring.service;

import org.springframework.scheduling.annotation.Scheduled;

public interface JobService {

    @Scheduled(fixedDelay = 10000)
    void saveSheetToArchiveInformation();
}


//TODO import sheet return true if get sheet with success(Optional) - call 10 seconds and verify if this function return true(aguardando proc.) each send of a new sheet
//TODO call this method on AlunoServiceImpl and pass XSSFSheet as a parameter - NO
//TODO in JobService, create whole logic to save the information of sheet, such as: Job class attributes
//TODO It Iis valid inform that the method should run every 10 seconds with cron expression, for example