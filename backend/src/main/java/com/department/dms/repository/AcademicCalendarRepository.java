package com.department.dms.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.department.dms.model.AcademicCalendar;

public interface AcademicCalendarRepository extends MongoRepository<AcademicCalendar, String> {
    Optional<AcademicCalendar> findByCalendarTypeIgnoreCaseAndYearLevelIgnoreCase(String calendarType, String yearLevel);
}
