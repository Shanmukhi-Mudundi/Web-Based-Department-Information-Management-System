package com.department.dms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "weekly_reports")
public class WeeklyReport {

    @Id
    private String id;

    private String weekNumber;
    private String title;
    private String description;
    private String facultyInCharge;
    private String date;

    public WeeklyReport() {
    }

    public WeeklyReport(String weekNumber, String title, String description,
                        String facultyInCharge, String date) {
        this.weekNumber = weekNumber;
        this.title = title;
        this.description = description;
        this.facultyInCharge = facultyInCharge;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(String weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacultyInCharge() {
        return facultyInCharge;
    }

    public void setFacultyInCharge(String facultyInCharge) {
        this.facultyInCharge = facultyInCharge;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}