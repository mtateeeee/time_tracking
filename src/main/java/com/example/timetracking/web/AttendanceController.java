package com.example.timetracking.web;

import com.example.timetracking.model.AttendanceRecord;
import com.example.timetracking.model.Employee;
import com.example.timetracking.service.AttendanceService;
import com.example.timetracking.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    public AttendanceController(AttendanceService attendanceService, EmployeeService employeeService) {
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceRecord> checkIn(Authentication authentication) {
        Employee employee = employeeService.findByUsername(authentication.getName())
                .orElseThrow();
        return ResponseEntity.ok(attendanceService.checkIn(employee));
    }

    @PostMapping("/check-out")
    public ResponseEntity<AttendanceRecord> checkOut(Authentication authentication) {
        Employee employee = employeeService.findByUsername(authentication.getName())
                .orElseThrow();
        return ResponseEntity.ok(attendanceService.checkOut(employee));
    }

    @GetMapping
    public ResponseEntity<List<AttendanceRecord>> myAttendance(Authentication authentication) {
        Employee employee = employeeService.findByUsername(authentication.getName())
                .orElseThrow();
        return ResponseEntity.ok(attendanceService.getForEmployee(employee));
    }
}

