package com.ucsb.cs48.spotcheck.ParkingSpotLists;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ucsb.cs48.spotcheck.R;

public class RentedSpotsFragment extends Fragment {

    public ListView rentedSpotsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rented_spots, container, false);

        rentedSpotsListView = view.findViewById(R.id.rented_spots_list);

        return view;
    }

}
