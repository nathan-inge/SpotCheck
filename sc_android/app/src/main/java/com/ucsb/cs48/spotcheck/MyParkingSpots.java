package com.ucsb.cs48.spotcheck;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import static com.ucsb.cs48.spotcheck.Utilities.SCConstants.*;

import com.google.firebase.auth.FirebaseUser;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseAuth;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;

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
        setContentView(R.layout.activity_my_parking_spots);


        scFirebase = new SCFirebase();
        scFirebaseAuth = new SCFirebaseAuth();
        currentUser = scFirebaseAuth.getCurrentUser();

        ownedParkingSpotsLV = findViewById(R.id.my_parking_spots);

        ownedParkingSpotsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), SpotDetailActivity.class);
                i.putExtra("spotID", usersParkingSpots.get(position).getSpotID());
                startActivityForResult(i, REQUEST_SPOT_DETAILS);
            }
        });


        final ProgressDialog dialog = ProgressDialog.show(MyParkingSpots.this, "",
            "Loading Spots...", true);
        scFirebase.getUsersParkingSpots(currentUser.getUid(), new SCFirebaseCallback<ArrayList<ParkingSpot>>() {
            @Override
            public void callback(ArrayList<ParkingSpot> data) {
               dialog.dismiss();
               if(data != null) {
                   usersParkingSpots = data;


                   final Handler mainHandler = new Handler(Looper.getMainLooper());
                   mainHandler.post(new Runnable() {
                       @Override
                       public void run() {
                           String[] allSpots = new String[usersParkingSpots.size()];
                           for (int i=0; i < usersParkingSpots.size(); i++)
                               allSpots[i] = usersParkingSpots.get(i).getAddress();

                           ArrayAdapter adapter = new ArrayAdapter<>(MyParkingSpots.this,
                                   R.layout.activity_listview, allSpots);
                           ownedParkingSpotsLV.setAdapter(adapter);
                       }
                   });
               }
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
