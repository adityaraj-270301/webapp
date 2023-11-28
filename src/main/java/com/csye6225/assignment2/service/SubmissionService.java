package com.csye6225.assignment2.service;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csye6225.assignment2.model.Submission;
import com.csye6225.assignment2.repository.SubmissionRepository;


@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public Submission createSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }

    public Submission getSubmission(UUID uuid) {
        return submissionRepository.getReferenceById(uuid);
    }

    public Submission updateSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }

    public void deleteSubmission(UUID id) {
        submissionRepository.deleteById(id);
    }
}
