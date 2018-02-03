package com.ucsb.cs48.spotcheck;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(android.R.style.Theme_Material_NoActionBar_Fullscreen);
        setContentView(R.layout.splash);

        int secondsDelayed = 1;

       /* new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, secondsDelayed * 1000); */
    }

    public void goToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}