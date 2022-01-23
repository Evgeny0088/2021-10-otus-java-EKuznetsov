package ru.otus.dataBasePackage.Cache;

public interface HwListener<K, V> {
    void notify(K key, V value, String action);
}
