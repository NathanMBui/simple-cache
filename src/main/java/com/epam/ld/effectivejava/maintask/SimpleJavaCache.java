package com.epam.ld.effectivejava.maintask;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class SimpleJavaCache<K, V> implements Cache<K, V> {

    private final HashMap<K, V> keyValueMap;//cache K and V
    private final HashMap<K, Integer> keyCountMap;//keys and counters
    private final HashMap<Integer, LinkedHashSet<K>> countValuesMap;//Counter and item list
    private final int maxSize;
    private int min = -1; //to find the least frequency used key
    private RemovalListener<K, V> listener = null;

    public SimpleJavaCache() {
        this(Integer.MAX_VALUE);
    }

    public SimpleJavaCache(int maxSize) {
        this.maxSize = maxSize;
        keyValueMap = new HashMap<>();
        keyCountMap = new HashMap<>();
        countValuesMap = new HashMap<>();
        countValuesMap.put(1, new LinkedHashSet<>());
    }

    public synchronized V get(K key) {
        if (!keyValueMap.containsKey(key)) {
            return null;
        }
        // Get the count from counts map
        int count = keyCountMap.get(key);
        // increase the counter
        keyCountMap.put(key, count + 1);
        // remove the element from the counter to LinkedHashSet
        countValuesMap.get(count).remove(key);

        // when current min does not have any data, next one would be the min
        if (count == min && countValuesMap.get(count).size() == 0) {
            min++;
        }

        if (!countValuesMap.containsKey(count + 1)) {
            countValuesMap.put(count + 1, new LinkedHashSet<>());
        }

        countValuesMap.get(count + 1).add(key);
        return keyValueMap.get(key);
    }

    public synchronized void set(K key, V value) {
        if (maxSize <= 0) {
            return;
        }

        // If key does exist, we are returning from here
        if (keyValueMap.containsKey(key)) {
            keyValueMap.put(key, value);
            get(key);
            return;
        }

        if (keyValueMap.size() >= maxSize) {
            K evictedKey = countValuesMap.get(min).iterator().next();
            countValuesMap.get(min).remove(evictedKey);
            V evictedValue = keyValueMap.remove(evictedKey);
            keyCountMap.remove(evictedKey);
            if (listener != null) {
                listener.onRemove(evictedKey, evictedValue);
            }
        }

        // If the key is new, insert the value and current min should be 1
        keyValueMap.put(key, value);
        keyCountMap.put(key, 1);
        min = 1;
        countValuesMap.get(1).add(key);
    }

    @Override
    public boolean isFull() {
        return keyValueMap.size() >= maxSize;
    }

    @Override
    public void setRemovalListener(RemovalListener<K, V> listener) {
        this.listener = listener;
    }
}
