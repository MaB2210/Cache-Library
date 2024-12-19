package io.reactiveStax;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest {

    Cache<Integer, String> cache;

    @BeforeEach
    void setUp() {
        cache = CacheFactory.createCache(EvictionPolicy.LRU);

    }

    @AfterEach
    void tearDown() {
        cache.concurrentHashMap.clear();
    }

    @Test
    void testPut() {
        cache.put(1, "one");
        assertEquals(1, cache.concurrentHashMap.size());
    }

    @Test
    void testPutWithTTL() {
        cache = CacheFactory.createCache(EvictionPolicy.TTL);
        cache.put(1, "one", 20);
        assertEquals(1, cache.concurrentHashMap.size());
    }

    @Test
    void testWhenTTLIsInfinite() {
        cache = CacheFactory.createCache(EvictionPolicy.TTL);
        cache.put(1, "one", -1);
        assertEquals("one", cache.get(1));
    }

    @Test
    void testGet() {
        cache.put(1, "one");
        cache.put(2, "two");
        assertEquals("two", cache.get(2));

    }

    @Test
    void testGetWithWrongKey() {
        cache.put(1, "one");
        cache.put(2, "two");
        assertNull(cache.get(3));

    }

    @Test
    void testRemove() {
        cache.put(1, "one");
        cache.put(2, "two");
        cache.remove(2);
        assertEquals(1, cache.concurrentHashMap.size());
    }

    @Test
    void testRemoveWithWrongKey() {
        cache.put(1, "one");
        cache.put(2, "two");
        assertEquals(false, cache.remove(3));
    }

    @Test
    void testSize() {
        cache.put(1, "one");
        cache.put(2, "two");
        assertEquals(2, cache.size());
    }

    @Test
    void testClear() {
        cache.put(1, "one");
        cache.put(2, "two");
        cache.clear();
        assertEquals(0, cache.concurrentHashMap.size());
    }

    @Test
    void testKeys() {
        cache.put(1, "one");
        cache.put(2, "two");
        assertEquals(Set.of(1, 2), cache.keys());
    }

    @Test
    void testShutDown() {
        cache.shutDown();
        assertTrue(cache.shutDown());
    }

    @Test
    void testTTLEvictionPolicy() throws InterruptedException {
        cache = CacheFactory.createCache(EvictionPolicy.TTL);
        cache.put(3, "Three", 1);
        cache.put(4, "Four", 10);
        Thread.sleep(2000);
        assertEquals(1, cache.size());
    }

    @Test
    void testLRUEvictionPolicy() throws InterruptedException {
        cache = CacheFactory.createCache(EvictionPolicy.TTL);
        cache.put(1, "value1", 5);
        cache.put(2, "value2", 10);
        cache.put(3, "value3", 15);
        Thread.sleep(2000);
        cache.get(1);
        assertEquals(Set.of(3,2), cache.keys());
    }

    @Test
    void testFIFOEvictionPolicy() throws InterruptedException {
        cache = CacheFactory.createCache(EvictionPolicy.FIFO);
        cache.put(1, "value1", 5);
        cache.put(2, "value2", 10);
        cache.put(3, "value3", 15);
        Thread.sleep(2000);
        assertEquals(Set.of(3,2), cache.keys());
    }

    @Test
    void testLFUEvictionPolicy() throws InterruptedException {
        cache = CacheFactory.createCache(EvictionPolicy.LFU);
        cache.put(1, "value1", 5);
        cache.put(2, "value2", 10);
        cache.put(3, "value3", 15);
        cache.get(1);
        cache.get(1);
        cache.get(3);
        cache.get(3);
        Thread.sleep(2000);
        assertEquals(Set.of(1,3), cache.keys());
    }

    @Test
    void testInvalidEvictionPolicy(){
        cache = CacheFactory.createCache(EvictionPolicy.TTL);

        Exception exception = assertThrows(IllegalArgumentException.class,() -> CacheFactory.createCache(EvictionPolicy.ABC));
        assertEquals("INVALID EVICTION POLICY",exception.getMessage());
    }

    @Test
    void testRandomEvictionPolicy() throws InterruptedException {
        cache = CacheFactory.createCache(EvictionPolicy.RANDOM);
        cache.put(1, "value1", 5);
        cache.put(2, "value2", 10);
        cache.put(3, "value3", 15);
        cache.put(4, "value4", 5);
        cache.put(5, "value5", 10);
        cache.put(6, "value6", 15);
        Thread.sleep(2000);
        assertNotSame(cache.keys(), cache.keys());

    }

}