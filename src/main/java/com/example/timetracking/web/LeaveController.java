package com.example.timetracking.web;

import com.example.timetracking.model.Employee;
import com.example.timetracking.model.LeaveRequest;
import com.example.timetracking.service.EmployeeService;
import com.example.timetracking.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;
    private final EmployeeService employeeService;

    public LeaveController(LeaveService leaveService, EmployeeService employeeService) {
        this.leaveService = leaveService;
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<LeaveRequest> requestLeave(@Valid @RequestBody LeaveRequest leaveRequest,
                                                     Authentication authentication) {
        Employee employee = employeeService.findByUsername(authentication.getName())
                .orElseThrow();
        leaveRequest.setEmployee(employee);
        return ResponseEntity.ok(leaveService.createLeaveRequest(leaveRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<List<LeaveRequest>> myLeaves(Authentication authentication) {
        Employee employee = employeeService.findByUsername(authentication.getName())
                .orElseThrow();
        return ResponseEntity.ok(leaveService.getForEmployee(employee));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<LeaveRequest>> pending() {
        return ResponseEntity.ok(leaveService.getPending());
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<LeaveRequest> approve(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.approve(id));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<LeaveRequest> reject(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.reject(id));
    }
}

