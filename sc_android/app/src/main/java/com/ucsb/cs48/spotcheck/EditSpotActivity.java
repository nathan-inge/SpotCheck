package com.ucsb.cs48.spotcheck;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseAuth;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.Utilities.MoneyTextWatcher;

/**
 * Created by Ray on 3/9/2018.
 */

public class EditSpotActivity extends AppCompatActivity {

    private SCFirebase scFirebase;
    private SCFirebaseAuth scAuth;

    private TextView addressView;
    private EditText editRate;


    private ParkingSpot spot;
    private boolean validRate = false;
    private int CODE_EDIT = 1;
    private int CODE_DELETE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_spot);

        // Initialize UI components

        addressView = findViewById(R.id.edit_spot_address_view);
        editRate = findViewById(R.id.edit_spot_rate_edit_text);
        editRate.addTextChangedListener(new MoneyTextWatcher(editRate));

        // Initialize SCFirebase instance
        scFirebase = new SCFirebase();
        scAuth = new SCFirebaseAuth();

        // Get current, logged in user
        final FirebaseUser currentUser = scAuth.getCurrentUser();


        // Get intent and extras
        Intent intent = getIntent();
        String spotID = intent.getStringExtra("spotID");

        // Get parking spot with ID from intent
        scFirebase.getParkingSpot(spotID, new SCFirebaseCallback<ParkingSpot>() {
            @Override
            public void callback(ParkingSpot data) {
                spot = data;

                final Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        addressView.setText(spot.getAddress());
                        editRate.setText(String.format(
                                "%s",
                                spot.formattedRate()
                        ));
                    }
                });

            }
        });

    }


    public void confirmChanges(View view) {
        String rawNewRate = editRate.getText().toString();
        double newRate = Double.parseDouble(rawNewRate.substring(1).replaceAll("[$+,+]", ""));
        if (newRate > 0 && newRate < 1000) {
            spot.setRate(newRate);
            scFirebase.updateSpot(spot.getSpotID(), spot);
            setResult(CODE_EDIT);
            finish();
        }

        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Error - Invalid Rate")
                    .setMessage("The new rate you entered is not valid. \n" + "Rate must be between 0 and 1000")
                    .setNegativeButton("Oops", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // do nothing
                        }
                    })
                    .setIcon(R.mipmap.spot_marker_icon)
                    .show();

        }


    }

    public void cancelChanges(View view) {
        finish();
    }

    public void deleteSpot(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm Spot Deletion")
                .setMessage(("Are you sure you want to delete this spot?\n\n" +
                        "You will not be able to undo this change."))
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(spot != null) {
                            scFirebase.deleteParkingSpot(spot.getSpotID());
                        }

                        Toast.makeText(
                                getApplicationContext(),
                                "Spot Deleted!",
                                Toast.LENGTH_SHORT).show();

                        setResult(CODE_DELETE);
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

    public void changeSpotPicture(View view){

    }


}