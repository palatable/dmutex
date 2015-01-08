package com.jnape.palatable.dmutex;

import org.junit.Test;

import static com.jnape.palatable.dmutex.Duration.days;
import static com.jnape.palatable.dmutex.Duration.forever;
import static com.jnape.palatable.dmutex.Duration.hours;
import static com.jnape.palatable.dmutex.Duration.milliseconds;
import static com.jnape.palatable.dmutex.Duration.minutes;
import static com.jnape.palatable.dmutex.Duration.seconds;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DurationTest {

    @Test
    public void foreverIsQuiteALongTime() {
        assertTrue(forever().toMillis() > days(1000).toMillis());
    }

    @Test
    public void millisecondConversionsAreAccurate() {
        assertThat(milliseconds(1).toMillis(), is(MILLISECONDS.toMillis(1)));
        assertThat(seconds(1).toMillis(), is(SECONDS.toMillis(1)));
        assertThat(minutes(1).toMillis(), is(MINUTES.toMillis(1)));
        assertThat(hours(1).toMillis(), is(HOURS.toMillis(1)));
        assertThat(days(1).toMillis(), is(DAYS.toMillis(1)));
    }

    @Test
    public void stringRepresentationsUseNaturalLanguage() {
        assertThat(milliseconds(0).toString(), is("0 milliseconds"));
        assertThat(seconds(2).toString(), is("2 seconds"));
        assertThat(minutes(10).toString(), is("10 minutes"));
        assertThat(hours(23).toString(), is("23 hours"));
        assertThat(days(10000).toString(), is("10000 days"));
    }
}