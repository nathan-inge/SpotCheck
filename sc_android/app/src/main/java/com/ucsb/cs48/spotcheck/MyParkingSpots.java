package com.ucsb.cs48.spotcheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseAuth;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;

public class MyParkingSpots extends AppCompatActivity {

    private SpotCheckUser user;
    private SCFirebase scFirebase;
    private SCFirebaseAuth scFirebaseAuth;
    private String spot_name;
    private String currentSCUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_parking_spots);


        scFirebase = new SCFirebase();
        scFirebaseAuth = new SCFirebaseAuth();

        Intent intent = getIntent();
        currentSCUserID = intent.getStringExtra("currentSCUserID");

//        ArrayAdapter adapter = new ArrayAdapter<String>(this,
//                R.layout.activity_listview, mobileArray);
//
//        ListView listView = (ListView) findViewById(R.id.mobile_list);
//        listView.setAdapter(adapter);

        scFirebase.getSCUser(currentSCUserID, new SCFirebaseCallback<SpotCheckUser>() {
            @Override
            public void callback(SpotCheckUser data) {
                if(data != null) {
                    user = data;
                }
            }
        });

//        ArrayAdapter adapter = new ArrayAdapter<String>(this,
//                R.layout.activity_listview, user.getCurrentListings());
//
//        ListView listView = (ListView) findViewById(R.id.my_parking_spots);
//        listView.setAdapter(adapter);


    }
}
