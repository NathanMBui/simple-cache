package com.epam.ld.effectivejava.maintask;

import org.junit.jupiter.api.Test;

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
    public void testMaxSize() {
        //given
        cache = new SimpleJavaCache<>(10);

        //when
        for (int i = 0; i < 11; i++) {
            cache.set("K" + i, "V");
        }

        //then
        assertTrue(cache.isFull());
    }

    @Test
    public void testRemovalListener() {
        //given
        cache = new SimpleJavaCache<>(1);
        RemovalListener<String, String> listener = mock(RemovalListener.class);
        cache.setRemovalListener(listener);

        //when
        cache.set("key", "value");
        cache.set("key2",  "value");

        //then
        verify(listener).onRemove(any(), any());
    }


}
