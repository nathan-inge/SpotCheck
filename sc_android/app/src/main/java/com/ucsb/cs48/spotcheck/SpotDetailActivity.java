package com.ucsb.cs48.spotcheck;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;

public class SpotDetailActivity extends AppCompatActivity {

    private SCFirebase scFirebase;
    private TextView addressView;
    private TextView rateView;

    private SpotCheckUser owner;
    private ParkingSpot spot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);

        // Initialize UI components
        addressView = findViewById(R.id.spot_detail_address_view);
        rateView = findViewById(R.id.spot_detail_rate_view);

        // Initialize SCFirebase instance
        scFirebase = new SCFirebase();

        // Get intent and extras
        Intent intent = getIntent();
        String spotID = intent.getStringExtra("spotID");

        // Get parking spot with ID from intent
        scFirebase.getParkingSpot(spotID, new SCFirebaseCallback<ParkingSpot>() {
            @Override
            public void callback(ParkingSpot data) {
                if(data != null) {
                    spot = data;

                    final Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            addressView.setText(spot.getAddress());
                            rateView.setText(String.format(
                                "%s%s",
                                spot.formattedRate(),
                                getString(R.string.per_hour)
                            ));
                        }
                    });

                }
            }
        });
    }

    public void rentButtonClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm Rent Request")
            .setMessage(("Are you sure you want to request to rent this spot?\n\n" +
                "The owner will be provided with your email to contact you directly."))
            .setPositiveButton("Rent", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with rent
                    // TODO: Actually send owner details or connect users somehow
                    Toast.makeText(getApplicationContext(), "Request Sent!",Toast.LENGTH_SHORT).show();
                    finish();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            })
            .setIcon(R.mipmap.spot_marker_icon)
            .show();
    }
}
