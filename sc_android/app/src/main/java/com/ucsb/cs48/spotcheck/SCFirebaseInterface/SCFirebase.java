package com.ucsb.cs48.spotcheck.SCFirebaseInterface;


import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;

import java.io.ByteArrayOutputStream;
import android.net.Uri;
import java.util.ArrayList;
import java.util.UUID;

public class SCFirebase {

    private DatabaseReference scDatabase;
    private StorageReference scStorage;


    private final String PARKINGSPOT_PATH = "parking_spots";
    private final String USER_PATH = "users";

    public SCFirebase() {
        scDatabase = FirebaseDatabase.getInstance().getReference();
        scStorage = FirebaseStorage.getInstance().getReference();
    }

    /// MARK - Parking Spot Interface
    // Create a new parking spot in the database
    public String createNewSpot(ParkingSpot spot) {
        String newSpotID = "spot-" + UUID.randomUUID().toString();

        scDatabase.child(PARKINGSPOT_PATH).child(newSpotID).setValue(spot);

        return newSpotID;
    }

    // Update a current parking spot from the data base
    public void updateSpot(final String spotID, ParkingSpot updatedSpot) {
        scDatabase.child(PARKINGSPOT_PATH).child(spotID).setValue(updatedSpot);
    }

    // Get a parking spot from the data base
    public void getParkingSpot(final String spotID,
                               @NonNull final SCFirebaseCallback<ParkingSpot> finishedCallback) {

        DatabaseReference myRef = scDatabase.child(PARKINGSPOT_PATH);

        myRef.child(spotID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ParkingSpot spot = dataSnapshot.getValue(ParkingSpot.class);

                if(spot != null) {
                    spot.setSpotID(spotID);
                }

                finishedCallback.callback(spot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
    }

    public void getAllParkingSpots(
        @NonNull final SCFirebaseCallback<ArrayList<ParkingSpot>> finishedCalback) {

        DatabaseReference myRef = scDatabase.child(PARKINGSPOT_PATH);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ParkingSpot> parkingSpots = new ArrayList<>();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ParkingSpot spot = postSnapshot.getValue(ParkingSpot.class);

                    if (spot != null) {
                        spot.setSpotID(postSnapshot.getKey());
                        parkingSpots.add(spot);
                    }
                }

                finishedCalback.callback(parkingSpots);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
    }

    public void deleteParkingSpot(String spotID) {
        DatabaseReference myRef = scDatabase.child(PARKINGSPOT_PATH).child(spotID);
        myRef.removeValue();
    }

    public void uploadSpotImage(String spotID, Bitmap imageBitmap,
                                @NonNull final SCFirebaseCallback<Uri> finishedCallback) {

        StorageReference uploadLocation = scStorage.child(
            PARKINGSPOT_PATH).child(spotID + " -spotImage.jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = uploadLocation.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finishedCallback.callback(null);
                
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                finishedCallback.callback(taskSnapshot.getDownloadUrl());

            }
        });
    }


    /// MARK - User Interface
    // Create a new user in database
    // SHOULD ONLY BE USED WHEN REGISTERING A NEW USER
    public void uploadUser(SpotCheckUser user) {
        scDatabase.child("users").child(user.getUserID()).setValue(user);
    }

    // Get a user from the database
    public void getSCUser(final String userID,
                          @NonNull final SCFirebaseCallback<SpotCheckUser> finishedCallback) {

        DatabaseReference myRef = scDatabase.child(USER_PATH);

        myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SpotCheckUser user = dataSnapshot.getValue(SpotCheckUser.class);

                if(user != null) {
                    user.setUserID(userID);
                }

                finishedCallback.callback(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
    }
}
