package com.department.dms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.department.dms.model.WeeklyReport;
import com.department.dms.service.WeeklyReportService;

@RestController
@CrossOrigin
@RequestMapping("/api/weeklyreports")
public class WeeklyReportController {

    private final WeeklyReportService service;
    private final MongoTemplate mongoTemplate;
    private final String adminToken;

    public WeeklyReportController(WeeklyReportService service,
                                  MongoTemplate mongoTemplate,
                                  @Value("${app.admin-token:}") String adminToken) {
        this.service = service;
        this.mongoTemplate = mongoTemplate;
        this.adminToken = adminToken == null ? "" : adminToken.trim();
    }

    @PostMapping
    public WeeklyReport addWeeklyReport(@RequestBody WeeklyReport report,
                                        @RequestHeader(value = "X-Admin-Token", required = false) String token) {
        requireAdmin(token);
        return service.saveWeeklyReport(report);
    }

    @GetMapping
    public List<WeeklyReport> getAllWeeklyReports() {
        return service.getAllWeeklyReports();
    }

    @GetMapping("/raw")
    public Map<String, Object> getAllWeeklyReportsRaw() {
        List<Document> docs = mongoTemplate.find(new Query(), Document.class, "weekly_reports");
        Map<String, Object> response = new HashMap<>();
        response.put("items", docs);
        response.put("version", "weekly-reports-raw-v1");
        return response;
    }

    @GetMapping("/{id}")
    public WeeklyReport getWeeklyReport(@PathVariable String id) {
        return service.getWeeklyReportById(id);
    }

    @PutMapping("/{id}")
    public WeeklyReport updateWeeklyReport(@PathVariable String id,
                                           @RequestBody WeeklyReport report,
                                           @RequestHeader(value = "X-Admin-Token", required = false) String token) {
        requireAdmin(token);
        report.setId(id);
        return service.saveWeeklyReport(report);
    }

    @DeleteMapping("/{id}")
    public void deleteWeeklyReport(@PathVariable String id,
                                   @RequestHeader(value = "X-Admin-Token", required = false) String token) {
        requireAdmin(token);
        service.deleteWeeklyReport(id);
    }

    private void requireAdmin(String token) {
        if (adminToken.isBlank()) {
            return;
        }
        if (token == null || !adminToken.equals(token.trim())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }
}
