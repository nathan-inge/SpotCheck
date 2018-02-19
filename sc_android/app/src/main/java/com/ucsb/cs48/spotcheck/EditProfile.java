package com.ucsb.cs48.spotcheck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class EditProfile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    public void cancel(View view){
        finish();
    }

    public void confirm(View view){
        //Change user's screen name, profile pic, and location in firebase
        finish();
    }
}
