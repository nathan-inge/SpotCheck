package com.ucsb.cs48.spotcheck;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import static com.ucsb.cs48.spotcheck.Utilities.SCConstants.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseUser;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseAuth;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.Utilities.MoneyTextWatcher;

import java.io.IOException;


public class EditSpotActivity extends AppCompatActivity {

    private SCFirebase scFirebase;
    private SCFirebaseAuth scAuth;

    private TextView addressView;
    private EditText editRate;
    private ProgressBar spotImageEditProgress;
    private ImageView spotImageEdit;

    private boolean newImageSet = false;
    private Bitmap newBitmapImage;

    private ParkingSpot spot;
    private boolean validRate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_spot);

        // Initialize UI components

        addressView = findViewById(R.id.edit_spot_address_view);
        editRate = findViewById(R.id.edit_spot_rate_edit_text);
        editRate.addTextChangedListener(new MoneyTextWatcher(editRate));
        spotImageEdit = findViewById(R.id.spotImageEdit);
        spotImageEditProgress = findViewById(R.id.spotImageEditProgress);

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

                        if (spot.getImageUrl() != null) {
                            Uri spotImageUri = Uri.parse(spot.getImageUrl());
                            Glide.with(EditSpotActivity.this).load(spotImageUri).apply(new RequestOptions()
                                .fitCenter()).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    spotImageEdit.setImageResource(R.mipmap.spot_marker_icon);
                                    spotImageEditProgress.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    spotImageEditProgress.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(spotImageEdit);
                        } else {
                            spotImageEdit.setImageResource(R.mipmap.spot_marker_icon);
                            spotImageEditProgress.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICK_IMAGE) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                try {
                    newBitmapImage = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), data.getData());
                    spotImageEdit.setImageBitmap(newBitmapImage);
                    newImageSet = true;

                } catch (IOException e) {
                    newImageSet = false;
                    e.printStackTrace();
                    showUploadError();
                }

            } else if (resultCode != RESULT_CANCELED) {
                showUploadError();
            }
        }
    }


    public void confirmChanges(View view) {
        boolean newRateSet = false;
        String rawNewRate = editRate.getText().toString();
        double newRate = Double.parseDouble(rawNewRate.substring(1).replaceAll("[$+,+]", ""));

        if ((newRate > 0 && newRate < 1000) && (newRate != spot.getRate())) {
            newRateSet = true;
            spot.setRate(newRate);
            scFirebase.updateSpot(spot.getSpotID(), spot);

        } else if (newRate != spot.getRate()) {
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

        if(newImageSet) {
            final ProgressDialog dialog = ProgressDialog.show(EditSpotActivity.this, "",
                "Saving Edits...", true);

            scFirebase.uploadSpotImage(spot.getSpotID(), newBitmapImage, new SCFirebaseCallback<Uri>() {
                @Override
                public void callback(Uri data) {
                    dialog.dismiss();
                    if (data != null) {
                        spot.setImageUrl(data.toString());
                        scFirebase.updateSpot(spot.getSpotID(), spot);

                        setResult(SPOT_EDITED);
                        finish();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditSpotActivity.this);

                        builder.setTitle("Unable to Change Spot Picture")
                            .setMessage(("Please check your internet connection and try again."))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(R.mipmap.spot_marker_icon)
                            .show();
                    }
                }
            });
        } else {
            if (newRateSet) {
                setResult(SPOT_EDITED);
            }
            finish();
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

                        setResult(SPOT_DELETED);
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
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Spot Image"),
            REQUEST_PICK_IMAGE
        );

    }

    private void showUploadError() {
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