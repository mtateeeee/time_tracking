package com.example.timetracking.repository;

import com.example.timetracking.model.Employee;
import com.example.timetracking.model.OvertimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OvertimeRecordRepository extends JpaRepository<OvertimeRecord, Long> {

    List<OvertimeRecord> findByEmployee(Employee employee);
}

