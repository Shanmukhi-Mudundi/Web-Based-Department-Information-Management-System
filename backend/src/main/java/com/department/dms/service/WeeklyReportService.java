package com.department.dms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.department.dms.model.WeeklyReport;
import com.department.dms.repository.WeeklyReportRepository;

@Service
public class WeeklyReportService {

    private final WeeklyReportRepository repository;

    public WeeklyReportService(WeeklyReportRepository repository) {
        this.repository = repository;
    }

    public WeeklyReport saveWeeklyReport(WeeklyReport report) {
        return repository.save(report);
    }

    public List<WeeklyReport> getAllWeeklyReports() {
        return repository.findAll();
    }

    public WeeklyReport getWeeklyReportById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteWeeklyReport(String id) {
        repository.deleteById(id);
    }
}