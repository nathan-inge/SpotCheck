package com.ucsb.cs48.spotcheck;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;


public class EditProfile extends AppCompatActivity {

    private SpotCheckUser user;
    private String currentSCUserID;
    private EditText editName;
    private SCFirebase scFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        scFirebase = new SCFirebase();

        editName = findViewById(R.id.edit_user_name);

        Intent intent = getIntent();
        currentSCUserID = intent.getStringExtra("currentSCUserID");

        scFirebase.getSCUser(currentSCUserID, new SCFirebaseCallback<SpotCheckUser>() {
            @Override
            public void callback(SpotCheckUser data) {
                if(data != null) {
                    user = data;
                    user.setUserID(currentSCUserID);

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            editName.setHint(user.getFullname());
                        }
                    });
                }
            }
        });
    }

    public void cancel(View view){
        finish();
    }

    public void confirm(View view){
        //Change user's screen name, profile pic, and location in firebase
        String rawNewName = editName.getText().toString();

        if((rawNewName.length()) > 0) {
            user.setFullname(rawNewName);
            scFirebase.uploadUser(user);
        }

        finish();
    }
}