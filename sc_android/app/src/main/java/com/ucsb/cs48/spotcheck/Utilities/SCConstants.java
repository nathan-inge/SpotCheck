package com.ucsb.cs48.spotcheck.Utilities;

public final class SCConstants {

    private SCConstants() {
        // Restrict abstraction
    }

    public static final String TEST_USER_ID = "test-user-ID-123-abc";
    public static final String TEST_SPOT_OWNER_ID = "testOwnerID";

    // Responses
    public static final int SPOT_CREATED = 10;
    public static final int SPOT_EDITED = 11;
    public static final int SPOT_DELETED = 12;
    public static final int PROFILE_EDITED = 13;
    public static final int SPOT_RENTED = 14;


    public static final int SEND_OWNER_EMAIL = 20;

    // Requests
    public static final int REQUEST_EDIT_SPOT = 100;
    public static final int REQUEST_SPOT_DETAILS = 101;
    public static final int REQUEST_CREATE_SPOT = 102;
    public static final int REQUEST_PICK_IMAGE = 103;
    public static final int REQUEST_EDIT_PROFILE = 104;

    // Firebase
    public static final String PARKINGSPOT_PATH = "parking_spots";
    public static final String USER_PATH = "users";
    public static final String SPOT_IMAGE_POSTFIX = "-spotImage.jpg";
    public static final String USER_IMAGE_POSTFIX = "-userImage.jpg";
}
