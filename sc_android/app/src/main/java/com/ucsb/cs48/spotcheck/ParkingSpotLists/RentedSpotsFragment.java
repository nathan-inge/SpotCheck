package com.ucsb.cs48.spotcheck.ParkingSpotLists;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.ucsb.cs48.spotcheck.SCLocalObjects.BlockedDates;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;
import com.ucsb.cs48.spotcheck.SpotDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.ucsb.cs48.spotcheck.Utilities.SCConstants.REQUEST_SPOT_DETAILS;

public class RentedSpotsFragment extends Fragment {

    public ListView rentedSpotsListView;

    private SCFirebase scFirebase;
    private SCFirebaseAuth scFirebaseAuth;

    private FirebaseUser currentUser;

    private ArrayList<ParkingSpot> usersParkingSpots = new ArrayList<>();

    private Map<ParkingSpot, BlockedDates> spotBlockedDatesMap;

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

                ParkingSpot spot = usersParkingSpots.get(position);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E M/d, h:mm a");
                Date startDate = spotBlockedDatesMap.get(spot).getStartDate();
                String startString = simpleDateFormat.format(startDate);

                Date endDate = spotBlockedDatesMap.get(spot).getEndDate();
                String endString = simpleDateFormat.format(endDate);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Rental Details")
                    .setMessage(("Start Time: " + startString + "\n\nEnd Time: " + endString))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(R.mipmap.spot_marker_icon)
                    .show();
            }
        });

        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
            "Loading Spots...", true);

        scFirebase.getSCUser(currentUser.getUid(), new SCFirebaseCallback<SpotCheckUser>() {
            @Override
            public void callback(SpotCheckUser data) {
                scFirebase.getRentedParkingSpots(data, new SCFirebaseCallback<Map<ParkingSpot, BlockedDates>>(){
                    @Override
                    public void callback(Map<ParkingSpot, BlockedDates> data) {
                        dialog.dismiss();
                        if(data != null) {
                            usersParkingSpots.addAll(data.keySet());
                            spotBlockedDatesMap = data;

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
            }
        });

        return view;
    }

}
