package com.department.dms.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.department.dms.model.Topper;
import com.department.dms.service.TopperService;

@RestController
@CrossOrigin
@RequestMapping("/api/toppers")
public class TopperController {

    private final TopperService service;

    public TopperController(TopperService service) {
        this.service = service;
    }

    @PostMapping
    public Topper addTopper(@RequestBody Topper topper) {
        return service.saveTopper(topper);
    }

    @GetMapping
    public List<Topper> getAllToppers() {
        return service.getAllToppers();
    }

    @GetMapping("/{id}")
    public Topper getTopper(@PathVariable String id) {
        return service.getTopperById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTopper(@PathVariable String id) {
        service.deleteTopper(id);
    }
}