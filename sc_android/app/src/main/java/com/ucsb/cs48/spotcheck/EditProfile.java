package com.ucsb.cs48.spotcheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;


public class EditProfile extends AppCompatActivity {

    SpotCheckUser user;
    EditText editName;
    SCFirebase scFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        scFirebase = new SCFirebase();

        Intent intent = getIntent();
        user = intent.getParcelableExtra("currentSCUser");

        editName = findViewById(R.id.edit_user_name);

        if(user != null) {
            editName.setHint(user.getFullname());
        }
    }

    public void cancel(View view){
        finish();
    }

    public void confirm(View view){
        //Change user's screen name, profile pic, and location in firebase
        String rawNewName = editName.getText().toString();

        if((rawNewName.length()) > 0) {
            scFirebase.uploadUser(user);
        }

        finish();
    }
}
