package com.yanazenkevich.rafinfo.entities;

import com.google.gson.annotations.SerializedName;

public class Vacancy {

    @SerializedName("objectId")
    private String id;
    private String name;
    private Employer employer;
    private String location;
    @SerializedName("contact_info")
    private String contactInfo;
    private String description;
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

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
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
}
