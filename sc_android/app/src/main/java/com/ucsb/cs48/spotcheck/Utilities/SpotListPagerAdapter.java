package com.ucsb.cs48.spotcheck.Utilities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.ListView;

import com.ucsb.cs48.spotcheck.ParkingSpotLists.OwnedSpotsFragment;
import com.ucsb.cs48.spotcheck.ParkingSpotLists.RentedSpotsFragment;
import com.ucsb.cs48.spotcheck.SpotDetailActivity;


public class SpotListPagerAdapter extends FragmentStatePagerAdapter {

    public OwnedSpotsFragment ownedSpotsFragment;
    public RentedSpotsFragment rentedSpotsFragment;

    private int numTabs;

    public SpotListPagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.ownedSpotsFragment = new OwnedSpotsFragment();
        this.rentedSpotsFragment = new RentedSpotsFragment();
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return this.ownedSpotsFragment;

            case 1:
                return this.rentedSpotsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
