package com.ucsb.cs48.spotcheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SCLatLng;
import com.ucsb.cs48.spotcheck.Utilities.MoneyTextWatcher;


public class CreateSpotEntry extends AppCompatActivity {


    private TextView placeText;
    private TextView rateErrorText;
    private EditText rateInput;

    private static final String TAG = "CreateSpotEntry";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private Place place;

    private boolean validPlace = false;
    private boolean validRate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot_entry);
        rateInput = findViewById(R.id.rateEditText);
        rateErrorText = findViewById(R.id.rate_error_text);
        placeText = findViewById(R.id.place_result_text);

        rateInput.addTextChangedListener(new MoneyTextWatcher(rateInput));
    }


    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                validPlace = true;
                placeText.setText(place.getAddress());
                placeText.setTextColor(0xff000000);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

                Log.i(TAG, status.getStatusMessage());

                validPlace = false;
                placeText.setText(R.string.place_error);
                placeText.setTextColor(0xff000000);

            }
        }
    }
    

    public void submitSpotButtonTapped(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Intent returnToSplash = new Intent(this, MainActivity.class);
            startActivity(returnToSplash);
            return;
        }

        if(place == null) { validPlace = false; }

        double rate = 0.0;
        String rawRateInput = rateInput.getText().toString();

        if((rawRateInput.length() > 0)) {
            double rawRate = Double.parseDouble(rawRateInput.substring(1));
            if(rawRate > 0) {
                validRate = true;
                rate = rawRate;
            }
        }

        if(validRate && validPlace) {
            String address = place.getAddress().toString();

            LatLng latLng = place.getLatLng();
            SCLatLng scLatLng = new SCLatLng(latLng.latitude, latLng.longitude);


            ParkingSpot newSpot = new ParkingSpot(user.getUid(), address, scLatLng, rate);

            SCFirebase scFirebase = new SCFirebase();
            scFirebase.createNewSpot(newSpot);

            Intent returnToMaps = new Intent(this, GoogleMapsActivity.class);
            startActivity(returnToMaps);
        } else {
            if(!validPlace) {
                placeText.setTextColor(0xffcc0000);

                placeText.setText(R.string.place_error);
            } else {
                rateErrorText.setText(R.string.rate_input_error);
            }
        }
    }
}
