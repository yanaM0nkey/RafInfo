package com.yanazenkevich.rafinfo.entities;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("objectId")
    private String id;
    private String login;
    private String password;
    @SerializedName("user-token")
    private String token;
    private String surname;
    private String name;
    private String email;
    private int department;
    private boolean isAdmin;

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
