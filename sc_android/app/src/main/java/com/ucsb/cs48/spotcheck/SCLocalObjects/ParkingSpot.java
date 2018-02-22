package com.ucsb.cs48.spotcheck.SCLocalObjects;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.text.NumberFormat;


public class ParkingSpot {

    private String spotID;
    private String ownerID;
    private String address;
    private Double rate;
    private SCLatLng latLng;

    public ParkingSpot() {
        // Required no argument constructor for Firebase database
    }

    // Constructor only used to create new spot in interface
    public ParkingSpot(String ownerID, String address, SCLatLng latLng, Double rate) {
        this.ownerID = ownerID;
        this.address = address;
        this.latLng = latLng;
        this.rate = rate;
    }

    public ParkingSpot(String spotID, String ownerID, String address, SCLatLng latLng, Double rate) {
        this.spotID = spotID;
        this.ownerID = ownerID;
        this.address = address;
        this.latLng = latLng;
        this.rate = rate;
    }

    public String formattedRate() {
        return NumberFormat.getCurrencyInstance().format((rate));
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

    public SCLatLng getLatLng() { return this.latLng; }

    public Double getRate() {
        return this.rate;
    }

    @Exclude
    public void setSpotID(String spotID) { this.spotID = spotID; }

    public void setOwnerID(String ownerID) { this.ownerID = ownerID; }

    public void setAddress(String address) { this.address = address; }

    public void setLatLng(SCLatLng latLng) { this.latLng = latLng; }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
