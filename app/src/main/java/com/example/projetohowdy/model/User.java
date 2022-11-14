package com.example.projetohowdy.model;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class User {

    public String idUSer;
    public String user;
    public String name;
    public String email;
    public String password;
    public List<String> blockedPeople;
    public List<String> pinnedInboxes;

    @Exclude
    public String getIdUSer() {
        return idUSer;
    }

    public void setIdUSer(String idUSer) {
        this.idUSer = idUSer;
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

    public List<String> getBlockedPeople() {
        return blockedPeople;
    }

    public void setBlockedPeople(List<String> blockedPeople) {
        this.blockedPeople = blockedPeople;
    }

    public List<String> getPinnedInboxes() {
        return pinnedInboxes;
    }

    public void setPinnedInboxes(List<String> pinnedInboxes) {
        this.pinnedInboxes = pinnedInboxes;
    }

    public User(String user, String name, String email, String password) {
        this.user = user;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String user, String name, String email, String password, List<String> blockedPeople, List<String> pinnedInboxes) {
        this.user = user;
        this.name = name;
        this.email = email;
        this.password = password;
        this.blockedPeople = blockedPeople;
        this.pinnedInboxes = pinnedInboxes;
    }

    public User(String user, String name, String email, String password, List<String> blockedPeople) {
        this.user = user;
        this.name = name;
        this.email = email;
        this.password = password;
        this.blockedPeople = blockedPeople;
    }

    public User(String idUSer, String user, String name, String email, String password, List<String> blockedPeople) {
        this.idUSer = idUSer;
        this.user = user;
        this.name = name;
        this.email = email;
        this.password = password;
        this.blockedPeople = blockedPeople;
    }

    public User(String idUSer, String user, String name, String email, String password) {
        this.idUSer = idUSer;
        this.user = user;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {
    }
}
