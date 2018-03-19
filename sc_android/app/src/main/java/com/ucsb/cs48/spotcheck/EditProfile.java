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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;

import java.io.IOException;

import static com.ucsb.cs48.spotcheck.Utilities.SCConstants.PROFILE_EDITED;
import static com.ucsb.cs48.spotcheck.Utilities.SCConstants.REQUEST_PICK_IMAGE;
import static com.ucsb.cs48.spotcheck.Utilities.SCConstants.SPOT_EDITED;


public class EditProfile extends AppCompatActivity {

    private SpotCheckUser user;
    private String currentSCUserID;
    private EditText editName;
    private EditText editLocation;
    private SCFirebase scFirebase;

    private ProgressBar profileImageEditProgress;
    private ImageView profileImageEdit;

    private boolean newImageSet = false;
    private Bitmap newBitmapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        scFirebase = new SCFirebase();

        editName = findViewById(R.id.edit_user_name);
        editLocation = findViewById(R.id.edit_user_location);
        profileImageEdit = findViewById(R.id.profileImageEdit);
        profileImageEditProgress = findViewById(R.id.profileImageEditProgress);

        Intent intent = getIntent();
        currentSCUserID = intent.getStringExtra("currentSCUserID");

        scFirebase.getSCUser(currentSCUserID, new SCFirebaseCallback<SpotCheckUser>() {
            @Override
            public void callback(SpotCheckUser data) {
                if (data != null) {
                    user = data;

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            editName.setHint(user.getFullname());

                            String locationHint =
                                    (user.getLocation().isEmpty()) ? "Location" : user.getLocation();
                            editLocation.setHint(locationHint);


                            if (user.getImageUrl() != null) {
                                Uri userImageUri = Uri.parse(user.getImageUrl());
                                Glide.with(EditProfile.this).load(userImageUri).apply(new RequestOptions()
                                        .fitCenter()).listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        profileImageEdit.setImageResource(R.mipmap.spot_marker_icon);
                                        profileImageEditProgress.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        profileImageEditProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                }).into(profileImageEdit);
                            } else {
                                profileImageEdit.setImageResource(R.mipmap.spot_marker_icon);
                                profileImageEditProgress.setVisibility(View.GONE);
                            }

                        }
                    });
                }
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
                    profileImageEdit.setImageBitmap(newBitmapImage);
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

    public void changeProfilePicture(View view) {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"),
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

    public void cancel(View view) {
        finish();
    }

    public void confirm(View view) {
        //Change user's screen name, profile pic, and location in firebase
        String rawNewName = editName.getText().toString();
        String rawNewLocation = editLocation.getText().toString();

        boolean newNameSet = false;
        boolean newLocationSet = false;

        if (((rawNewName.length()) > 0) && (rawNewName != user.getFullname())) {
            newNameSet = true;
            user.setFullname(rawNewName);
            scFirebase.uploadUser(user);
        }
        if (((rawNewLocation.length()  > 0) && (rawNewLocation != user.getLocation()))) {
            newLocationSet = true;
            user.setLocation(rawNewLocation);
            scFirebase.uploadUser(user);
        }
        if (newImageSet) {
            final ProgressDialog dialog = ProgressDialog.show(EditProfile.this, "",
                    "Saving Edits...", true);

            scFirebase.uploadProfileImage(user.getUserID(), newBitmapImage, new SCFirebaseCallback<Uri>() {
                @Override
                public void callback(Uri data) {
                    dialog.dismiss();
                    if (data != null) {
                        user.setImageUrl(data.toString());
                        scFirebase.uploadUser(user);
                        setResult(PROFILE_EDITED);

                        finish();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);

                        builder.setTitle("Unable to Change Profile Picture")
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
            if(newNameSet || newLocationSet) {
                setResult(PROFILE_EDITED);
            }
            finish();
        }
    }
}
