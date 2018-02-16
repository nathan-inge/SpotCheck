package com.ucsb.cs48.spotcheck.SCFirebaseInterface;


import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;

import java.util.UUID;

public class SCFirebaseParkingSpot {

    private DatabaseReference scDatabase;

    public SCFirebaseParkingSpot() {
        scDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public String createNewSpot(ParkingSpot spot) {
        String newSpotID = "spot-" + UUID.randomUUID().toString();

        scDatabase.child("parking_spots").child(newSpotID).setValue(spot);

        return newSpotID;
    }

    public void getParkingSpot(String spotID,
        @NonNull final SCFirebaseCallback<ParkingSpot> finishedCallback) {

        DatabaseReference myRef = scDatabase.child("parking_spots/");

        myRef.child(spotID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ParkingSpot spot = dataSnapshot.getValue(ParkingSpot.class);
                finishedCallback.callback(spot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
    }
}
