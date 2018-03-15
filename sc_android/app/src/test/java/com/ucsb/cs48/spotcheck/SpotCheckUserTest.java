package com.ucsb.cs48.spotcheck;

import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for SpotCheckUser
 */
public class SpotCheckUserTest {

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
}
