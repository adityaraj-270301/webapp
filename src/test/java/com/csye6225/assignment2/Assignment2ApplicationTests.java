package com.csye6225.assignment2;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Assignment2ApplicationTests {

	@Test
	void contextLoads() {
	}

	@LocalServerPort
	private int port;

	private final TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testHealthEndpoint() {
		ResponseEntity<String> response = restTemplate.getForEntity(
				"http://localhost:" + port + "/healthz", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

}
