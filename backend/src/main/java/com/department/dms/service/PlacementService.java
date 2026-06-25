package com.department.dms.service;

import com.department.dms.model.Placement;
import com.department.dms.repository.PlacementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlacementService {

    private final PlacementRepository repository;

    public PlacementService(PlacementRepository repository) {
        this.repository = repository;
    }

    public Placement save(Placement placement) {
        return repository.save(placement);
    }

    public List<Placement> getAll() {
        return repository.findAll();
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}