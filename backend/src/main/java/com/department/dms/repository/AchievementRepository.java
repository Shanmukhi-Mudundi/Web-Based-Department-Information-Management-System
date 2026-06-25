package com.department.dms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.department.dms.model.Achievement;

public interface AchievementRepository 
        extends MongoRepository<Achievement, String> {
}