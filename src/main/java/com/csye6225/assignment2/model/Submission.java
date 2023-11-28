package com.csye6225.assignment2.model;

import java.util.Date;
import java.util.UUID;
import jakarta.persistence.Entity;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Submission {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private UUID assignment_id;

    @Column(nullable = false)
    private String submission_url;

    @Column(nullable = false)
    private Date submission_date;

    @Column(nullable = false)
    private Date submission_updated;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(UUID assignment_id) {
        this.assignment_id = assignment_id;
    }

    public String getSubmission_url() {
        return submission_url;
    }

    public void setSubmission_url(String submission_url) {
        this.submission_url = submission_url;
    }

    public Date getSubmission_date() {
        return submission_date;
    }

    public void setSubmission_date(Date submission_date) {
        this.submission_date = submission_date;
    }

    public Date getSubmission_updated() {
        return submission_updated;
    }

    public void setSubmission_updated(Date submission_updated) {
        this.submission_updated = submission_updated;
    }

    

    
}
