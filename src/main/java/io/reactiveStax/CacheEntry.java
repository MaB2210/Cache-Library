package io.reactiveStax;

import lombok.Getter;
import lombok.Setter;


import java.time.Instant;

public class CacheEntry<V> {
    @Getter
    private V value;
    private long ttl;
    @Getter
    private Instant lastAccessTime;
    @Getter
    private Instant creationTime;
    @Getter
    private int frequency;

    public CacheEntry(V value, long ttl) {
        this.value = value;
        this.ttl = ttl;
        this.lastAccessTime = Instant.now();
        this.creationTime = Instant.now();
        this.frequency = 0;
    }

    public void UpdateLastAccessTime(){
        this.lastAccessTime = Instant.now();
        frequency++;
    }

    //public void IncrementFrequency(){this.frequency = frequency++;}
    
    public boolean isExpired(){
        if(ttl == -1){
            return false;
        }
        Instant expire = getLastAccessTime().plusSeconds(ttl);
        return Instant.now().isAfter(expire);
    }
}
