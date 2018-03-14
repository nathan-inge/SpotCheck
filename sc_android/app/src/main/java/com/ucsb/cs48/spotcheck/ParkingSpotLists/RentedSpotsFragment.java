package com.ucsb.cs48.spotcheck.ParkingSpotLists;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.ucsb.cs48.spotcheck.R;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseAuth;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;
import com.ucsb.cs48.spotcheck.SpotDetailActivity;

import java.util.ArrayList;

import static com.ucsb.cs48.spotcheck.Utilities.SCConstants.REQUEST_SPOT_DETAILS;

public class RentedSpotsFragment extends Fragment {

    public ListView rentedSpotsListView;

    private SCFirebase scFirebase;
    private SCFirebaseAuth scFirebaseAuth;

    private FirebaseUser currentUser;

    private ArrayList<ParkingSpot> usersParkingSpots;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rented_spots, container, false);

        rentedSpotsListView = view.findViewById(R.id.rented_spots_list);

        scFirebase = new SCFirebase();
        scFirebaseAuth = new SCFirebaseAuth();
        currentUser = scFirebaseAuth.getCurrentUser();

        rentedSpotsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), SpotDetailActivity.class);
                i.putExtra("spotID", usersParkingSpots.get(position).getSpotID());
                startActivityForResult(i, REQUEST_SPOT_DETAILS);
            }
        });

        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
            "Loading Spots...", true);

        scFirebase.getSCUser(currentUser.getUid(), new SCFirebaseCallback<SpotCheckUser>() {
            @Override
            public void callback(SpotCheckUser data) {
                try {
                    scFirebase.getRentedParkingSpots(data, new SCFirebaseCallback<ArrayList<ParkingSpot>>(){
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

                                        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),
                                            R.layout.activity_listview, allSpots);
                                        rentedSpotsListView.setAdapter(adapter);
                                    }
                                });
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

}
