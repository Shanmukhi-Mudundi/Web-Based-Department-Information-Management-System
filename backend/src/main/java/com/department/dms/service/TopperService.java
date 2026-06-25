package com.department.dms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.department.dms.model.Topper;
import com.department.dms.repository.TopperRepository;

@Service
public class TopperService {

    private final TopperRepository repository;

    public TopperService(TopperRepository repository) {
        this.repository = repository;
    }

    public Topper saveTopper(Topper topper) {
        return repository.save(topper);
    }

    public List<Topper> getAllToppers() {
        return repository.findAll();
    }

    public Topper getTopperById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteTopper(String id) {
        repository.deleteById(id);
    }
}