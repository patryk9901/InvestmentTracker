package com.example.investmenttracker.domain;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class CustomClock extends Clock {

    @Override
    public ZoneId getZone() {
        return null;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return null;
    }

    @Override
    public Instant instant() {
        return null;
    }
}
