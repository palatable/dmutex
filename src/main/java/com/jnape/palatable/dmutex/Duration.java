package com.jnape.palatable.dmutex;

import java.util.concurrent.TimeUnit;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public final class Duration {
    private static final Duration INFINITY = days(MAX_VALUE);

    private final int      value;
    private final TimeUnit timeUnit;

    private Duration(int value, TimeUnit timeUnit) {
        this.value = value;
        this.timeUnit = timeUnit;
    }

    public long toMillis() {
        return timeUnit.toMillis(value);
    }

    @Override
    public String toString() {
        return format("%d %s", value, timeUnit.name().toLowerCase());
    }

    public static Duration duration(int value, TimeUnit timeUnit) {
        return new Duration(value, timeUnit);
    }

    public static Duration milliseconds(int value) {
        return duration(value, MILLISECONDS);
    }

    public static Duration seconds(int value) {
        return duration(value, SECONDS);
    }

    public static Duration minutes(int value) {
        return duration(value, MINUTES);
    }

    public static Duration hours(int value) {
        return duration(value, HOURS);
    }

    public static Duration days(int value) {
        return duration(value, DAYS);
    }

    public static Duration forever() {
        return INFINITY;
    }
}
