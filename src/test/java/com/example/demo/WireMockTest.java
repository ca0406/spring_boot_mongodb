package com.example.demo;

import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.web.client.RestTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WireMockTest {
	private WireMockServer wireMockServer;

	@BeforeEach
	public void setup() {
		wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8089));
		wireMockServer.start();
		configureFor("localhost", 8089);
		wireMockServer.stubFor(get(urlEqualTo("/api/users")).willReturn(aResponse().withStatus(200)
				.withBody("{\"id\":2,\"name\":\"Lakshya\",\"salary\":50000,\"dept\":\"btpd\"}")
				.withHeader("Content-Type", "application/json")));
		wireMockServer.stubFor(get(urlEqualTo("/employee/get/2/salary")).willReturn(aResponse().withStatus(200)
				.withBody("Salary of employee with id 2 is 50000").withHeader("Content-Type", "application/json")));
	}


	@Test
	public void testGetEmployeeById() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/employee/get/2";
		String response = restTemplate.getForObject(url, String.class);
		assertEquals("{\"id\":2,\"name\":\"Lakshya\",\"salary\":50000,\"dept\":\"btpd\"}", response);
	}

	@Test    public void getEmployeeSalary() {        RestTemplate restTemplate = new RestTemplate();        String url = "http://localhost:8080/employee/get/2/salary";        String response = restTemplate.getForObject(url,String.class);        assertEquals("Salary of employee with id 2 is 50000", response);    }

@AfterEach    public void tearDown() {
 if (wireMockServer != null) {
 wireMockServer.stop(); } }}
