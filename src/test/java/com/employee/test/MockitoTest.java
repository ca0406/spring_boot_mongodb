package com.employee.test;

import com.employee.test.controller.EmployeeController;
import com.employee.test.model.Employee;
import com.employee.test.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class MockitoTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        // Arrange
        Employee employee1 = new Employee("John", "att", "manager", 1000, LocalDate.of(2000, 10, 10));
        Employee employee2 = new Employee("Lakshya", "att", "se", 800, LocalDate.of(2004, 1, 22));
        List<Employee> employees = Arrays.asList(employee1, employee2);

        when(employeeRepository.findAll()).thenReturn(employees);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testGetEmployeeById() {
        // Arrange
        Employee employee = new Employee("Lakshya", "att", "se", 800, LocalDate.of(2004, 1, 22));

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        // Act
        ResponseEntity<Employee> response = employeeController.getEmployeeById(2L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
        verify(employeeRepository, times(1)).findById(2L);
    }

    @Test
public void testCreateEmployee() {
    // Arrange
    Employee employee = new Employee("Jane", "att", "developer", 900, LocalDate.of(2005, 5, 15));
    when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

    // Act
    ResponseEntity<Employee> response = employeeController.createEmployee(employee);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(employee, response.getBody());

    ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
    verify(employeeRepository, times(1)).save(employeeCaptor.capture());
    assertEquals(employee.getName(), employeeCaptor.getValue().getName());
    assertEquals(employee.getCompany(), employeeCaptor.getValue().getCompany());
    assertEquals(employee.getRole(), employeeCaptor.getValue().getRole());
}

@Test
public void testUpdateEmployee() {
    // Arrange
    Employee existingEmployee = new Employee("Lakshya", "att", "se", 800, LocalDate.of(2004, 1, 22));
    Employee updatedEmployee = new Employee("Lakshya Updated", "att", "se", 800, LocalDate.of(2004, 1, 22));

    when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(existingEmployee));
    when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

    // Act
    ResponseEntity<Employee> response = employeeController.updateEmployee(2L, updatedEmployee);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedEmployee, response.getBody());

    ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
    verify(employeeRepository, times(1)).save(employeeCaptor.capture());
    assertEquals(updatedEmployee.getName(), employeeCaptor.getValue().getName());
    assertEquals(updatedEmployee.getCompany(), employeeCaptor.getValue().getCompany());
    assertEquals(updatedEmployee.getRole(), employeeCaptor.getValue().getRole());
}

    @Test
    public void testDeleteEmployee() {
        // Arrange
        doNothing().when(employeeRepository).deleteById(anyLong());

        // Act
        ResponseEntity<HttpStatus> response = employeeController.deleteEmployee(2L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(employeeRepository, times(1)).deleteById(2L);
    }

    @Test
    public void testDeleteAllEmployees() {
        // Arrange
        doNothing().when(employeeRepository).deleteAll();

        // Act
        ResponseEntity<HttpStatus> response = employeeController.deleteAllEmployees();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(employeeRepository, times(1)).deleteAll();
    }

    @Test
    public void testFindByRole() {
        // Arrange
        Employee employee = new Employee("Lakshya", "att", "se", 800, LocalDate.of(2004, 1, 22));
        List<Employee> employees = Arrays.asList(employee);

        when(employeeRepository.findByRole("se")).thenReturn(employees);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.findByRole("se");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("se", response.getBody().get(0).getRole());
        verify(employeeRepository, times(1)).findByRole("se");
    }
}