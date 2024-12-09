package io.reactiveStax;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GenericHashMapTest {

    GenericHashMap<Integer, String> genericHashMap;

    @BeforeEach
    void setUp() {
        genericHashMap = new GenericHashMap<>();
    }

    @AfterEach
    void tearDown() {
        genericHashMap.concurrentHashMap.clear();
    }

    @Test
    void testPut() {
        genericHashMap.put(1, "one");
        assertEquals(1, genericHashMap.concurrentHashMap.size());
    }

    @Test
    void testPutWithTTL() {
        genericHashMap.put(1, "one", 20);
        assertEquals(1, genericHashMap.concurrentHashMap.size());
    }

    @Test
    void testWhenTTLIsInfinite() {
        genericHashMap.put(1, "one", -1);
        assertEquals("one", genericHashMap.get(1));
    }

    @Test
    void testGet() {
        genericHashMap.put(1, "one");
        genericHashMap.put(2, "two");
        assertEquals("two", genericHashMap.get(2));

    }

    @Test
    void testGetWithWrongKey() {
        genericHashMap.put(1, "one");
        genericHashMap.put(2, "two");
        assertNull(genericHashMap.get(3));

    }

    @Test
    void testRemove() {
        genericHashMap.put(1, "one");
        genericHashMap.put(2, "two");
        genericHashMap.remove(2);
        assertEquals(1, genericHashMap.concurrentHashMap.size());
    }

    @Test
    void testRemoveWithWrongKey() {
        genericHashMap.put(1, "one");
        genericHashMap.put(2, "two");
        assertEquals(false, genericHashMap.remove(3));
    }

    @Test
    void testSize() {
        genericHashMap.put(1, "one");
        genericHashMap.put(2, "two");
        assertEquals(2, genericHashMap.size());
    }

    @Test
    void testClear() {
        genericHashMap.put(1, "one");
        genericHashMap.put(2, "two");
        genericHashMap.clear();
        assertEquals(0, genericHashMap.concurrentHashMap.size());
    }

    @Test
    void testKeys() {
        genericHashMap.put(1, "one");
        genericHashMap.put(2, "two");
        assertEquals(Set.of(1, 2), genericHashMap.keys());
    }

    @Test
    void testShutDown() {
        genericHashMap.shutDown();
        assertTrue(genericHashMap.shutDown());
    }

    @Test
    void testDemonThread() throws InterruptedException {
        genericHashMap.put(3, "Three", 1);
        genericHashMap.put(4, "Four", 10);
        Thread.sleep(2000);
        assertEquals(1, genericHashMap.size());

    }

}