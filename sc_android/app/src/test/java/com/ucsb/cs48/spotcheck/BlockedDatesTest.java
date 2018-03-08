package com.ucsb.cs48.spotcheck;

import com.ucsb.cs48.spotcheck.SCLocalObjects.BlockedDates;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

/**
 * Unit tests for ParkingSpot
 */
public class BlockedDatesTest {

    @Test
    public void test_getDate() {
        Calendar calendar = Calendar.getInstance();
        Date startDate = new Date();

        // Start Time=
        long start = startDate.getTime();

        // End Time
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, 3);
        Date endDate = calendar.getTime();
        long end = calendar.getTimeInMillis();

        BlockedDates blockedDates = new BlockedDates(start, end);

        assertEquals(startDate, blockedDates.getStartDate());
        assertEquals(endDate, blockedDates.getEndDate());
    }

    @Test
    public void test_equals() {
        long start = ThreadLocalRandom.current().nextLong();
        long end = ThreadLocalRandom.current().nextLong();

        BlockedDates blockedDatesOne = new BlockedDates(start, end);
        BlockedDates blockedDatesTwo = new BlockedDates(start, end);


        BlockedDates blockedDatesThree = new BlockedDates(
            ThreadLocalRandom.current().nextLong(),
            ThreadLocalRandom.current().nextLong());

        BlockedDates blockedDatesFour = new BlockedDates(end, start);

        assertTrue(blockedDatesOne.equals(blockedDatesTwo));
        assertTrue(blockedDatesTwo.equals(blockedDatesOne));

        assertFalse(blockedDatesOne.equals(blockedDatesThree));

        assertFalse(blockedDatesTwo.equals(blockedDatesFour));
    }

    @Test
    public void test_conflict() {
        long start = 400L;
        long end = 600L;
        BlockedDates blockedDatesA = new BlockedDates(start, end);

        assertFalse(blockedDatesA.conflict(100L, 350L));
        assertFalse(blockedDatesA.conflict(700L, 900L));

        assertTrue(blockedDatesA.conflict(300L, 500L));
        assertTrue(blockedDatesA.conflict(550L, 800L));
        assertTrue(blockedDatesA.conflict(200L, 1000L));

    }
}
