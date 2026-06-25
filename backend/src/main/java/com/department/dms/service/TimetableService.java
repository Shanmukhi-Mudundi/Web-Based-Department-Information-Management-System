package com.department.dms.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.department.dms.model.Timetable;
import com.department.dms.repository.TimetableRepository;

@Service
public class TimetableService {

    private final TimetableRepository repository;

    public TimetableService(TimetableRepository repository) {
        this.repository = repository;
    }

    public Timetable save(Timetable timetable) {
        return repository.save(timetable);
    }

    public List<Timetable> getAll() {
        return repository.findAll();
    }
}