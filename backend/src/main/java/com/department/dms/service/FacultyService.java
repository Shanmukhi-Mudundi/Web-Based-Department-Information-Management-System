package com.department.dms.service;

import com.department.dms.model.Faculty;
import com.department.dms.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository repository;

    public FacultyService(FacultyRepository repository) {
        this.repository = repository;
    }

    public Faculty save(Faculty faculty) {
        return repository.save(faculty);
    }

    public List<Faculty> getAll() {
        return repository.findAll();
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}