package io.reactiveStax;

import lombok.Getter;
import lombok.Setter;


import java.time.Instant;

public class CacheEntry<V> {
    @Getter
    @Setter
    private V value;
    @Setter
    private long ttl;
    @Getter
    private Instant lastAccessTime;

    public CacheEntry(V value, long ttl) {
        this.value = value;
        this.ttl = ttl;
        this.lastAccessTime = Instant.now();
    }

    public void UpdateLastAccessTime(){
        this.lastAccessTime = Instant.now();
    }
    
    public boolean isExpired(){
        if(ttl == -1){
            return false;
        }
        Instant expire = getLastAccessTime().plusSeconds(ttl);
        return Instant.now().isAfter(expire);
    }
}
