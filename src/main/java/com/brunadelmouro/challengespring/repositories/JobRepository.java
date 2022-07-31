package com.brunadelmouro.challengespring.repositories;

import com.brunadelmouro.challengespring.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
}
