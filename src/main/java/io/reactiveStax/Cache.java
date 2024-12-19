package io.reactiveStax;


import java.util.Set;
import java.util.concurrent.*;

public class Cache<K, V> implements CacheHashMap<K, V> {
    ConcurrentHashMap<K, CacheEntry<V>> concurrentHashMap = new ConcurrentHashMap<>();
    private final long DEFAULT_TTL = 60;
    private ScheduledExecutorService executorService;
    private EvictionPolicyManagerService<K, V> evictionPolicyManagerService;

    public Cache(ConcurrentHashMap<K,CacheEntry<V>>concurrentHashMap) {
        this.concurrentHashMap = concurrentHashMap;
        startCleanUpThread();
    }

    public void startCleanUpThread() {
        executorService = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
        evictionPolicyManagerService = new EvictionPolicyManagerService<>(concurrentHashMap, executorService);
    }

    public void evictionPolicyProvider(EvictionPolicy policy) {
        switch (policy) {
            case TTL -> evictionPolicyManagerService.applyTTLEvictionPolicy();
            case LRU -> evictionPolicyManagerService.applyLRUEvictionPolicy();
            case FIFO -> evictionPolicyManagerService.applyFIFOEvictionPolicy();
            case LFU -> evictionPolicyManagerService.applyLFUEvictionPolicy();
            case RANDOM -> evictionPolicyManagerService.applyRandomReplacement();
            default -> throw new IllegalArgumentException("INVALID EVICTION POLICY");
        }
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

    public Boolean shutDown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        System.out.println("EXECUTOR SERVICE SHUT DOWN!!");
        return true;
    }


}
