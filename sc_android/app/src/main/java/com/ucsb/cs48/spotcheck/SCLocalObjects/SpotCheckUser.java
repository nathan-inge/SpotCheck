package com.ucsb.cs48.spotcheck.SCLocalObjects;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class SpotCheckUser {

    private String userID;
    private String email;
    private String fullname;
    private String location;
    private String imageUrl;


    public SpotCheckUser() {
        // Default no argument constructor needed for Firebase database
    }

    public SpotCheckUser(String userID, String email, String fullname, String location) {
        this.userID = userID;
        this.email = email;
        this.fullname = fullname;
        this.location = location;
    }

    @Exclude
    public String getUserID() {
        return this.userID;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFullname() {
        return this.fullname;
    }

    public String getLocation() { return this.location; }

    public String getImageUrl() { return this.imageUrl; }

    @Exclude
    public void setUserID(String userID) { this.userID = userID; }

    public void setEmail(String email) { this.email = email; }

    public void setFullname(String fullname) { this.fullname = fullname; }

    public void setLocation(String location) { this.location = location; }

    public void setImageUrl(String url) { this.imageUrl = url; }
}
