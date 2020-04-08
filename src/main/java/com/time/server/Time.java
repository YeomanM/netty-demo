package com.time.server;

import java.util.Date;

public class Time {

    private long value;

    public Time() {
        this(System.currentTimeMillis() / 1000L);
    }

    public Time(long value) {
        this.value = value;
    }

    public long getValue(){
        return this.value;
    }

    @Override
    public String toString() {
        return new Date((getValue()) * 1000L).toString();
    }
}
