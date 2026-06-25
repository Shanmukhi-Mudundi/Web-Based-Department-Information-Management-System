package com.department.dms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.department.dms.model.Timetable;

public interface TimetableRepository 
        extends MongoRepository<Timetable, String> {
}