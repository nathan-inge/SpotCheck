package com.ucsb.cs48.spotcheck;

import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for SCLocalObjects (user and parking spot)
 */
public class SCLocalObjectsTest {

    /// MARK - ParkingSpot Unit Tests
    @Test
    public void firebase_constructor() {
        ParkingSpot parkingSpot = new ParkingSpot("ownerID", "home", 12.5);
        assertEquals("ownerID", parkingSpot.getOwnerID());
        assertEquals("home", parkingSpot.getAddress());
        assertEquals(12.5, parkingSpot.getRate(), 0.0);
    }

    @Test
    public void all_args_spotConstrutor() {
        ParkingSpot parkingSpot = new ParkingSpot(
            "spotID",
            "ownerID",
            "home",
            12.5
        );
        assertEquals("spotID", parkingSpot.getSpotID());
        assertEquals("ownerID", parkingSpot.getOwnerID());
        assertEquals("home", parkingSpot.getAddress());
        assertEquals(12.5, parkingSpot.getRate(), 0.0);
    }


    // MARK - SpotCheckUser Unit Tests
    @Test
    public void all_args_userConstructor() {
        SpotCheckUser spotCheckUser = new SpotCheckUser(
            "userID",
            "email",
            "Full Name"
        );

        assertEquals("userID", spotCheckUser.getUserID());
        assertEquals("email", spotCheckUser.getEmail());
        assertEquals("Full Name", spotCheckUser.getFullname());
    }
}
