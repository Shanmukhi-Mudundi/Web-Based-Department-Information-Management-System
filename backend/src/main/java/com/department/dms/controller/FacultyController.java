package com.department.dms.controller;

import com.department.dms.model.Faculty;
import com.department.dms.service.FacultyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin
public class FacultyController {

    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @PostMapping
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return service.save(faculty);
    }

    @GetMapping
    public List<Faculty> getFaculty() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void deleteFaculty(@PathVariable String id) {
        service.delete(id);
    }
}