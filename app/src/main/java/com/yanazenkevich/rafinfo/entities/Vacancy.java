package com.yanazenkevich.rafinfo.entities;

import com.google.gson.annotations.SerializedName;

public class Vacancy {

    @SerializedName("objectId")
    private String id;
    private String name;
    private String employer;
    private String location;
    @SerializedName("contact_info")
    private String contactInfo;
    private String description;
    @SerializedName("employer_site")
    private String employerSite;
    @SerializedName("employer_info")
    private String employerInfo;
    private long created;
    private long updated;

    public String getId() {
        return id;
    }

    public long getCreated() {
        return created;
    }

    public long getUpdated() {
        return updated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmployerSite() {
        return employerSite;
    }

    public void setEmployerSite(String employerSite) {
        this.employerSite = employerSite;
    }

    public String getEmployerInfo() {
        return employerInfo;
    }

    public void setEmployerInfo(String employerInfo) {
        this.employerInfo = employerInfo;
    }
}
