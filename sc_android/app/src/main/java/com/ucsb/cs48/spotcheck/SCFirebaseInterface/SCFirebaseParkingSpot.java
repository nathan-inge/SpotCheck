package com.ucsb.cs48.spotcheck.SCFirebaseInterface;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;

import java.util.UUID;

public class SCFirebaseParkingSpot {

    private DatabaseReference scDatabase;

    public SCFirebaseParkingSpot() {
        scDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void createNewSpot(ParkingSpot spot) {
        String newSpotID = "spot-" + UUID.randomUUID().toString();

        scDatabase.child("parking_spots").child(newSpotID).setValue(spot);
    }
}
