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
        return overtimeRecordRepository.save(overtimeRecord);
    }

    public List<OvertimeRecord> getForEmployee(Employee employee) {
        return overtimeRecordRepository.findByEmployee(employee);
    }
}

