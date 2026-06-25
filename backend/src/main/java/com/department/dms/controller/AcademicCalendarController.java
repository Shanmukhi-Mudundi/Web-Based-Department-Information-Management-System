package com.department.dms.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.department.dms.model.AcademicCalendar;
import com.department.dms.repository.AcademicCalendarRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/academic-calendars")
public class AcademicCalendarController {

    private final AcademicCalendarRepository repository;

    public AcademicCalendarController(AcademicCalendarRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Map<String, Object> list() {
        List<AcademicCalendar> items = new ArrayList<>(repository.findAll());
        items.sort(Comparator
            .comparing((AcademicCalendar item) -> calendarTypeSortValue(item == null ? null : item.getCalendarType()))
            .thenComparing(item -> yearLevelSortValue(item == null ? null : item.getYearLevel()))
            .thenComparing(item -> normalizeText(item == null ? null : item.getTitle())));
        return Map.of("items", items);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> upsert(@RequestBody AcademicCalendar payload) {
        String calendarType = normalizeCalendarType(payload == null ? null : payload.getCalendarType());
        String yearLevel = normalizeYearLevel(payload == null ? null : payload.getYearLevel());
        String title = payload == null ? null : clean(payload.getTitle());
        String previewUrl = payload == null ? null : clean(payload.getPreviewUrl());
        String fileUrl = payload == null ? null : clean(payload.getFileUrl());

        if (!StringUtils.hasText(calendarType)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "calendarType is required"));
        }
        if (!StringUtils.hasText(yearLevel)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "yearLevel is required"));
        }
        if (!StringUtils.hasText(previewUrl) && !StringUtils.hasText(fileUrl)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "previewUrl or fileUrl is required"));
        }

        AcademicCalendar item = repository
            .findByCalendarTypeIgnoreCaseAndYearLevelIgnoreCase(calendarType, yearLevel)
            .orElseGet(AcademicCalendar::new);

        item.setCalendarType(calendarType);
        item.setYearLevel(yearLevel);
        item.setTitle(StringUtils.hasText(title) ? title : calendarType + " " + yearLevel + " Academic Calendar");
        item.setPreviewUrl(previewUrl);
        item.setFileUrl(fileUrl);

        AcademicCalendar saved = repository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("item", saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable String id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "not found"));
        }
        repository.deleteById(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    private String clean(String value) {
        if (value == null) return null;
        String text = value.trim();
        return text.isBlank() ? null : text;
    }

    private String normalizeText(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ENGLISH);
    }

    private String normalizeCalendarType(String value) {
        String text = normalizeText(value);
        if (text.isBlank()) return null;
        return text.contains("non") ? "Non-Autonomous" : "Autonomous";
    }

    private String normalizeYearLevel(String value) {
        String text = normalizeText(value);
        if (text.isBlank()) return null;
        if (text.contains("iv") || text.contains("4")) return "IV Year";
        if (text.contains("iii") || text.contains("3")) return "III Year";
        if (text.contains("ii") || text.contains("2")) return "II Year";
        return value.trim();
    }

    private int calendarTypeSortValue(String value) {
        String normalized = normalizeCalendarType(value);
        return "Autonomous".equals(normalized) ? 0 : 1;
    }

    private int yearLevelSortValue(String value) {
        String normalized = normalizeYearLevel(value);
        if ("II Year".equals(normalized)) return 0;
        if ("III Year".equals(normalized)) return 1;
        if ("IV Year".equals(normalized)) return 2;
        return 99;
    }
}
