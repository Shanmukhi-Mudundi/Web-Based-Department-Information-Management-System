package com.department.dms.service;

import com.department.dms.model.Announcement;
import com.department.dms.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {

    private final AnnouncementRepository repository;

    public AnnouncementService(AnnouncementRepository repository) {
        this.repository = repository;
    }

    public Announcement save(Announcement announcement) {
        return repository.save(announcement);
    }

    public List<Announcement> getAll() {
        return repository.findAll();
    }

    public Optional<Announcement> getById(String id) {
        return repository.findById(id);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
