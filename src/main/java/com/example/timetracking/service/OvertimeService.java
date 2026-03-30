package com.example.timetracking.service;

import com.example.timetracking.model.Employee;
import com.example.timetracking.model.OvertimeRecord;
import com.example.timetracking.repository.OvertimeRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OvertimeService {

    private final OvertimeRecordRepository overtimeRecordRepository;

    public OvertimeService(OvertimeRecordRepository overtimeRecordRepository) {
        this.overtimeRecordRepository = overtimeRecordRepository;
    }

    public OvertimeRecord logOvertime(OvertimeRecord overtimeRecord) {
        overtimeRecord.setStatus(OvertimeRecord.Status.PENDING);
        return overtimeRecordRepository.save(overtimeRecord);
    }

    public List<OvertimeRecord> getForEmployee(Employee employee) {
        return overtimeRecordRepository.findByEmployee(employee);
    }

    public List<OvertimeRecord> getPending() {
        // Older records (before status column existed) may have NULL -> treat as pending
        return overtimeRecordRepository.findByStatusOrStatusIsNull(OvertimeRecord.Status.PENDING);
    }

    public OvertimeRecord approve(Long id) {
        OvertimeRecord request = overtimeRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Overtime request not found"));
        request.setStatus(OvertimeRecord.Status.APPROVED);
        return overtimeRecordRepository.save(request);
    }

    public OvertimeRecord reject(Long id) {
        OvertimeRecord request = overtimeRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Overtime request not found"));
        request.setStatus(OvertimeRecord.Status.REJECTED);
        return overtimeRecordRepository.save(request);
    }
}

