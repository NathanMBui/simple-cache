package com.epam.ld.effectivejava.maintask;

import java.util.concurrent.ConcurrentHashMap;

public class SimpleJavaCache<K, V> implements Cache<K,V> {

    ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();

    @Override
    public void set(K key, V value) {
        map.put(key, value);
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public int size() {
        return 0;
    }
}
