package com.ucsb.cs48.spotcheck;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;


public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private SCFirebase scFirebase;

    private SpotCheckUser user;

    private DrawerLayout mDrawerLayout;
    private TextView mUserName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_google_maps);

        setNavigationViewListner();

        scFirebase = new SCFirebase();
        mAuth = FirebaseAuth.getInstance();

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mUserName = findViewById(R.id.display_name);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
                if (userFirebase != null) {
                    // User is signed in - do nothing, stay in main view
                    scFirebase.getSCUser(userFirebase.getUid(), new SCFirebaseCallback<SpotCheckUser>() {
                        @Override
                        public void callback(SpotCheckUser data) {
                            if(data != null) {
                                user = data;
                                user.setUserID(userFirebase.getUid());

                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mUserName.setText(user.getFullname());
                                    }
                                });
                            }
                        }
                    });

                } else {
                    // No user is signed in, go to splash screen
                    goToSplashScreen();
                }
            }
        };



        // Construct a GeoDataClient.
       // mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
       // mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
       // mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



/**
 * Manipulates the map once available.
 * This callback is triggered when the map is ready to be used.
 * This is where we can add markers or lines, add listeners or move the camera. In this case,
 * we just add a marker near Sydney, Australia.
 * If Google Play services is not installed on the device, the user will be prompted to install
 * it inside the SupportMapFragment. This method will only be triggered once the user has
 * installed Google Play services and returned to the app.
 */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng IV = new LatLng(34.412609, -119.861433);
        mMap.addMarker(new MarkerOptions().position(IV).title("Marker in Isla Vista"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(IV));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.profile_page_button: {
                viewProfileButtonClicked();
                break;
            }

            case R.id.logout_button: {
                mAuth.signOut();
                break;
            }

            case R.id.create_spot_entry_button: {
                Intent create_spot_entry = new Intent(this, CreateSpotEntry.class);
                startActivity(create_spot_entry);
            }
        }
        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationViewListner() {
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void goToSplashScreen() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void menuButtonClicked(View view) {
        mDrawerLayout.openDrawer(Gravity.START);
    }

    public void viewProfileButtonClicked() {
        Intent i = new Intent(this, ProfilePage.class);
        startActivity(i);
    }
}
