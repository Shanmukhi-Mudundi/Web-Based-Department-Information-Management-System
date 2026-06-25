package com.department.dms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Timetables")
public class Timetable {

    @Id
    private String id;
    private String semester;
    private String subject;
    private String faculty;
    private String time;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
