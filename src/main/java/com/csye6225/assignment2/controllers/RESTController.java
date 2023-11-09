package com.csye6225.assignment2.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.timgroup.statsd.StatsDClient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class RESTController {

    @Autowired
    private StatsDClient statsDClient;

    private final static Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    @GetMapping("/healthz")
    public ResponseEntity<Void> health_check(@RequestBody(required = false) String requestBody,
            @RequestParam(required = false) Map<String, String> queryParams)
            throws SQLException, JsonProcessingException {
        statsDClient.incrementCounter("get.healthz.count");
        logger.info("RESTController: Checking database health...");
        var database_ip = System.getenv("DATABASE_IP")  ;    
        var database_name = System.getenv("DATABASE_NAME")  ; 
        var database_user =  System.getenv("DATABASE_USER")  ; 
        var database_password =  System.getenv("DATABASE_PASSWORD")  ; 
        if ((queryParams != null && !queryParams.isEmpty()) || ((requestBody != null && !requestBody.isEmpty()))) {
            logger.error("RESTController: QueryParameter should be empty, and request Bosy should be empty too...");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        } else {
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://" + database_ip + ":3306/" + database_name,
                        database_user,
                        database_password);
                if (conn != null) {
                    System.out.println("Database connection successful.");

                    // Create a JSON object.

                    // json.put("status", "OK");
                    logger.error("AssignmentController: Success!! Database connected....");
                    return ResponseEntity.ok().header("cache-control", "no-cache, no-store, must-revalidate").build();
                } else {

                    // json.put("status", "KO");
                    logger.error("AssignmentController: Failure!! Database not connected....jdbc:mysql://{DATABASE_IP}:3306/${DATABASE_NAME}");


                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                            .header("cache-control", "no-cache, no-store, must-revalidate").build();
                }

            } catch (SQLException sqlException) {

                // Convert the JSON object to a string.
                // ObjectMapper mapper = new ObjectMapper();
                // String jsonResponse = mapper.writeValueAsString(json);
                logger.error("AssignmentController: Failure!! Database not connected....jdbc:mysql://${DATABASE_IP}:3306/${DATABASE_NAME}");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .header("cache-control", "no-cache, no-store, must-revalidate").build();
            }
        }
    }

    @PostMapping("/healthz")
    public ResponseEntity<Void> doPost() {
        statsDClient.incrementCounter("post.healthz.count");
        logger.error("RESTController: Failure!!PostMapping Not allowed...");  
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("cache-control", "no-cache, no-store, must-revalidate").build();

    }

    @PutMapping(value = "/healthz")
    public ResponseEntity<Void> doPut() {
        statsDClient.incrementCounter("put.healthz.count");
        logger.error("RESTController: Failure!!PutMapping Not allowed...");  
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("cache-control", "no-cache, no-store, must-revalidate").build();
    }

    @PatchMapping(value = "/healthz")
    public ResponseEntity<Void> doPatch() {
        statsDClient.incrementCounter("patch.healthz.count");
        logger.error("RESTController: Failure!!PatchMapping Not allowed...");  
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("cache-control", "no-cache, no-store, must-revalidate").build();
    }

    @DeleteMapping(value = "/healthz")
    public ResponseEntity<Void> doDelete() {
        statsDClient.incrementCounter("delete.healthz.count");
        logger.error("RESTController: Failure!!DeleteMapping Not allowed...");  
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("cache-control", "no-cache, no-store, must-revalidate").build();
    }

}
