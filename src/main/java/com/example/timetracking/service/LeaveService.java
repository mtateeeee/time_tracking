package com.example.timetracking.service;

import com.example.timetracking.model.Employee;
import com.example.timetracking.model.LeaveRequest;
import com.example.timetracking.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequest.setStatus(LeaveRequest.Status.PENDING);
        return leaveRequestRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getForEmployee(Employee employee) {
        return leaveRequestRepository.findByEmployee(employee);
    }

    public LeaveRequest approve(Long id) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));
        request.setStatus(LeaveRequest.Status.APPROVED);
        return leaveRequestRepository.save(request);
    }

    public LeaveRequest reject(Long id) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));
        request.setStatus(LeaveRequest.Status.REJECTED);
        return leaveRequestRepository.save(request);
    }
}

