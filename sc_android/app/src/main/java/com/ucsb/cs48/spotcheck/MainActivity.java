package com.ucsb.cs48.spotcheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(android.R.style.Theme_Material_NoActionBar_Fullscreen);
        setContentView(R.layout.activity_main);

    }

    //Intent register = new Intent(this, RegisterActivity.class);
    public void goToRegisterActivity(View view) {
        Intent register = new Intent(this, RegisterActivity.class);
        startActivity(register);
    }

    public void goToLoginActivity(View view) {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }

    public void goToGoogleMapsActivity(View view) {
        Intent maps = new Intent(this, GoogleMapsActivity.class);
        startActivity(maps);
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this app
     */
    public native String stringFromJNI();
}
