package com.ucsb.cs48.spotcheck.SCFirebaseInterface;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;

public class SCFirebaseUser {

    private DatabaseReference scDatabase;

    public SCFirebaseUser() {
        scDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void createUser(SpotCheckUser user) {
        scDatabase.child("users").child(user.getUserID()).setValue(user);
    }
}
