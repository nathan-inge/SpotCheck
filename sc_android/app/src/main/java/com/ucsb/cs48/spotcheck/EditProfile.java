package com.ucsb.cs48.spotcheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by labas on 2/13/2018.
 */

public class EditProfile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    public void cancel(View view){
        Intent intent = new Intent(this, ProfilePage.class);
        startActivity(intent);
    }

    public void confirm(View view){
        //Change user's screen name, profile pic, and location in firebase
        Intent intent = new Intent(this, ProfilePage.class);
        startActivity(intent);
    }
}
