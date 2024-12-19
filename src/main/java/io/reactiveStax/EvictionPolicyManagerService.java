package io.reactiveStax;

import java.time.Instant;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EvictionPolicyManagerService<K, V> implements EvictionPolicyManager {
    private ConcurrentHashMap<K, CacheEntry<V>> concurrentHashMap;
    private ScheduledExecutorService executorService;

    public EvictionPolicyManagerService(ConcurrentHashMap<K, CacheEntry<V>> concurrentHashMap, ScheduledExecutorService executorService) {
        this.concurrentHashMap = concurrentHashMap;
        this.executorService = executorService;
    }

    @Override
    public void applyTTLEvictionPolicy() {
        executorService.scheduleAtFixedRate(() -> {
            for (K key : concurrentHashMap.keySet()) {
                CacheEntry<V> entry = concurrentHashMap.get(key);
                if (entry != null && entry.isExpired()) {
                    concurrentHashMap.remove(key);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void applyLRUEvictionPolicy() {
        executorService.scheduleAtFixedRate(() -> {
            K lurKey = null;
            Instant leastRecentTime = Instant.now();
            for (K key : concurrentHashMap.keySet()) {
                CacheEntry<V> entry = concurrentHashMap.get(key);
                if (entry != null && entry.getLastAccessTime().isBefore(leastRecentTime)) {
                    leastRecentTime = entry.getLastAccessTime();
                    lurKey = key;
                }
            }
            if (lurKey != null) {
                concurrentHashMap.remove(lurKey);
            }
        }, 1, 3, TimeUnit.SECONDS);
    }

    @Override
    public void applyFIFOEvictionPolicy() {
        executorService.scheduleAtFixedRate(() -> {
            K fifoKey = null;
            Instant firstCreatedKey = Instant.now();
            for (K key : concurrentHashMap.keySet()) {
                CacheEntry<V> entry = concurrentHashMap.get(key);
                if (entry != null && entry.getCreationTime().isBefore(firstCreatedKey)) {
                    firstCreatedKey = entry.getCreationTime();
                    fifoKey = key;
                }
            }
            if (fifoKey != null) {
                concurrentHashMap.remove(fifoKey);
            }
        }, 1, 3, TimeUnit.SECONDS);
    }

    @Override
    public void applyLFUEvictionPolicy() {
        executorService.scheduleAtFixedRate(() -> {
            K lfuKey = null;
            int frequency = Integer.MAX_VALUE;
            for (K key : concurrentHashMap.keySet()) {
                CacheEntry<V> entry = concurrentHashMap.get(key);
                if (entry != null && entry.getFrequency() < frequency) {
                    lfuKey = key;
                    frequency = entry.getFrequency();
                }
            }
            if (lfuKey != null) {
                concurrentHashMap.remove(lfuKey);
            }
        }, 1, 3, TimeUnit.SECONDS);
    }

    @Override
    public void applyRandomReplacement() {
        executorService.scheduleAtFixedRate(() ->{
            if(!concurrentHashMap.isEmpty()){
               K randomKey = concurrentHashMap.keySet()
                       .stream()
                       .skip(new Random().nextInt(concurrentHashMap.size()))
                       .findFirst()
                       .orElse(null);
               if(randomKey != null){
                   concurrentHashMap.remove(randomKey);
               }
            }
        },1,3,TimeUnit.SECONDS);
    }
}
