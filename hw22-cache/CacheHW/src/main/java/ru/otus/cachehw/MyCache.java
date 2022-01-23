package ru.otus.cachehw;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    private static final int CACHE_SIZE = 1000;

    private final Map<K,V> cache = new WeakHashMap<>(CACHE_SIZE);
    private final List<WeakReference<HwListener<K,V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        if (cache.size()<CACHE_SIZE){
            cache.put(key,value);
            logger.info("### Cache is updated: {} - {} ###",key,value);
        }
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listener = null;
        logger.info("listener is removed: listener == {}", listener);
    }
}
