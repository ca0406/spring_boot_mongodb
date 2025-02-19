package com.employee.test;

import com.employee.test.model.Employee;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

public class WireMockTest {
    private WireMockServer wireMockServer;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8089));
        wireMockServer.start();
        configureFor("localhost", 8089);

        // Stubbing GET /api/Employees/2
        wireMockServer.stubFor(get(urlEqualTo("/api/Employees/2"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"id\":2,\"name\":\"Lakshya\",\"company\":\"att\",\"role\":\"se\",\"salary\":800,\"date\":\"22-01-2004\"}")
                        .withHeader("Content-Type", "application/json")));

        // Stubbing GET /api/Employees
        wireMockServer.stubFor(get(urlEqualTo("/api/Employees"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("[{\"id\":1,\"name\":\"John\",\"company\":\"att\",\"role\":\"manager\",\"salary\":1000,\"date\":\"10-10-2000\"}, {\"id\":2,\"name\":\"Lakshya\",\"company\":\"att\",\"role\":\"se\",\"salary\":800,\"date\":\"22-01-2004\"}]")
                        .withHeader("Content-Type", "application/json")));

        // Stubbing POST /api/Employees
        wireMockServer.stubFor(post(urlEqualTo("/api/Employees"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody("{\"id\":3,\"name\":\"Jane\",\"company\":\"att\",\"role\":\"developer\",\"salary\":900,\"date\":\"15-05-2005\"}")
                        .withHeader("Content-Type", "application/json")));

        // Stubbing PUT /api/Employees/2
        wireMockServer.stubFor(put(urlEqualTo("/api/Employees/2"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"id\":2,\"name\":\"Lakshya Updated\",\"company\":\"att\",\"role\":\"se\",\"salary\":800,\"date\":\"22-01-2004\"}")
                        .withHeader("Content-Type", "application/json")));

        // Stubbing DELETE /api/Employees/2
        wireMockServer.stubFor(delete(urlEqualTo("/api/Employees/2"))
                .willReturn(aResponse()
                        .withStatus(204)));

        // Stubbing DELETE /api/Employees
        wireMockServer.stubFor(delete(urlEqualTo("/api/Employees"))
                .willReturn(aResponse()
                        .withStatus(204)));

        // Stubbing GET /api/Employees/role/se
        wireMockServer.stubFor(get(urlEqualTo("/api/Employees/role/se"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("[{\"id\":2,\"name\":\"Lakshya\",\"company\":\"att\",\"role\":\"se\",\"salary\":800,\"date\":\"22-01-2004\"}]")
                        .withHeader("Content-Type", "application/json")));
    }
 
    @Test
    public void testGetEmployeeById() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8089/api/Employees/2";
        String response = restTemplate.getForObject(url, String.class);
        assertEquals("{\"id\":2,\"name\":\"Lakshya\",\"company\":\"att\",\"role\":\"se\",\"salary\":800,\"date\":\"22-01-2004\"}", response);
    }

    @Test
    public void testGetAllEmployees() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8089/api/Employees";
        String response = restTemplate.getForObject(url, String.class);
        assertTrue(response.contains("\"id\":1"));
        assertTrue(response.contains("\"id\":2"));
    }

    @Test
    public void testCreateEmployee() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8089/api/Employees";

        Employee newEmployee = new Employee("Jane", "att", "developer", 900, LocalDate.of(2005, 5, 15));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Employee> request = new HttpEntity<>(newEmployee, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().contains("\"id\":3"));
    }

    @Test
    public void testUpdateEmployee() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8089/api/Employees/2";

        Employee updatedEmployee = new Employee("Lakshya Updated", "att", "se", 800, LocalDate.of(2004, 1, 22));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Employee> request = new HttpEntity<>(updatedEmployee, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("\"name\":\"Lakshya Updated\""));
    }

    @Test
    public void testDeleteEmployee() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8089/api/Employees/2";

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteAllEmployees() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8089/api/Employees";

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testFindByRole() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8089/api/Employees/role/se";
        String response = restTemplate.getForObject(url, String.class);
        assertTrue(response.contains("\"role\":\"se\""));
    }

    @AfterEach
    public void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}