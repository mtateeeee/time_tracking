package com.example.timetracking.service;

import com.example.timetracking.model.AttendanceRecord;
import com.example.timetracking.model.Employee;
import com.example.timetracking.repository.AttendanceRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRecordRepository attendanceRecordRepository;

    public AttendanceService(AttendanceRecordRepository attendanceRecordRepository) {
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    public AttendanceRecord checkIn(Employee employee) {
        LocalDate today = LocalDate.now();
        AttendanceRecord record = attendanceRecordRepository
                .findByEmployeeAndDate(employee, today)
                .orElseGet(() -> {
                    AttendanceRecord r = new AttendanceRecord();
                    r.setEmployee(employee);
                    r.setDate(today);
                    return r;
                });
        record.setCheckInTime(LocalDateTime.now());
        return attendanceRecordRepository.save(record);
    }

    public AttendanceRecord checkOut(Employee employee) {
        LocalDate today = LocalDate.now();
        AttendanceRecord record = attendanceRecordRepository
                .findByEmployeeAndDate(employee, today)
                .orElseThrow(() -> new IllegalStateException("No check-in record for today"));
        record.setCheckOutTime(LocalDateTime.now());
        return attendanceRecordRepository.save(record);
    }

    public List<AttendanceRecord> getForEmployee(Employee employee) {
        return attendanceRecordRepository.findByEmployee(employee);
    }
}

