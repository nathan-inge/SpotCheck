package com.ucsb.cs48.spotcheck;

import com.ucsb.cs48.spotcheck.SCLocalObjects.BlockedDates;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SCLatLng;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit tests for ParkingSpot
 */
public class ParkingSpotTest {

    @Test
    public void firebase_constructor() {
        SCLatLng testLatLng = new SCLatLng(13.4, -35.73);
        ParkingSpot parkingSpot = new ParkingSpot(
            "ownerID",
            "home",
            testLatLng,
            12.5);
        assertEquals("ownerID", parkingSpot.getOwnerID());
        assertEquals("home", parkingSpot.getAddress());
        assertEquals(testLatLng, parkingSpot.getLatLng());
        assertEquals(12.5, parkingSpot.getRate(), 0.0);
    }

    @Test
    public void all_args_spotConstrutor() {
        SCLatLng testLatLng = new SCLatLng(13.4, -35.73);
        ParkingSpot parkingSpot = new ParkingSpot(
            "spotID",
            "ownerID",
            "home",
            testLatLng,
            12.5
        );
        assertEquals("spotID", parkingSpot.getSpotID());
        assertEquals("ownerID", parkingSpot.getOwnerID());
        assertEquals("home", parkingSpot.getAddress());
        assertEquals(testLatLng, parkingSpot.getLatLng());
        assertEquals(12.5, parkingSpot.getRate(), 0.0);
    }

    @Test
    public void formatted_rate() {
        SCLatLng testLatLng = new SCLatLng(13.4, -35.73);
        ParkingSpot parkingSpot = new ParkingSpot(
            "spotID",
            "ownerID",
            "home",
            testLatLng,
            25.95
        );

        assertEquals("$25.95", parkingSpot.formattedRate());

        parkingSpot.setRate(0.01);
        assertEquals("$0.01", parkingSpot.formattedRate());

        parkingSpot.setRate(0d);
        assertEquals("$0.00", parkingSpot.formattedRate());

        parkingSpot.setRate(9.5);
        assertEquals("$9.50", parkingSpot.formattedRate());
    }

    @Test
    public void test_blockedDates() {
        SCLatLng testLatLng = new SCLatLng(13.4, -35.73);
        ParkingSpot parkingSpot = new ParkingSpot(
            "spotIDBlockedDates",
            "ownerID",
            "home",
            testLatLng,
            25.95
        );

        assertEquals(0, parkingSpot.getBlockedDatesCount());

        Date startDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, 3);
        Date endDate = calendar.getTime();

        BlockedDates blockedDatesAdd = new BlockedDates(startDate.getTime(), endDate.getTime());

        parkingSpot.addBlockedDates(blockedDatesAdd);

        assertEquals(1, parkingSpot.getBlockedDatesCount());

        ArrayList<BlockedDates> blockedDatesList= parkingSpot.getBlockedDatesList();
        assertEquals(blockedDatesList.get(0), blockedDatesAdd);

        BlockedDates blockedDatesRemove = new BlockedDates(startDate.getTime(), endDate.getTime());
        parkingSpot.removeBlockedDates(blockedDatesRemove);

        assertEquals(0, parkingSpot.getBlockedDatesCount());
    }
}
