package com.example.projetohowdy.model;

public class User {
    public String id;
    public String user;
    public String name;
    public String email;
    public String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String id, String user, String name, String email, String password) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {
    }
}
