package com.department.dms.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.department.dms.model.Achievement;
import com.department.dms.repository.AchievementRepository;

@Service
public class AchievementService {
    private static final String COLLECTION = "student_achievements";
    private static final Pattern YEAR_PATTERN = Pattern.compile("\\b(19|20)\\d{2}\\b");
    private static final Pattern NUMERIC_DATE_PATTERN = Pattern.compile("\\b(\\d{1,2})[/-](\\d{1,2})[/-](\\d{4})\\b");
    private static final Pattern DAY_MONTH_YEAR_PATTERN = Pattern.compile("\\b(\\d{1,2})\\s+([a-z]+)\\s+(\\d{4})\\b");
    private static final Pattern MONTH_DAY_YEAR_PATTERN = Pattern.compile("\\b([a-z]+)\\s+(\\d{1,2})(?:\\s+(?:and|to|&)\\s+(\\d{1,2}))?\\s+(\\d{4})\\b");
    private static final Pattern MONTH_RANGE_PATTERN = Pattern.compile("\\b(\\d{1,2})\\s+([a-z]+)\\s*-\\s*(\\d{1,2})\\s+([a-z]+)\\s+(\\d{4})\\b");
    private static final List<DateTimeFormatter> DATE_FORMATTERS = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("d/M/uuuu"),
            DateTimeFormatter.ofPattern("dd/MM/uuuu"),
            DateTimeFormatter.ofPattern("d-M-uuuu"),
            DateTimeFormatter.ofPattern("dd-MM-uuuu"),
            DateTimeFormatter.ofPattern("d MMM uuuu", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.ENGLISH)
    );
    private static final List<DateTimeFormatter> MONTH_YEAR_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("MMM uuuu", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("MMMM uuuu", Locale.ENGLISH)
    );
    private static final Map<String, Integer> MONTHS = Map.ofEntries(
            Map.entry("january", 1), Map.entry("jan", 1),
            Map.entry("february", 2), Map.entry("feb", 2),
            Map.entry("march", 3), Map.entry("mar", 3),
            Map.entry("april", 4), Map.entry("apr", 4),
            Map.entry("may", 5),
            Map.entry("june", 6), Map.entry("jun", 6),
            Map.entry("july", 7), Map.entry("jul", 7),
            Map.entry("august", 8), Map.entry("aug", 8),
            Map.entry("september", 9), Map.entry("sep", 9), Map.entry("sept", 9),
            Map.entry("october", 10), Map.entry("oct", 10),
            Map.entry("november", 11), Map.entry("nov", 11),
            Map.entry("december", 12), Map.entry("dec", 12)
    );

    private final AchievementRepository repository;
    private final MongoTemplate mongoTemplate;

    public AchievementService(AchievementRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    public Achievement saveAchievement(Achievement achievement) {
        return repository.save(achievement);
    }

    public List<Map<String, Object>> getAllAchievements() {
        List<Map<String, Object>> achievements = new ArrayList<>();
        for (Document doc : getRawAchievements()) {
            achievements.add(toAchievementMap(doc));
        }
        achievements.sort(
                Comparator.comparing(
                                (Map<String, Object> item) -> resolveSortDate(asText(item.get("date"))))
                        .reversed()
                        .thenComparing(item -> asText(item.get("id")), Comparator.reverseOrder())
        );
        return achievements;
    }

    public List<Document> getRawAchievements() {
        return mongoTemplate.find(new Query(), Document.class, COLLECTION);
    }

    public Achievement getAchievementById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteAchievement(String id) {
        repository.deleteById(id);
    }

    private Map<String, Object> toAchievementMap(Document doc) {
        Map<String, Object> out = new LinkedHashMap<>();

        Object idValue = doc.get("_id");
        if (idValue != null) {
            out.put("id", idValue.toString());
        }

        String studentName = asText(findValue(doc, List.of(
                "Name of the Student",
                "studentName",
                "student_name",
                "student name",
                "name",
                "student",
                "studentFullName",
                "fullName",
                "details.name",
                "data.name"
        )));

        String title = asText(findValue(doc, List.of(
                "Prize Secured/Participation",
                "Event Name",
                "title",
                "achievement",
                "achievementTitle",
                "achievement_title",
                "achievementType",
                "achievement_type",
                "type",
                "category",
                "award",
                "prize",
                "result",
                "position",
                "recognition",
                "details.achievement",
                "data.achievement"
        )));

        String description = asText(findValue(doc, List.of(
                "Name of the Activity",
                "Organized by",
                "description",
                "details",
                "remarks",
                "remark",
                "summary",
                "note",
                "achievementDescription",
                "achievement_description",
                "details.description",
                "data.description"
        )));

        String department = asText(findValue(doc, List.of(
                "department",
                "dept",
                "branch",
                "program",
                "course",
                "details.department",
                "data.department"
        )));

        String date = asText(findValue(doc, List.of(
                "Date of the Events",
                "Year of the Study",
                "date",
                "year",
                "achievementDate",
                "achievement_date",
                "awardedOn",
                "awarded_on",
                "eventDate",
                "event_date",
                "createdAt",
                "created_at",
                "details.date",
                "data.date"
        )));

        out.put("studentName", studentName);
        out.put("title", title);
        out.put("description", description);
        out.put("department", department);
        out.put("date", date);
        out.put("raw", doc);
        return out;
    }

    private Object findValue(Document doc, List<String> names) {
        for (String name : names) {
            Object direct = getNestedValue(doc, name);
            if (direct != null && !String.valueOf(direct).isBlank()) {
                return direct;
            }
        }

        Map<String, String> normalized = new HashMap<>();
        for (String key : doc.keySet()) {
            normalized.put(normalizeKey(key), key);
        }

        for (String name : names) {
            String key = normalized.get(normalizeKey(name));
            if (key != null) {
                Object value = doc.get(key);
                if (value != null && !String.valueOf(value).isBlank()) {
                    return value;
                }
            }
        }

        return null;
    }

    private Object getNestedValue(Document doc, String path) {
        if (doc == null || path == null) {
            return null;
        }
        if (!path.contains(".")) {
            return doc.get(path);
        }

        String[] parts = path.split("\\.");
        Object current = doc;
        for (String part : parts) {
            if (!(current instanceof Map)) {
                return null;
            }
            current = ((Map<?, ?>) current).get(part);
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    private String normalizeKey(String value) {
        if (value == null) {
            return "";
        }
        return value.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }

    private String asText(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private LocalDate resolveSortDate(String rawDate) {
        String value = asText(rawDate);
        if (value.isEmpty()) {
            return LocalDate.MIN;
        }

        String normalizedValue = value
                .toLowerCase(Locale.ENGLISH)
                .replaceAll("(\\d+)\\s*(st|nd|rd|th)\\b", "$1")
                .replace(',', ' ')
                .replaceAll("\\s+", " ")
                .trim();

        List<LocalDate> candidates = new ArrayList<>();

        if (value.matches("\\d{4}")) {
            candidates.add(LocalDate.of(Integer.parseInt(value), 12, 31));
        }

        collectNumericDates(normalizedValue, candidates);
        collectMonthDates(normalizedValue, candidates);

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                candidates.add(LocalDate.parse(value, formatter));
            } catch (DateTimeParseException ignored) {
            }
        }

        String normalized = value.replace(",", " ").replaceAll("\\s+", " ").trim();
        for (DateTimeFormatter formatter : MONTH_YEAR_FORMATTERS) {
            try {
                candidates.add(YearMonth.parse(normalized, formatter).atEndOfMonth());
            } catch (DateTimeParseException ignored) {
            }
        }

        if (candidates.isEmpty()) {
            Matcher matcher = YEAR_PATTERN.matcher(normalizedValue);
            while (matcher.find()) {
                candidates.add(LocalDate.of(Integer.parseInt(matcher.group()), 12, 31));
            }
        }

        return candidates.stream().max(LocalDate::compareTo).orElse(LocalDate.MIN);
    }

    private void collectNumericDates(String value, List<LocalDate> candidates) {
        Matcher matcher = NUMERIC_DATE_PATTERN.matcher(value);
        while (matcher.find()) {
            candidates.add(toDate(matcher.group(3), matcher.group(2), matcher.group(1)));
        }
    }

    private void collectMonthDates(String value, List<LocalDate> candidates) {
        Matcher monthRangeMatcher = MONTH_RANGE_PATTERN.matcher(value);
        while (monthRangeMatcher.find()) {
            addMonthCandidate(candidates, monthRangeMatcher.group(5), monthRangeMatcher.group(2), monthRangeMatcher.group(1));
            addMonthCandidate(candidates, monthRangeMatcher.group(5), monthRangeMatcher.group(4), monthRangeMatcher.group(3));
        }

        Matcher dayMonthYearMatcher = DAY_MONTH_YEAR_PATTERN.matcher(value);
        while (dayMonthYearMatcher.find()) {
            addMonthCandidate(candidates, dayMonthYearMatcher.group(3), dayMonthYearMatcher.group(2), dayMonthYearMatcher.group(1));
        }

        Matcher monthDayYearMatcher = MONTH_DAY_YEAR_PATTERN.matcher(value);
        while (monthDayYearMatcher.find()) {
            int day = Math.max(Integer.parseInt(monthDayYearMatcher.group(2)),
                    Integer.parseInt(monthDayYearMatcher.group(3) == null ? monthDayYearMatcher.group(2) : monthDayYearMatcher.group(3)));
            addMonthCandidate(candidates, monthDayYearMatcher.group(4), monthDayYearMatcher.group(1), String.valueOf(day));
        }
    }

    private void addMonthCandidate(List<LocalDate> candidates, String year, String monthName, String day) {
        Integer month = MONTHS.get(monthName);
        if (month == null) {
            return;
        }
        candidates.add(toDate(year, String.valueOf(month), day));
    }

    private LocalDate toDate(String year, String month, String day) {
        int safeYear = Integer.parseInt(year);
        int safeMonth = Math.max(1, Math.min(12, Integer.parseInt(month)));
        int safeDay = Math.max(1, Math.min(31, Integer.parseInt(day)));
        return LocalDate.of(safeYear, safeMonth, safeDay);
    }
}
