package com.department.dms.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import com.department.dms.model.Complaint;
import com.department.dms.repository.ComplaintRepository;

@Service
public class ComplaintService {

    private final ComplaintRepository repository;

    public ComplaintService(ComplaintRepository repository) {
        this.repository = repository;
    }

    public Complaint save(Complaint complaint) {
        return repository.save(complaint);
    }

    public List<Complaint> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "submittedAt"));
    }
}
