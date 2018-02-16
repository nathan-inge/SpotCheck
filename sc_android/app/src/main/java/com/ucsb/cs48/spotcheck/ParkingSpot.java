package com.ucsb.cs48.spotcheck;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.util.UUID;

/**
 * Created by Nathan Inge on 2/3/18.
 */

public class ParkingSpot {

    private String spotID;
    private String ownerID;
    private String address;
    private Double rate;
    private LatLng latLng;

    public ParkingSpot() {

    }

    public ParkingSpot(String ownerID, String address, LatLng latLng, Double rate) {
        String uuid = UUID.randomUUID().toString();
        this.spotID = "spot-" + uuid;
        this.ownerID = ownerID;
        this.address = address;
        this.latLng = latLng;
        this.rate = rate;

    }

    @Exclude
    public String getSpotID() {
        return this.spotID;
    }

    public String getOwnerID() {
        return this.ownerID;
    }

    public String getAddress() {
        return this.address;
    }

    public Double getRate() {
        return this.rate;
    }

    public LatLng getLatLng() {return this.latLng;}

}
