package com.ucsb.cs48.spotcheck;

import com.google.android.gms.maps.model.LatLng;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SCLatLng;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for ParkingSpot
 */
public class SCLatLngTest {
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

        SCLatLng scLatLngThree = new SCLatLng(-223.4, -244.2);
        assertFalse(scLatLngOne.equals(scLatLngThree));
    }
}
