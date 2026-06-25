package com.department.dms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.department.dms.model.WeeklyReport;

public interface WeeklyReportRepository 
        extends MongoRepository<WeeklyReport, String> {
}