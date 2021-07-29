package com.epam.ld.effectivejava.maintask;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MainTaskTest {

    Cache<String, String> cache;

    @Test
    public void testSimpleCache() {
        cache = new SimpleJavaCache<>();
        cache.set("key", "value");
        assertEquals("value", cache.get("key"));
    }

    @Test
    public void testMaxSize100() {
        //given
        cache = new SimpleJavaCache<>(new SizeEviction(100_000));

        //when
        for (int i = 0; i < 100_001; i++) {
            cache.set("K" + 1, "V");
        }

        //then
        assertEquals(100_000, cache.size());
    }

    @Test
    public void testTimedBaseOnLastAccess() throws InterruptedException {
        //given
        cache = new SimpleJavaCache<>(new TimedBaseEviction(Duration.ofSeconds(5)));
        cache.set("key", "value");

        //when
        Thread.sleep(2000L);
        //then
        assertNotNull(cache.get("key"));

        //when
        Thread.sleep(4000L);
        //then
        assertNull(cache.get("key"));
    }

    @Test
    public void testRemovalListener() throws InterruptedException {
        //given
        cache = new SimpleJavaCache<>(new SizeEviction(1));
        RemovalListener listener = mock(RemovalListener);
        cache.setRemovalLisenter(listener);

        //when
        cache.set("key", "value");
        cache.set("key2",  "value");

        //then
        verify(listener).onRemove(any());
    }


}
