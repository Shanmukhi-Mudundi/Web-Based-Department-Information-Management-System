package com.department.dms.controller;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import com.department.dms.model.Achievement;
import com.department.dms.service.AchievementService;

@RestController
@CrossOrigin
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementService service;

    public AchievementController(AchievementService service) {
        this.service = service;
    }

    @PostMapping
    public Achievement addAchievement(@RequestBody Achievement achievement) {
        return service.saveAchievement(achievement);
    }

    @GetMapping
    public Map<String, Object> getAllAchievements() {
        return Map.of(
                "items", service.getAllAchievements(),
                "version", "achievements-v2"
        );
    }

    @GetMapping("/raw")
    public Map<String, Object> getRawAchievements() {
        List<Document> items = service.getRawAchievements();
        return Map.of(
                "items", items,
                "version", "achievements-raw-v1"
        );
    }

    @GetMapping("/{id}")
    public Achievement getAchievement(@PathVariable String id) {
        return service.getAchievementById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteAchievement(@PathVariable String id) {
        service.deleteAchievement(id);
    }
}
