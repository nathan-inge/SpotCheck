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
    private ArrayList<String> parkingSpots;
    private ArrayList<String> currentListings;
    // private List<String> parkingSpotIDs;


    public SpotCheckUser() {
        // Default no argument constructor needed for Firebase database
    }

    public SpotCheckUser(String userID, String email, String fullname, String location) {
        this.userID = userID;
        this.email = email;
        this.fullname = fullname;
        this.location = location;
        this.parkingSpots = new ArrayList<String>(); // Spots user owns
        this.currentListings = new ArrayList<String>(); // Spots user renting
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

    public ArrayList<String> getParkingSpots() { return this.parkingSpots; }

    public ArrayList<String> getCurrentListings() { return this.currentListings; }

    @Exclude
    public void setUserID(String userID) { this.userID = userID; }

    public void setEmail(String email) { this.email = email; }

    public void setFullname(String fullname) { this.fullname = fullname; }

    public void setLocation(String location) { this.location = location; }

    public void addParkingSpot(String spot) { this.parkingSpots.add(spot); }

    public void removeParkingSpot(String spot) { parkingSpots.remove(spot); }

    public void addListing(String spot) { this.currentListings.add(spot); }

    public void removeListing (String spot) { this.currentListings.remove(spot); }


}
