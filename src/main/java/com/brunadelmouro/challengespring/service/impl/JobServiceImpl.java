package com.brunadelmouro.challengespring.service.impl;

import com.brunadelmouro.challengespring.models.Job;
import com.brunadelmouro.challengespring.repositories.JobRepository;
import com.brunadelmouro.challengespring.service.JobService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    JobRepository jobRepository;

    public JobServiceImpl(final JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Scheduled(fixedDelay = 10000)
    @Override
    public void saveSheetToArchiveInformation() {
    }
}
