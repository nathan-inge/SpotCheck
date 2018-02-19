package com.ucsb.cs48.spotcheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class ProfilePage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void editProfile(View view){
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

}
