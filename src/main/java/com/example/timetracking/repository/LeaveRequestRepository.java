package com.example.timetracking.repository;

import com.example.timetracking.model.Employee;
import com.example.timetracking.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployee(Employee employee);

    List<LeaveRequest> findByStatus(LeaveRequest.Status status);

    List<LeaveRequest> findByStatusOrStatusIsNull(LeaveRequest.Status status);
}

