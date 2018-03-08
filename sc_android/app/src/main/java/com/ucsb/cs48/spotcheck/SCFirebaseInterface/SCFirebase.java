package com.ucsb.cs48.spotcheck.SCFirebaseInterface;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ucsb.cs48.spotcheck.SCLocalObjects.BlockedDates;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;

import static com.ucsb.cs48.spotcheck.Utilities.SCConstants.TEST_SPOT_OWNER_ID;
import static com.ucsb.cs48.spotcheck.Utilities.SCConstants.TEST_USER_ID;

import java.util.ArrayList;
import java.util.UUID;

public class SCFirebase {

    private DatabaseReference scDatabase;

    private final String PARKINGSPOT_PATH = "parking_spots";
    private final String USER_PATH = "users";

    public SCFirebase() {
        scDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /**
     *
     * MARK - ParkingSpot Interface
     */

    // Create a new parking spot in the database
    public String createNewSpot(ParkingSpot spot) {
        String newSpotID = "spot-" + UUID.randomUUID().toString();

        scDatabase.child(PARKINGSPOT_PATH).child(newSpotID).setValue(spot);

        return newSpotID;
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

                    if(spot != null) {
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

    public void getAvailableParkingSpots(final long start, final long end,
                                         @NonNull final SCFirebaseCallback<ArrayList<ParkingSpot>> finishedCallback) {

        DatabaseReference myRef = scDatabase.child(PARKINGSPOT_PATH);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ParkingSpot> availableParkingSpots = new ArrayList<>();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ParkingSpot spot = postSnapshot.getValue(ParkingSpot.class);

                    if(spot != null) {
                        spot.setSpotID(postSnapshot.getKey());

                        if (spot.getBlockedDatesCount() == 0) {
                            availableParkingSpots.add(spot);

                        } else {
                            Boolean available = true;
                            ArrayList<BlockedDates> blockedDates = spot.getBlockedDatesList();

                            for ( BlockedDates block : blockedDates) {
                                if(block.conflict(start, end)) {
                                    available = false;
                                    break;
                                }
                            }

                            if(available) {
                                availableParkingSpots.add(spot);
                            }
                        }
                    }
                }

                finishedCallback.callback(availableParkingSpots);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void deleteParkingSpot(String spotID) {
        DatabaseReference myRef = scDatabase.child(PARKINGSPOT_PATH).child(spotID);
        myRef.removeValue();
    }

    public void deleteTestParkingSpots(@NonNull final SCFirebaseCallback<Boolean> finishedCallback) {
        DatabaseReference myRef = scDatabase.child(PARKINGSPOT_PATH);

        Query query = myRef.orderByChild("ownerID").equalTo(TEST_SPOT_OWNER_ID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with TEST_SPOT_OWNER_ID
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        issue.getRef().removeValue();
                    }
                    finishedCallback.callback(true);
                } else {
                    finishedCallback.callback(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     *
     * MARK - SpotCheckUser Interface
     */

    // Create or modify user object on database
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

    public void deleteUser(String userID) {
        DatabaseReference myRef = scDatabase.child(USER_PATH).child(userID);
        myRef.removeValue();
    }


    public void deleteTestUsers() {
       deleteUser(TEST_USER_ID);
    }
}
