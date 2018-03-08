package com.ucsb.cs48.spotcheck.SCLocalObjects;

import java.util.Date;

public class BlockedDates {

    private long start;
    private long end;

    public BlockedDates() {
        // No arg constructor needed for Firebase
    }

    public BlockedDates(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public Date getStartDate() {
        return new Date(start);
    }

    public Date getEndDate() {
        return new Date(end);
    }

    public long getStart() { return this.start; }
    public void setStart(long start) { this.start = start; }

    public long getEnd() { return this.end; }
    public void setEnd(long end) { this.end = end; }
}
