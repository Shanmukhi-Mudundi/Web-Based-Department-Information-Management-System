package com.department.dms.controller;

import java.util.List;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import com.department.dms.model.Timetable;
import com.department.dms.service.TimetableService;

@RestController
@CrossOrigin
@RequestMapping("/api/timetables")
public class TimetableController {

    private final TimetableService service;
    private final MongoTemplate mongoTemplate;

    public TimetableController(TimetableService service, MongoTemplate mongoTemplate) {
        this.service = service;
        this.mongoTemplate = mongoTemplate;
    }

    @PostMapping
    public Timetable add(@RequestBody Timetable timetable) {
        return service.save(timetable);
    }

    @GetMapping
    public List<Document> getAll() {
        return mongoTemplate.findAll(Document.class, "Timetables");
    }
}
