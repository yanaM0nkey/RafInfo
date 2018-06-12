package com.yanazenkevich.rafinfo.entities;

import com.google.gson.annotations.SerializedName;

public class Announcement {

    @SerializedName("objectId")
    private String id;
    private String title;
    @SerializedName("started")
    private long date;
    private String location;
    private String description;
    private long created;
    private long updated;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreated() {
        return created;
    }

    public long getUpdated() {
        return updated;
    }
}
