package com.ucsb.cs48.spotcheck.SCLocalObjects;

import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.UUID;


public class SpotCheckUser {

    private String userID;
    private String email;
    private String fullname;
    // private List<String> parkingSpotIDs;


    public SpotCheckUser() {
        // Default no argument constructor needed for Firebase database
    }

    public SpotCheckUser(String userID, String email, String fullname) {
        this.userID = userID;
        this.email = email;
        this.fullname = fullname;
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

    @Exclude
    public void setUserID(String userID) { this.userID = userID; }

    public void setEmail(String email) { this.email = email; }

    public void setFullname(String fullname) { this.fullname = fullname; }
}
