package com.department.dms.repository;

import com.department.dms.model.Placement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlacementRepository extends MongoRepository<Placement, String> {
}