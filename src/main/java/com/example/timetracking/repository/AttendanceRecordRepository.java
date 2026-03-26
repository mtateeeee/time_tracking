package com.example.timetracking.repository;

import com.example.timetracking.model.AttendanceRecord;
import com.example.timetracking.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    List<AttendanceRecord> findByEmployee(Employee employee);

    Optional<AttendanceRecord> findByEmployeeAndDate(Employee employee, LocalDate date);
}

