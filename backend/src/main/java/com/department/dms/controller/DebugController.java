package com.department.dms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final MongoTemplate mongoTemplate;

    public DebugController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/db")
    public Map<String, Object> dbInfo() {
        Map<String, Object> info = new HashMap<>();
        String dbName = mongoTemplate.getDb().getName();
        info.put("database", dbName);
        info.put("collections", mongoTemplate.getCollectionNames());
        return info;
    }

    @GetMapping("/counts")
    public Map<String, Object> counts() {
        Map<String, Object> info = new HashMap<>();
        List<String> collections = List.of("placements", "faculty", "acm_events", "acm_benefits", "Timetables");
        for (String name : collections) {
            try {
                long count = mongoTemplate.getCollection(name).countDocuments();
                info.put(name, count);
            } catch (Exception ex) {
                info.put(name, "error");
            }
        }
        return info;
    }
}
