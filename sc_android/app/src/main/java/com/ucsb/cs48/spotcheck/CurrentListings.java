package com.ucsb.cs48.spotcheck;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseAuth;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;

public class CurrentListings extends AppCompatActivity {

    private SpotCheckUser user;
    private SCFirebase scFirebase;
    private SCFirebaseAuth scFirebaseAuth;
    private String spot_name;
    private String currentSCUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_listings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize interface objects
        scFirebase = new SCFirebase();
        scFirebaseAuth = new SCFirebaseAuth();

        // Initialize UI objects
        Intent intent = getIntent();
        currentSCUserID = intent.getStringExtra("currentSCUserID");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Go to My Parking Spots", Snackbar.LENGTH_LONG)
                        .setAction("Going to My Parking Spots", null).show();
            }
        });

        scFirebase.getSCUser(currentSCUserID, new SCFirebaseCallback<SpotCheckUser>() {
            @Override
            public void callback(SpotCheckUser data) {
                if(data != null) {
                    user = data;

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (user.getCurrentListings().size() < 10){
                                //Set button text as address
                                View b = findViewById(R.id.spot10);
                                b.setVisibility(View.GONE);
                            }
                            if (user.getCurrentListings().size() < 9){
                                View b = findViewById(R.id.spot9);
                                b.setVisibility(View.GONE);
                            }
                            if (user.getCurrentListings().size() < 8){
                                View b = findViewById(R.id.spot8);
                                b.setVisibility(View.GONE);
                            }
                            if (user.getCurrentListings().size() < 7){
                                View b = findViewById(R.id.spot7);
                                b.setVisibility(View.GONE);
                            }
                            if (user.getCurrentListings().size() < 6){
                                View b = findViewById(R.id.spot6);
                                b.setVisibility(View.GONE);
                            }
                            if (user.getCurrentListings().size() < 5){
                                View b = findViewById(R.id.spot5);
                                b.setVisibility(View.GONE);
                            }
                            if (user.getCurrentListings().size() < 4){
                                View b = findViewById(R.id.spot4);
                                b.setVisibility(View.GONE);
                            }
                            if (user.getCurrentListings().size() < 3){
                                View b = findViewById(R.id.spot3);
                                b.setVisibility(View.GONE);
                            }
                            if (user.getCurrentListings().size() < 2){
                                View b = findViewById(R.id.spot2);
                                b.setVisibility(View.GONE);
                            }
                            if (user.getCurrentListings().size() < 1){
                                View b = findViewById(R.id.spot1);
                                b.setVisibility(View.GONE);
                            }

                        }
                    });
                }
            }
        });

    }
    public void goToMyParkingSpots(View vew) {
        if (user != null) {
            Intent intent = new Intent(this, EditProfile.class);
            intent.putExtra("currentSCUserID", user.getUserID());
            startActivity(intent);
        }
    }

    public void goToSpot1(View view) {
        if (user != null) {
            Intent intent = new Intent(this, EditProfile.class);
            intent.putExtra("currentSCUserID", user.getUserID());
            startActivity(intent);
        }
    }

    public void goToSpot2(View view) {
        if (user != null) {
            Intent intent = new Intent(this, EditProfile.class);
            intent.putExtra("currentSCUserID", user.getUserID());
            startActivity(intent);
        }
    }
}
