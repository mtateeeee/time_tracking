package com.example.timetracking.service;

import com.example.timetracking.model.AttendanceRecord;
import com.example.timetracking.model.Employee;
import com.example.timetracking.repository.AttendanceRecordRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public byte[] exportAttendanceExcel(Employee employee) throws IOException {
        List<AttendanceRecord> records = getForEmployee(employee);
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Attendance");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Check In");
            header.createCell(2).setCellValue("Check Out");
            header.createCell(3).setCellValue("Work Hours");

            int rowIdx = 1;
            for (AttendanceRecord record : records) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(record.getDate() != null ? record.getDate().format(dateFmt) : "");
                row.createCell(1).setCellValue(record.getCheckInTime() != null ? record.getCheckInTime().format(dateTimeFmt) : "");
                row.createCell(2).setCellValue(record.getCheckOutTime() != null ? record.getCheckOutTime().format(dateTimeFmt) : "");

                String hours = "";
                if (record.getCheckInTime() != null && record.getCheckOutTime() != null) {
                    long minutes = java.time.Duration.between(record.getCheckInTime(), record.getCheckOutTime()).toMinutes();
                    if (minutes < 0) minutes = 0;
                    hours = String.format("%.2f", minutes / 60.0);
                }
                row.createCell(3).setCellValue(hours);
            }

            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}

