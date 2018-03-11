package com.ucsb.cs48.spotcheck;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseUser;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseAuth;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;


public class SpotDetailActivity extends AppCompatActivity {

    private SCFirebase scFirebase;
    private SCFirebaseAuth scAuth;
    private TextView addressView;
    private TextView rateView;
    private Button rentButton;
    private ImageView spotImage;
    private ProgressBar spotImageProgress;

    private SpotCheckUser owner;
    private ParkingSpot spot;
    private boolean isOwner = false;

    private boolean openedMailClient = false;
    private int CODE_SEND = 0;
    private int CODE_EDIT = 1;
    private int CODE_DELETE = 2;
    private ProgressDialog startingEmailDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);

        // Initialize UI components
        addressView = findViewById(R.id.spot_detail_address_view);
        rateView = findViewById(R.id.spot_detail_rate_view);
        rentButton = findViewById(R.id.spot_details_rent_button);
        spotImage = findViewById(R.id.spotImageDetail);
        spotImageProgress = findViewById(R.id.spotImageProgress);

        // Initialize SCFirebase instance
        scFirebase = new SCFirebase();
        scAuth = new SCFirebaseAuth();

        // Get current, logged in user
        final FirebaseUser currentUser = scAuth.getCurrentUser();

        final ProgressDialog dialog = ProgressDialog.show(
            SpotDetailActivity.this,
            "",
            "Fetching spot details...",
            true
        );

        // Get intent and extras
        Intent intent = getIntent();
        String spotID = intent.getStringExtra("spotID");

        // Get parking spot with ID from intent
        scFirebase.getParkingSpot(spotID, new SCFirebaseCallback<ParkingSpot>() {
            @Override
            public void callback(ParkingSpot data) {
                dialog.dismiss();
                if(data != null) {
                    spot = data;


                    final Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (spot.getOwnerID().equals(currentUser.getUid())) {
                                isOwner = true;
                                rentButton.setText(R.string.edit_spot_button);
                            }

                            addressView.setText(spot.getAddress());
                            rateView.setText(String.format(
                                "%s%s",
                                spot.formattedRate(),
                                getString(R.string.per_hour)
                            ));

                            if (spot.getImageUrl() != null) {
                                Uri spotImageUri = Uri.parse(spot.getImageUrl());
                                Glide.with(SpotDetailActivity.this).load(spotImageUri).apply(new RequestOptions()
                                .fitCenter()).listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        spotImage.setImageResource(R.mipmap.spot_marker_icon);
                                        spotImageProgress.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        spotImageProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                }).into(spotImage);
                            } else {
                                spotImage.setImageResource(R.mipmap.spot_marker_icon);
                                spotImageProgress.setVisibility(View.GONE);
                            }
                        }
                    });

                } else {
                    showSpotNotAvailableDialog();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        openedMailClient = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        openedMailClient = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        startingEmailDialog.dismiss();
        if(requestCode == CODE_SEND && openedMailClient){
            Toast.makeText(
                getApplicationContext(),
                "Spot Successfully Rented!",
                Toast.LENGTH_SHORT).show();
             rentSpot();

        }
        else if (resultCode == CODE_EDIT) {
            // do nothing
        }
        else if (resultCode == CODE_DELETE) {
            finish();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Rent Request Cancelled")
                .setMessage((
                    "Your request has been cancelled. You must send the owner an email.\n\n" +
                    "Would you like to try again?"))
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendOwnerEmail();
                    }
                })
                .setNegativeButton("Cancel Request", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(R.mipmap.spot_marker_icon)
                .show();
        }
    }

    public void rentButtonClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (isOwner) {
            Intent i = new Intent(this, EditSpotActivity.class);
            i.putExtra("spotID", spot.getSpotID());
            startActivity(i);

        }
        else {
            builder.setTitle("Confirm Rent Request")
                    .setMessage(("Are you sure you want to request to rent this spot?\n\n" +
                            "You will be directed to a screen to send an email to the owner."))
                    .setPositiveButton("Rent", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getOwnerInfo();
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

    public void showSpotNotAvailableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Spot Unavailable!")
            .setMessage(("Sorry, but this parking spot is currently unavailable."))
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            })
            .setIcon(R.mipmap.spot_marker_icon)
            .show();
    }

    public void getOwnerInfo() {
        final ProgressDialog dialog = ProgressDialog.show(SpotDetailActivity.this, "",
            "Getting owner info...", true);

        scFirebase.getSCUser(spot.getOwnerID(), new SCFirebaseCallback<SpotCheckUser>() {
            @Override
            public void callback(SpotCheckUser data) {
                owner = data;

                final Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (owner != null) {
                            sendOwnerEmail();
                            rentSpot();

                        } else {
                            Toast.makeText(
                                getApplicationContext(),
                                "Error getting owner information.",
                                Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });
    }

    public void sendOwnerEmail() {
        startingEmailDialog = ProgressDialog.show(SpotDetailActivity.this, "",
            "Setting up email...", true);

        String currentUserName = scAuth.getCurrentUser().getDisplayName();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{owner.getEmail()});
        i.putExtra(Intent.EXTRA_SUBJECT, "SpotCheck Rent Request");
        i.putExtra(
            Intent.EXTRA_TEXT   ,
            "Hello,\n" + currentUserName
                + " would like to rent your parking spot located at " + spot.getAddress()
                + " from START TIME until END TIME.\n\n"
                + "Please confirm this request by replying to this email.\n\n"
                + "Payment should be arranged directly with " + currentUserName + "\n\n"
                + "Thank you,\nThe SpotCheck Team"
        );

        try {
            startActivityForResult(
                Intent.createChooser(i, "Send rent request..."),
                0
            );

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(
                getApplicationContext(),
                "There are no email clients installed.",
                Toast.LENGTH_SHORT
            ).show();
        }
    }

    public void rentSpot() {

    }



}
