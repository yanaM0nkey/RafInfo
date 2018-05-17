package com.yanazenkevich.rafinfo.entities;

import com.google.gson.annotations.SerializedName;

public class Staff {

    private int department;
    @SerializedName("full_name")
    private String fullName;
    private String position;
    private String description;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String email;
    private String address;

    public int getDepartment() {
        return department;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }
}
