package io.reactiveStax;

import java.util.Set;

public interface CacheHashMap<K,V> {
     void put(K key,V value);
     void put(K key,V valur,long ttl);
     V get(K key);
     boolean remove(K key);
     int size();
     void clear();
     Set<K> keys();
}
