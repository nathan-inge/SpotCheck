package com.ucsb.cs48.spotcheck;

import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.UUID;

/**
 * Created by Nathan Inge on 2/3/18.
 */

public class SpotCheckUser {

    private String userID;
    private String username;
    private String fullname;
    private List<String> parkingSpotIDs;


    public SpotCheckUser() {

    }

    public SpotCheckUser(String username, String fullname) {
        String uuid = UUID.randomUUID().toString();
        this.userID = "user-" + uuid;

        this.username = username;
        this.fullname = fullname;
    }

    public SpotCheckUser(String username, String fullname, List<String> parkingSpotIDs) {
        this(username, fullname);
        addParkingSpots(parkingSpotIDs);
    }

    @Exclude
    public String getUserID() {
        return this.userID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFullname() {
        return this.fullname;
    }

    public List<String> getParkingSpotIDs() {
        return this.parkingSpotIDs;
    }

    public void addParkingSpots(List<String> parkingSpotIDs) {
        for (String id : parkingSpotIDs) {
            this.parkingSpotIDs.add(id);
        }
    }
}
