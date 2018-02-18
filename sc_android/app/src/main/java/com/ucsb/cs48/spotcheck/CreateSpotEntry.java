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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateSpotEntry extends AppCompatActivity {


    TextView placeText;
    TextView rateErrorText;
    EditText rateInput;
    private static final String TAG = "CreateSpotEntry";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Place place;
    boolean validPlace = false;

    private DatabaseReference spotDatabase;
//    PlaceAutocompleteFragment autocompleteFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot_entry);
        rateInput = findViewById(R.id.rateEditText);
        rateErrorText = findViewById(R.id.rate_error_text);
        placeText = findViewById(R.id.place_result_text);

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
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    public void submitSpotButtonTapped(View view) {
        double rate = 0;
        if (rateInput.length() > 0) {
            rate = Double.parseDouble(rateInput.getText().toString());
        }
        if (rate > 0 && validPlace) {
            String address = place.getAddress().toString();
            LatLng latLng = place.getLatLng();

            spotDatabase = FirebaseDatabase.getInstance().getReference();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            ParkingSpot newSpot = new ParkingSpot(user.getUid(), address, latLng, rate);
            spotDatabase.child("parking_spots").child(newSpot.getSpotID()).setValue(newSpot);

            Intent returnToMaps = new Intent(this, GoogleMapsActivity.class);
            startActivity(returnToMaps);
        }
        else {
            if (!validPlace) {
                placeText.setTextColor(0xffcc0000);
                placeText.setText("Not a valid Place");
            }
            rateErrorText.setText("Invalid Rate: Must be greater than zero");
        }

    }



}
