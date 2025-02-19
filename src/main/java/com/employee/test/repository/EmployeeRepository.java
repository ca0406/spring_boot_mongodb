package com.employee.test.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.employee.test.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByRole(String role);
    List<Employee> findByNameContaining(String name);
}