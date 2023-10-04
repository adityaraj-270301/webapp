package com.csye6225.assignment2.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class RESTController {

    @GetMapping("/healthz")
    public ResponseEntity<Void> health_check(@RequestBody(required = false) String requestBody,
            @RequestParam(required = false) Map<String, String> queryParams)
            throws SQLException, JsonProcessingException {
        if ((queryParams != null && !queryParams.isEmpty()) || ((requestBody != null && !requestBody.isEmpty()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        } else {
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/csye6225_assignments",
                        "root",
                        "Pass1234");
                if (conn != null) {
                    System.out.println("Database connection successful.");

                    // Create a JSON object.

                    // json.put("status", "OK");

                    return ResponseEntity.ok().header("cache-control", "no-cache, no-store, must-revalidate").build();
                } else {

                    // json.put("status", "KO");

                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                            .header("cache-control", "no-cache, no-store, must-revalidate").build();
                }

            } catch (SQLException sqlException) {

                // Convert the JSON object to a string.
                // ObjectMapper mapper = new ObjectMapper();
                // String jsonResponse = mapper.writeValueAsString(json);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .header("cache-control", "no-cache, no-store, must-revalidate").build();
            }
        }
    }

    @PostMapping("/healthz")
    public ResponseEntity<Void> doPost() {

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("cache-control", "no-cache, no-store, must-revalidate").build();

    }

    @PutMapping(value = "/healthz")
    public ResponseEntity<Void> doPut() {

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("cache-control", "no-cache, no-store, must-revalidate").build();
    }

    @PatchMapping(value = "/healthz")
    public ResponseEntity<Void> doPatch() {

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("cache-control", "no-cache, no-store, must-revalidate").build();
    }

    @DeleteMapping(value = "/healthz")
    public ResponseEntity<Void> doDelete() {

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("cache-control", "no-cache, no-store, must-revalidate").build();
    }

}
