package com.yanazenkevich.rafinfo.entities;

import com.google.gson.annotations.SerializedName;

public class Staff {

    @SerializedName("objectId")
    private String id;
    private int department;
    @SerializedName("full_name")
    private String fullName;
    private String position;
    private String description;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String email;
    private String address;
    private String surname;
    private String name;
    private String patronymic;

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

    public String getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
}
