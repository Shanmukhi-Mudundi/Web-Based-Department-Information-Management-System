package com.department.dms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.department.dms.model.Complaint;

public interface ComplaintRepository 
        extends MongoRepository<Complaint, String> {
}