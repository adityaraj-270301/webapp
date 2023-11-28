package com.csye6225.assignment2.repository;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csye6225.assignment2.model.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
    
}
