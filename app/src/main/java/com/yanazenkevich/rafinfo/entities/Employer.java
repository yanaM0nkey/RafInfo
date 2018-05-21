package com.yanazenkevich.rafinfo.entities;

import com.google.gson.annotations.SerializedName;

public class Employer {

    @SerializedName("objectId")
    private String id;
    private String name;
    private String site;
    private String info;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
