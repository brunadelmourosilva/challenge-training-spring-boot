package com.brunadelmouro.challengespring.service;

import com.brunadelmouro.challengespring.models.Job;

public interface BaseService {
    void importSheetToDatabase(Job job);
}
