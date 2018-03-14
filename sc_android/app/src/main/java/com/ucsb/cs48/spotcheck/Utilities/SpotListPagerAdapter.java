package com.ucsb.cs48.spotcheck.Utilities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ucsb.cs48.spotcheck.ParkingSpotLists.OwnedSpotsFragment;
import com.ucsb.cs48.spotcheck.ParkingSpotLists.RentedSpotsFragment;
import com.ucsb.cs48.spotcheck.SpotDetailActivity;


public class SpotListPagerAdapter extends FragmentStatePagerAdapter {

    private int numTabs;

    public SpotListPagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OwnedSpotsFragment();

            case 1:
                return new RentedSpotsFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
