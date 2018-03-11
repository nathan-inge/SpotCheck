package com.ucsb.cs48.spotcheck;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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

import java.io.IOException;


public class CreateSpotEntry extends AppCompatActivity {


    private TextView placeText;
    private EditText rateInput;
    private ImageView spotImageView;

    private static final String TAG = "CreateSpotEntry";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private Place place;

    private boolean validPlace = false;
    private boolean validRate = false;
    private boolean validImage = false;

    private int PICK_IMAGE_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot_entry);

        rateInput = findViewById(R.id.rateEditText);
        placeText = findViewById(R.id.place_result_text);
        spotImageView = findViewById(R.id.spotImageView);

        rateInput.addTextChangedListener(new MoneyTextWatcher(rateInput));
    }


    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
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

        } else if (requestCode == PICK_IMAGE_REQUEST) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                spotImageView.setImageURI(data.getData());
                validImage = true;

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Unable to Upload Picture")
                    .setMessage(("Couldn't upload picture. Please select a different image."))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(R.mipmap.spot_marker_icon)
                    .show();
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

        if(!validPlace) {
            Toast.makeText(
                CreateSpotEntry.this,
                "Please select a valid spot location.",
                Toast.LENGTH_SHORT).show();

        } else if(!validRate) {
            Toast.makeText(
                CreateSpotEntry.this,
                R.string.rate_input_error,
                Toast.LENGTH_SHORT).show();

        } else if(!validImage) {
            Toast.makeText(
                CreateSpotEntry.this,
                "Please upload an image of the spot.",
                Toast.LENGTH_SHORT).show();

        } else {
            String address = place.getAddress().toString();

            LatLng latLng = place.getLatLng();
            SCLatLng scLatLng = new SCLatLng(latLng.latitude, latLng.longitude);

            ParkingSpot newSpot = new ParkingSpot(user.getUid(), address, scLatLng, rate);

            SCFirebase scFirebase = new SCFirebase();
            scFirebase.createNewSpot(newSpot);

            finish();
        }
    }

    public void changeSpotImageTapped(View view) {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Spot Image"),
            PICK_IMAGE_REQUEST
        );
    }

}
