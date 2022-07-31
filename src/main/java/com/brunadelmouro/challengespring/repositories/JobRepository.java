package com.brunadelmouro.challengespring.repositories;

import com.brunadelmouro.challengespring.enums.Status;
import com.brunadelmouro.challengespring.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    List<Job> findByStatus(Status status);
}
