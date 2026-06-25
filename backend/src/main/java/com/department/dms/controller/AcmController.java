package com.department.dms.controller;

import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/acm")
public class AcmController {

    private final MongoTemplate mongoTemplate;

    public AcmController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping
    public Map<String, Object> getAcmData() {
        Document info = mongoTemplate.findOne(new Query(), Document.class, "acm_info");
        List<Document> benefits = mongoTemplate.findAll(Document.class, "acm_benefits");
        List<Document> events = mongoTemplate.findAll(Document.class, "acm_events");

        return Map.of(
            "info", info == null ? Map.of() : info,
            "benefits", benefits,
            "events", events
        );
    }
}
