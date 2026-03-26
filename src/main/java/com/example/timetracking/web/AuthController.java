package com.example.timetracking.web;

import com.example.timetracking.model.Employee;
import com.example.timetracking.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final EmployeeService employeeService;

    public AuthController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/register")
    public ResponseEntity<Employee> register(@Valid @RequestBody Employee employee) {
        // For demo purposes, default role EMPLOYEE
        if (employee.getRoles().isEmpty()) {
            employee.getRoles().add("EMPLOYEE");
        }
        return ResponseEntity.ok(employeeService.create(employee));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}

