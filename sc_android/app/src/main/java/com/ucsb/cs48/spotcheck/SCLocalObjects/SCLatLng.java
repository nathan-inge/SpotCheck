package com.ucsb.cs48.spotcheck.SCLocalObjects;

import com.google.android.gms.maps.model.LatLng;

public class SCLatLng {

    private double latitude;
    private double longitude;

    public SCLatLng() {
        // Empty constructor required for Firebase gets
    }

    public SCLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatLng convertToGoogleLatLng() {
        com.google.android.gms.maps.model.LatLng mapsLatLng =
            new com.google.android.gms.maps.model.LatLng(this.latitude,
                this.longitude);

        return mapsLatLng;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!SCLatLng.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final SCLatLng other = (SCLatLng) obj;

        if (this.latitude != other.latitude) {
            return false;
        }

        if (this.longitude != other.longitude) {
            return false;
        }
        return true;
    }

    public double getLatitude() { return this.latitude; }

    public double getLongitude() { return this.longitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }
}
