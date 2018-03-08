package com.ucsb.cs48.spotcheck;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.FirebaseApp;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebaseCallback;
import com.ucsb.cs48.spotcheck.SCFirebaseInterface.SCFirebase;
import com.ucsb.cs48.spotcheck.SCLocalObjects.BlockedDates;
import com.ucsb.cs48.spotcheck.SCLocalObjects.ParkingSpot;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SCLatLng;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.ucsb.cs48.spotcheck.Utilities.SCConstants.TEST_USER_ID;
import static com.ucsb.cs48.spotcheck.Utilities.SCConstants.TEST_SPOT_OWNER_ID;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SCFirebaseTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.ucsb.cs48.spotcheck", appContext.getPackageName());
    }

    /**
     * MARK - ParkingSpot Integration Tests
     */

    @Test
    public void write_and_readSpot() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FirebaseApp.initializeApp(appContext);

        SCLatLng testLatLng = new SCLatLng(13.4, -35.73);

        SCFirebase scFirebase = new SCFirebase();
        final ParkingSpot writeSpot = new ParkingSpot(
            TEST_SPOT_OWNER_ID,
            "testAddress",
            testLatLng,
            10.5
        );

        final BlockedDates blockedDates = new BlockedDates(ThreadLocalRandom.current().nextLong(),
            ThreadLocalRandom.current().nextLong());
        writeSpot.addBlockedDates(blockedDates);

        final String spotID = scFirebase.createNewSpot(writeSpot);
        writeSpot.setSpotID(spotID);

        final CountDownLatch signal = new CountDownLatch(1);

        scFirebase.getParkingSpot(spotID, new SCFirebaseCallback<ParkingSpot>() {
            @Override
            public void callback(ParkingSpot data) {

                assertNotNull(data);
                assertEquals(writeSpot.getSpotID(), data.getSpotID());
                assertEquals(writeSpot.getOwnerID(), data.getOwnerID());
                assertEquals(writeSpot.getAddress(), data.getAddress());
                assertEquals(writeSpot.getLatLng(), data.getLatLng());
                assertEquals(writeSpot.getRate(), data.getRate(), 0.0);
                assertEquals(writeSpot.getBlockedDatesCount(), data.getBlockedDatesCount());

                ArrayList<BlockedDates> dataBlockedDates = data.getBlockedDatesList();
                assertEquals(blockedDates, dataBlockedDates.get(0));
                signal.countDown();
            }
        });

        scFirebase.deleteParkingSpot(writeSpot.getSpotID());

        signal.await(30, TimeUnit.SECONDS);
    }

    @Test
    public void getAllSpots() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FirebaseApp.initializeApp(appContext);

        // Run the SCFirebase test
        final SCFirebase scFirebase = new SCFirebase();

        final CountDownLatch signalB = new CountDownLatch(1);

        scFirebase.getAllParkingSpots(new SCFirebaseCallback<ArrayList<ParkingSpot>>() {
            @Override
            public void callback(ArrayList<ParkingSpot> data) {
                assertNotNull(data);

                for(final ParkingSpot spot : data) {

                    scFirebase.getParkingSpot(spot.getSpotID(), new SCFirebaseCallback<ParkingSpot>() {
                        @Override
                        public void callback(ParkingSpot data) {
                            if(data != null) {
                                assertEquals(data.getSpotID(), spot.getSpotID());
                                assertEquals(data.getOwnerID(), spot.getOwnerID());
                                assertEquals(data.getAddress(), spot.getAddress());
                                assertEquals(data.getLatLng(), spot.getLatLng());
                                assertEquals(data.getRate(), spot.getRate(), 0.0);
                            }
                        }
                    });
                }

                signalB.countDown();
            }
        });

        signalB.await(30, TimeUnit.SECONDS);
    }

    @Test
    public void deleteSpot() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FirebaseApp.initializeApp(appContext);

        SCLatLng testLatLng = new SCLatLng(13.4, -35.73);

        SCFirebase scFirebase = new SCFirebase();
        final ParkingSpot writeSpot = new ParkingSpot(
            TEST_SPOT_OWNER_ID,
            "testAddressID",
            testLatLng,
            10.5
        );

        final String spotID = scFirebase.createNewSpot(writeSpot);
        writeSpot.setSpotID(spotID);

        scFirebase.deleteParkingSpot(spotID);

        final CountDownLatch signal = new CountDownLatch(1);

        scFirebase.getParkingSpot(spotID, new SCFirebaseCallback<ParkingSpot>() {
            @Override
            public void callback(ParkingSpot data) {
                assertNull(data);
                signal.countDown();
            }
        });

        signal.await(30, TimeUnit.SECONDS);

    }

    @Test
    public void test_deleteTestParkingSpots() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FirebaseApp.initializeApp(appContext);

        SCFirebase scFirebase = new SCFirebase();

        SCLatLng testLatLng = new SCLatLng(13.4, -35.73);

        final ParkingSpot writeSpot = new ParkingSpot(
            TEST_SPOT_OWNER_ID,
            "testAddressID",
            testLatLng,
            10.5
        );

        final String spotID = scFirebase.createNewSpot(writeSpot);
        writeSpot.setSpotID(spotID);

        final CountDownLatch signalA = new CountDownLatch(1);

        scFirebase.deleteTestParkingSpots(new SCFirebaseCallback<Boolean>() {
            @Override
            public void callback(Boolean data) {
                assertTrue(data);
                signalA.countDown();
            }
        });
        signalA.await(30, TimeUnit.SECONDS);

        final CountDownLatch signal = new CountDownLatch(1);

        scFirebase.getParkingSpot(writeSpot.getSpotID(), new SCFirebaseCallback<ParkingSpot>() {
            @Override
            public void callback(ParkingSpot data) {
                assertNull(data);
                signal.countDown();
            }
        });

        signal.await(30, TimeUnit.SECONDS);
    }


    /**
     * MARK - SpotCheckUser Integration Tests
     */

    @Test
    public void write_and_readUser() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FirebaseApp.initializeApp(appContext);

        SCFirebase scFirebase = new SCFirebase();
        final SpotCheckUser writeUser = new SpotCheckUser(
            TEST_USER_ID,
            "testEmail",
            "Test Name",
                "Test Location"
        );

        scFirebase.uploadUser(writeUser);

        final CountDownLatch signal = new CountDownLatch(1);

        scFirebase.getSCUser(TEST_USER_ID, new SCFirebaseCallback<SpotCheckUser>() {
            @Override
            public void callback(SpotCheckUser data) {
                assertNotNull(data);

                assertEquals(writeUser.getUserID(), data.getUserID());
                assertEquals(writeUser.getEmail(), data.getEmail());
                assertEquals(writeUser.getFullname(), data.getFullname());
                assertEquals(writeUser.getLocation(), data.getLocation());
                signal.countDown();
            }
        });

        scFirebase.deleteUser(writeUser.getUserID());

        signal.await(30, TimeUnit.SECONDS);
    }

    @Test
    public void deleteUser() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FirebaseApp.initializeApp(appContext);

        SCFirebase scFirebase = new SCFirebase();
        final SpotCheckUser writeUser = new SpotCheckUser(
            TEST_USER_ID,
            "testEmail",
            "Test Name",
            "Test Location"
        );

        scFirebase.uploadUser(writeUser);

        scFirebase.deleteUser(writeUser.getUserID());

        final CountDownLatch signal = new CountDownLatch(1);

        scFirebase.getSCUser(writeUser.getUserID(), new SCFirebaseCallback<SpotCheckUser>() {
            @Override
            public void callback(SpotCheckUser data) {
                assertNull(data);
                signal.countDown();
            }
        });

        signal.await(30, TimeUnit.SECONDS);
    }

    @Test
    public void test_deleteTestUsers() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FirebaseApp.initializeApp(appContext);

        SCFirebase scFirebase = new SCFirebase();
        final SpotCheckUser writeUser = new SpotCheckUser(
            TEST_USER_ID,
            "testEmail",
            "Test Name",
            "Test Location"
        );

        scFirebase.uploadUser(writeUser);

        scFirebase.deleteTestUsers();

        final CountDownLatch signal = new CountDownLatch(1);

        scFirebase.getSCUser(writeUser.getUserID(), new SCFirebaseCallback<SpotCheckUser>() {
            @Override
            public void callback(SpotCheckUser data) {
                assertNull(data);
                signal.countDown();
            }
        });

        signal.await(30, TimeUnit.SECONDS);
    }
}
