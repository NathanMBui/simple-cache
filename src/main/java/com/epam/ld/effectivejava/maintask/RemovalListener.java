package com.epam.ld.effectivejava.maintask;

public interface RemovalListener<K, V> {
    void onRemove(K key, V value);
}
