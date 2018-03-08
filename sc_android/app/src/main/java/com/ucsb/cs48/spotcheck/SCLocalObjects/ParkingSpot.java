package com.ucsb.cs48.spotcheck.SCLocalObjects;

import com.google.firebase.database.Exclude;

import java.text.NumberFormat;
import java.util.ArrayList;


public class ParkingSpot {

    private String spotID;
    private String ownerID;
    private String address;
    private Double rate;
    private SCLatLng latLng;
    private ArrayList<BlockedDates> blockedDatesList = new ArrayList<>();

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

    public void addBlockedDates(BlockedDates newBlock) {
        this.blockedDatesList.add(newBlock);
    }

    public void removeBlockedDates(BlockedDates oldBlock) {
        this.blockedDatesList.remove(oldBlock);
    }

    public int getBlockedDatesCount() {
        return this.blockedDatesList.size();
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

    public ArrayList<BlockedDates> getBlockedDatesList() { return this.blockedDatesList; }

    @Exclude
    public void setSpotID(String spotID) { this.spotID = spotID; }

    public void setOwnerID(String ownerID) { this.ownerID = ownerID; }

    public void setAddress(String address) { this.address = address; }

    public void setLatLng(SCLatLng latLng) { this.latLng = latLng; }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public void setBlockedDatesList(ArrayList<BlockedDates> list) { this.blockedDatesList = list; }
}
