package io.reactiveStax;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CacheFactory {
    private static Map<EvictionPolicy,List<Cache<?,?>>> policyCacheMap = new HashMap<>();
    private static Map<EvictionPolicy, ConcurrentHashMap<?,?>> sharedConcurrentHashMap = new HashMap<>();

    public static <K,V> Cache<K,V> createCache(EvictionPolicy policy){
        ConcurrentHashMap<K, CacheEntry<V>> concurrentHashMap = (ConcurrentHashMap<K, CacheEntry<V>>) sharedConcurrentHashMap
                .computeIfAbsent(policy, p -> new ConcurrentHashMap<>());

        Cache<K,V> cache = new Cache<>(concurrentHashMap);

        policyCacheMap.computeIfAbsent(policy,p-> new ArrayList<>()).add(cache);

        return cache;
    }

}
