package com.ucsb.cs48.spotcheck;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateSpotEntry extends AppCompatActivity {

    EditText addressInput;

    private DatabaseReference spotDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot_entry);

        addressInput = (EditText) findViewById(R.id.addressEditText);

    }

    public void submitSpotButtonTapped(View view) {
        String address = addressInput.getText().toString();

        spotDatabase = FirebaseDatabase.getInstance().getReference();

        ParkingSpot newSpot = new ParkingSpot("user1+2+3", address, 12.50);

        spotDatabase.child("parking_spots").child(newSpot.getSpotID()).setValue(newSpot);
    }

}
