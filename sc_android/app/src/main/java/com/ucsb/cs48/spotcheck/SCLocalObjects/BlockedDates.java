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

    public Boolean conflict(long requestStart, long requestEnd) {
        if(requestEnd < this.start) {
            return false;
        }

        if(requestStart > this.end) {
            return false;
        }

        return true;
    }

    public long getStart() { return this.start; }
    public void setStart(long start) { this.start = start; }

    public long getEnd() { return this.end; }
    public void setEnd(long end) { this.end = end; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!BlockedDates.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final BlockedDates other = (BlockedDates) obj;

        if (this.start != other.start) {
            return false;
        }

        if (this.end != other.end) {
            return false;
        }
        return true;
    }
}
