package com.csye6225.assignment2.controllers;

import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csye6225.assignment2.model.Assignment;
import com.csye6225.assignment2.service.AssignmentService;
import com.csye6225.assignment2.service.UserAccountService;
import com.csye6225.assignment2.util.JWTUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.timgroup.statsd.StatsDClient;

@RestController
public class AssignmentController {

    private final Map<UUID, String> assignmentOwners = new ConcurrentHashMap<>();

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private StatsDClient statsDClient;

    private final static Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    @GetMapping("/v1/assignments")
    public ResponseEntity<List<Assignment>> getAllAssignments(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(required = false) Map<String, String> queryParams)
            throws SQLException, JsonProcessingException {               

        statsDClient.incrementCounter("get.assignmentRequest.count");
        logger.info("AssignmentController: Fetching Assignment data...");
        if (queryParams != null && !queryParams.isEmpty()) {
            logger.error("AssignmentController: QueryParameter Should be empty");
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();

        }
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String base64Credentials = authorizationHeader.substring("Basic ".length());
            // Decode base64Credentials and split username:password
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] parts = credentials.split(":", 2);
            String username = parts[0];
            String password = parts[1];
            if (userAccountService.authenticateUser(username, password)) {
                // Authentication successful, you can generate and return a JWT token here
                String token = jwtUtil.generateToken(username); // Implement token generation
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Basic " + token); // Add the token to the response headers
                logger.info("AssignmentController: Success!! Fetching Assignment data...");
                return ResponseEntity.ok().headers(headers).body(assignmentService.getAllAssignments());

            } else {
                logger.error("AssignmentController: Failure!! UNAUTHORIZED Access...");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            logger.error("AssignmentController: Failure!! UNAUTHORIZED Access...");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @GetMapping("/v1/assignments/{id}")
    public ResponseEntity<Assignment> getAssignment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable UUID id)
            throws SQLException, JsonProcessingException {

        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        statsDClient.incrementCounter("get.individualAssignmentRequest.count");
        logger.info("AssignmentController: Fetching Individual Assignment data...");
        if (isUserAuthenticated(authorizationHeader)) {

            String username = extractTokenFromHeader(authorizationHeader);

            // Validate and parse the token to get the username

            // Perform assignment creation logic

            Assignment createdAssignment = assignmentService.getAssignment(id);
            if (createdAssignment != null) {
                Assignment a = new Assignment();
                a.setId(createdAssignment.getId());
                a.setName(createdAssignment.getName());
                a.setAssignment_created(createdAssignment.getAssignment_created());
                a.setAssignment_updated(createdAssignment.getAssignment_updated());
                a.setDeadline(createdAssignment.getDeadline());
                a.setNum_of_attempts(createdAssignment.getNum_of_attempts());
                a.setPoints(createdAssignment.getPoints());

                return ResponseEntity.status(HttpStatus.OK).body(a);
            } else {
                logger.error("AssignmentController: Failure!! Bad Request...");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            logger.error("AssignmentController: Failure!! UNAUTHORIZED Access...");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @PostMapping("/v1/assignments")
    public ResponseEntity<Assignment> createAssignment(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody Assignment assignment) {
        statsDClient.incrementCounter("post.saveAssignmentRequest.count");
        logger.info("AssignmentController: Saving Assignment Data...");
        // Check if the user is authenticated
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            logger.error("AssignmentController: Failure!! UNAUTHORIZED Access...");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (isUserAuthenticated(authorizationHeader)) {

            String username = extractTokenFromHeader(authorizationHeader);

            // Validate and parse the token to get the username

            // Perform assignment creation logic
            if (assignment.getPoints() < 1 || assignment.getPoints() > 10) {
                logger.error("AssignmentController: Failure!! Points should be between 1 and 10...");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            assignment.setId(UUID.randomUUID());
            assignment.setAssignment_created(new Date());
            assignment.setAssignment_updated(new Date());
            Assignment createdAssignment = assignmentService.createAssignment(assignment);
            if (createdAssignment != null) {
                // System.out.println(createdAssignment.getId());
                assignmentOwners.put(createdAssignment.getId(), username);
                logger.info("AssignmentController: Success!!Assignment Created...");
                return ResponseEntity.status(HttpStatus.CREATED).body(createdAssignment);
            } else {
                logger.error("AssignmentController: Failure!! Bad Request...");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            logger.error("AssignmentController: Failure!! Unauthorized Access...");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/v1/assignments/{id}")
    public ResponseEntity<Assignment> updateAssignment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable UUID id,
            @RequestBody Assignment assignment) {
        statsDClient.incrementCounter("put.updateAssignmentRequest.count");
        logger.info("AssignmentController: Updating Assignment Data...");
        // Check if the user is authenticated
        if (isUserAuthenticated(authorizationHeader)) {

            String username = extractTokenFromHeader(authorizationHeader);
            /// System.out.println(id);
            // Validate and parse the token to get the username
            System.out.println(assignmentOwners.get(id));
            System.out.println(username);
            if (assignmentOwners.get(id) == null || !assignmentOwners.get(id).equals(username)) {
                logger.error("AssignmentController: Failure!! Forbidden User...");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Perform assignment creation logic
            Assignment a = assignmentService.getAssignment(id);

            a.setName(assignment.getName());
            a.setNum_of_attempts(assignment.getNum_of_attempts());
            a.setAssignment_updated(new Date());
            a.setPoints(assignment.getPoints());
            a.setDeadline(assignment.getDeadline());
            Assignment createdAssignment = assignmentService.updateAssignment(a);
            if (createdAssignment != null) {
                logger.info("AssignmentController: Success!! Assignment Updated...");
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                logger.error("AssignmentController: Failure!! Bad request...");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            logger.error("AssignmentController: Failure!! Unauthorised User...");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/v1/assignments/{id}")
    public ResponseEntity<Assignment> deleteAssignment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable UUID id) {
        statsDClient.incrementCounter("delete.deleteAssignmentRequest.count");
        logger.info("AssignmentController: Deleting Assignment Data...");
        // Check if the user is authenticated
        if (isUserAuthenticated(authorizationHeader)) {

            String username = extractTokenFromHeader(authorizationHeader);
            /// System.out.println(id);
            // Validate and parse the token to get the username

            if (assignmentOwners.get(id) == null || !assignmentOwners.get(id).equals(username)) {
                logger.error("AssignmentController: Failure!! Forbidden User...");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Perform assignment creation logic

            if (assignmentService.getAssignment(id) != null) {
                assignmentService.deleteAssignment(id);
                logger.info("AssignmentController: Success!! Assignment Deleted...");
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                logger.error("AssignmentController: Failure!! Bad request...");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            logger.error("AssignmentController: Failure!! Unauthorised User...");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private boolean isUserAuthenticated(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String token = extractToken(authorizationHeader);
            if (token != null && !jwtUtil.isTokenExpired(token)) {//
                // Token is valid; perform authorized actions
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String base64Credentials = authorizationHeader.substring("Basic ".length());
            // Decode base64Credentials and split username:password
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] parts = credentials.split(":", 2);
            String username = parts[0];
            String password = parts[1];
            // Assuming you have a method to validate the user
            if (userAccountService.authenticateUser(username, password)) {
                // Generate and return the JWT token
                return jwtUtil.generateToken(username);
            }
        }
        return null;
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String base64Credentials = authorizationHeader.substring("Basic ".length());
            // Decode base64Credentials and split username:password
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] parts = credentials.split(":", 2);
            String username = parts[0];

            // Assuming you have a method to validate the user
            if (username != null) {
                // Generate and return the JWT token
                return username;
            }
        }
        return null;
    }

    @PatchMapping(value = "/vi/assignments")
    public ResponseEntity<Void> doPatch() {
        statsDClient.incrementCounter("patch.patchAssignmentRequest.count");
        logger.error("AssignmentController: Patching Not Allowed...");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("cache-control", "no-cache, no-store, must-revalidate").build();
    }

}
