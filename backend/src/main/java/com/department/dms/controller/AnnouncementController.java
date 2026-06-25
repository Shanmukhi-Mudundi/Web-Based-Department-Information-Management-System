package com.department.dms.controller;

import com.department.dms.model.Announcement;
import com.department.dms.service.AnnouncementService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin
public class AnnouncementController {

    private final AnnouncementService service;

    public AnnouncementController(AnnouncementService service) {
        this.service = service;
    }

    @PostMapping
    public Announcement addAnnouncement(@RequestBody Announcement announcement) {
        return service.save(announcement);
    }

    @GetMapping
    public List<Announcement> getAnnouncements() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncementById(@PathVariable String id) {
        return service.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deleteAnnouncement(@PathVariable String id) {
        service.delete(id);
    }
}
