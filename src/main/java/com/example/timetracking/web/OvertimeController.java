package com.example.timetracking.web;

import com.example.timetracking.model.Employee;
import com.example.timetracking.model.OvertimeRecord;
import com.example.timetracking.service.EmployeeService;
import com.example.timetracking.service.OvertimeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/overtime")
public class OvertimeController {

    private final OvertimeService overtimeService;
    private final EmployeeService employeeService;

    public OvertimeController(OvertimeService overtimeService, EmployeeService employeeService) {
        this.overtimeService = overtimeService;
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<OvertimeRecord> logOvertime(@Valid @RequestBody OvertimeRecord overtimeRecord,
                                                      Authentication authentication) {
        Employee employee = employeeService.findByUsername(authentication.getName())
                .orElseThrow();
        overtimeRecord.setEmployee(employee);
        return ResponseEntity.ok(overtimeService.logOvertime(overtimeRecord));
    }

    @GetMapping
    public ResponseEntity<List<OvertimeRecord>> myOvertime(Authentication authentication) {
        Employee employee = employeeService.findByUsername(authentication.getName())
                .orElseThrow();
        return ResponseEntity.ok(overtimeService.getForEmployee(employee));
    }
}

