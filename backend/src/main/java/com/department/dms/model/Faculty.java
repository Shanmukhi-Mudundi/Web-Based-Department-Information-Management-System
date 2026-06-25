package com.department.dms.model;

import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "faculty")
public class Faculty {

    @Id
    private String id;

    private String name;
    private String designation;
    private String email;

    @Field("aicteId")
    private String aicteId;

    @Field("jntuhId")
    private String jntuhId;

    @Field("ratifiedStatus")
    private String ratifiedStatus;

    private String phd;
    private String mtech;
    private String btech;

    private String teachingExperience;
    private String researchExperience;
    private String industryExperience;

    private String specialization;

    @Field("photoUrl")
    private String photoUrl;

    @Field("photoBase64")
    private String photoBase64;

    private Map<String, String> researchIdentifiers;

    public Faculty() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAicteId() {
        return aicteId;
    }

    public void setAicteId(String aicteId) {
        this.aicteId = aicteId;
    }

    public String getJntuhId() {
        return jntuhId;
    }

    public void setJntuhId(String jntuhId) {
        this.jntuhId = jntuhId;
    }

    public String getRatifiedStatus() {
        return ratifiedStatus;
    }

    public void setRatifiedStatus(String ratifiedStatus) {
        this.ratifiedStatus = ratifiedStatus;
    }

    public String getPhd() {
        return phd;
    }

    public void setPhd(String phd) {
        this.phd = phd;
    }

    public String getMtech() {
        return mtech;
    }

    public void setMtech(String mtech) {
        this.mtech = mtech;
    }

    public String getBtech() {
        return btech;
    }

    public void setBtech(String btech) {
        this.btech = btech;
    }

    public String getTeachingExperience() {
        return teachingExperience;
    }

    public void setTeachingExperience(String teachingExperience) {
        this.teachingExperience = teachingExperience;
    }

    public String getResearchExperience() {
        return researchExperience;
    }

    public void setResearchExperience(String researchExperience) {
        this.researchExperience = researchExperience;
    }

    public String getIndustryExperience() {
        return industryExperience;
    }

    public void setIndustryExperience(String industryExperience) {
        this.industryExperience = industryExperience;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public Map<String, String> getResearchIdentifiers() {
        return researchIdentifiers;
    }

    public void setResearchIdentifiers(Map<String, String> researchIdentifiers) {
        this.researchIdentifiers = researchIdentifiers;
    }
}
