package com.ucsb.cs48.spotcheck;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.FirebaseApp;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SCInterfaceTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.ucsb.cs48.spotcheck", appContext.getPackageName());
    }

    @Test
    public void write_and_readSpot() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FirebaseApp.initializeApp(appContext);

        SCFirebase scFirebase = new SCFirebase();
        final ParkingSpot writeSpot = new ParkingSpot(
            "testOwnerID",
            "testAddress",
            10.5
        );

        final String spotID = scFirebase.createNewSpot(writeSpot);
        writeSpot.setSpotID(spotID);

        final CountDownLatch signal = new CountDownLatch(1);

        scFirebase.getParkingSpot(spotID, new SCFirebaseCallback<ParkingSpot>() {
            @Override
            public void callback(ParkingSpot data) {

                assertNotNull(data);
                data.setSpotID(spotID);
                assertEquals(writeSpot.getSpotID(), data.getSpotID());
                assertEquals(writeSpot.getOwnerID(), data.getOwnerID());
                assertEquals(writeSpot.getAddress(), data.getAddress());
                assertEquals(writeSpot.getRate(), data.getRate(), 0.0);
                signal.countDown();
            }
        });

        signal.await(30, TimeUnit.SECONDS);
    }

    @Test
    public void getAllSpots() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FirebaseApp.initializeApp(appContext);

        // Run the SCFirebase test
        SCFirebase scFirebase = new SCFirebase();

        final CountDownLatch signalB = new CountDownLatch(1);

        scFirebase.getAllParkingSpots(new SCFirebaseCallback<ArrayList<ParkingSpot>>() {
            @Override
            public void callback(ArrayList<ParkingSpot> data) {
                assertNotNull(data);

                assertEquals(data.size(),  12);
                signalB.countDown();
            }
        });

        signalB.await(30, TimeUnit.SECONDS);
    }

    @Test
    public void write_and_readUser() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FirebaseApp.initializeApp(appContext);

        final String testUserID = "test-user-ID-123-abc";

        SCFirebase scFirebase = new SCFirebase();
        final SpotCheckUser writeUser = new SpotCheckUser(
            testUserID,
            "testEmail",
            "Test Name"
        );

        scFirebase.updateDatabaseUser(writeUser);

        final CountDownLatch signal = new CountDownLatch(1);

        scFirebase.getSCUser(testUserID, new SCFirebaseCallback<SpotCheckUser>() {
            @Override
            public void callback(SpotCheckUser data) {
                assertNotNull(data);

                data.setUserID(testUserID);
                assertEquals(writeUser.getUserID(), data.getUserID());
                assertEquals(writeUser.getEmail(), data.getEmail());
                assertEquals(writeUser.getFullname(), data.getFullname());
                signal.countDown();
            }
        });

        signal.await(30, TimeUnit.SECONDS);
    }
}
