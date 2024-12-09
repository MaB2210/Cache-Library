package io.reactiveStax;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class GenericHashMap<K, V> implements CacheHashMap<K, V> {
    ConcurrentHashMap<K, CacheEntry<V>> concurrentHashMap = new ConcurrentHashMap<>();
    private final long DEFAULT_TTL = 60;
    private ScheduledExecutorService executorService;

    public GenericHashMap() {
        startCleanUpThread();
    }

    public void startCleanUpThread(){
         executorService = Executors.newSingleThreadScheduledExecutor(runnable ->{
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
        executorService.scheduleAtFixedRate(() -> {
            for(K key : concurrentHashMap.keySet()){
                CacheEntry<V> entry = concurrentHashMap.get(key);
                if(entry!= null && entry.isExpired()){
                    concurrentHashMap.remove(key);
                }
            }
        },0,1, TimeUnit.SECONDS);
    }

    @Override
    public void put(K key, V value) {
        put(key, value, DEFAULT_TTL);
    }

    @Override
    public void put(K key, V value, long ttl) {
        CacheEntry<V> cacheEntry = new CacheEntry<>(value, ttl);
        concurrentHashMap.put(key, cacheEntry);
    }

    @Override
    public V get(K key) {
        CacheEntry<V> cacheEntry = concurrentHashMap.get(key);
        if (cacheEntry == null || cacheEntry.isExpired()) {
            concurrentHashMap.remove(key);
            return null;
        }
        cacheEntry.UpdateLastAccessTime();
        return cacheEntry.getValue();

    }

    @Override
    public boolean remove(K key) {
        if (concurrentHashMap.containsKey(key)) {
            concurrentHashMap.remove(key);
            return true;
        } else
            return false;

    }

    @Override
    public int size() {
        return concurrentHashMap.size();
    }

    @Override
    public void clear() {
        concurrentHashMap.clear();
    }

    @Override
    public Set<K> keys() {
        return concurrentHashMap.keySet();
    }

    public Boolean shutDown(){
        if(executorService != null && !executorService.isShutdown()){
            executorService.shutdown();
        }
        System.out.println("EXECUTOR SERVICE SHUT DOWN!!");
        return true;
    }


}
