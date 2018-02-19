package com.ucsb.cs48.spotcheck;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseAuth;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;


public class ProfilePage extends AppCompatActivity {

    private SpotCheckUser user;
    private TextView userNameTextView;
    private SCFirebase scFirebase;
    private SCFirebaseAuth scFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize interface objects
        scFirebase = new SCFirebase();
        scFirebaseAuth = new SCFirebaseAuth();

        // Initialize UI objects
        userNameTextView = findViewById(R.id.user_name_view);

        if(scFirebaseAuth.getCurrentUser() != null) {
            final String currentUserID = scFirebaseAuth.getCurrentUser().getUid();

            scFirebase.getSCUser(currentUserID, new SCFirebaseCallback<SpotCheckUser>() {
                @Override
                public void callback(SpotCheckUser data) {
                    if(data != null) {
                        user = data;
                        user.setUserID(currentUserID);

                        Handler mainHandler = new Handler(Looper.getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                userNameTextView.setText(user.getFullname());
                            }
                        });
                    }
                }
            });
        }
    }

    public void editProfile(View view){
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

}
