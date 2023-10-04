package com.csye6225.assignment2.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csye6225.assignment2.model.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

}
