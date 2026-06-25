package com.department.dms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.department.dms.model.Topper;

public interface TopperRepository extends MongoRepository<Topper, String> {
}