package com.ucsb.cs48.spotcheck.ParkingSpotLists;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static com.ucsb.cs48.spotcheck.Utilities.SCConstants.*;

import com.google.firebase.auth.FirebaseUser;
import com.ucsb.cs48.spotcheck.R;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseAuth;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;
import com.ucsb.cs48.spotcheck.SpotDetailActivity;
import com.ucsb.cs48.spotcheck.Utilities.SpotListPagerAdapter;

import java.util.ArrayList;

public class MyParkingSpots extends AppCompatActivity {

    private SpotCheckUser user;
    private SCFirebase scFirebase;
    private SCFirebaseAuth scFirebaseAuth;
    private FirebaseUser currentUser;

    private ListView ownedParkingSpotsLV;

    private ArrayList<ParkingSpot> usersParkingSpots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout_spots);
        getSupportActionBar().setElevation(0);


        scFirebase = new SCFirebase();
        scFirebaseAuth = new SCFirebaseAuth();
        currentUser = scFirebaseAuth.getCurrentUser();


        TabLayout tabLayout = findViewById(R.id.spots_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Owned Spots"));
        tabLayout.addTab(tabLayout.newTab().setText("Rented Spots"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.spots_view_pager);
        final SpotListPagerAdapter adapter = new SpotListPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == SPOT_DELETED) {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0,0);
        }
    }
}
