package com.epam.ld.effectivejava.maintask;

public interface Cache<K, V> {
    void set(K key, V value);

    V get(K key);

    boolean isFull();

    void setRemovalListener(RemovalListener<K, V> listener);
}
