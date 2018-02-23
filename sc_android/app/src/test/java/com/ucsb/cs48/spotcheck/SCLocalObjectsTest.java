package com.ucsb.cs48.spotcheck;

import com.google.android.gms.maps.model.LatLng;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SCLatLng;
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


    /// MARK - SpotCheckUser Unit Tests
    @Test
    public void all_args_userConstructor() {
        SpotCheckUser spotCheckUser = new SpotCheckUser(
            "userID",
            "email",
            "Full Name",
                "location"
        );

        assertEquals("userID", spotCheckUser.getUserID());
        assertEquals("email", spotCheckUser.getEmail());
        assertEquals("Full Name", spotCheckUser.getFullname());
        assertEquals("location", spotCheckUser.getLocation());
    }


    /// MARK - SCLatLng Unit Tests
    @Test
    public void convert_to_google_latlng() {
        double latitude = 16.02;
        double longitude = -102.4;

        SCLatLng scLatLng = new SCLatLng(latitude, longitude);
        LatLng googlelatLng = new LatLng(latitude, longitude);

        assertEquals(scLatLng.getLatitude(), googlelatLng.latitude, 0.0);
        assertEquals(scLatLng.getLongitude(), googlelatLng.longitude, 0.0);

        LatLng convertedSCLatLng = scLatLng.convertToGoogleLatLng();

        assertTrue(convertedSCLatLng.equals(googlelatLng));
    }

    @Test
    public void overridden_equals() {
        double latitude = 100.42;
        double longitude = -349.3;

        SCLatLng scLatLngOne = new SCLatLng(latitude, longitude);
        SCLatLng scLatLngTwo = new SCLatLng(latitude, longitude);

        assertTrue(scLatLngOne.equals(scLatLngTwo));

        LatLng latLng = new LatLng(latitude, longitude);
        assertFalse(scLatLngOne.equals(latLng));

        SCLatLng scLatLngThree = new SCLatLng(-223.4, -244.2);
        assertFalse(scLatLngOne.equals(scLatLngThree));
    }
}
