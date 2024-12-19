package io.reactiveStax;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {

        Cache<Integer,String>cache = CacheFactory.createCache(EvictionPolicy.LRU);
//        Cache<Integer, String> cache = new Cache<>();
//        cache.evictionPolicyProvider("random");
//        cache.evictionPolicyProvider("ttl");
//        Cache<Integer, String> cache1 = new Cache<>();
//        cache1.evictionPolicyProvider("ttl");
//        CHECKING isExpired()
//        CacheEntry<String> cacheEntry = new CacheEntry<>("Testing Cache",5);
//        System.out.println("Is expired? "+cacheEntry.isExpired());
//        Thread.sleep(6000);
//        System.out.println("Is expired? "+cacheEntry.isExpired());

        cache.put(2, "Two", -1);
        cache.put(1, "One");
        cache.put(3, "Three", 1);
        cache.put(4, "Four", 10);


//        CHECKING TTL EVICTION POLICY
//        System.out.println("Initial Size: "+cache.size());
//        try{
//            Thread.sleep(2000);
//        }catch (InterruptedException e){
//            Thread.currentThread().interrupt();
//        }
//        System.out.println("size after expire: "+cache.size());

//        CHECKING LRU EVICTION POLICY
        System.out.println("keys before lru: "+cache.keys());
        Thread.sleep(2000);
        cache.get(1);
        System.out.println("keys after lru: "+cache.keys());


//        CHECKING FIFO EVICTION POLICY
//        System.out.println("keys before fifo: " + cache.keys());
//        Thread.sleep(2000);
//        System.out.println("keys after fifo: " + cache.keys());


//        CHECKING LFU EVICTION POLICY
//        System.out.println("Keys before LFU: " + cache.keys());
//        cache.get(1);
//        cache.get(2);
//        cache.get(4);
//        Thread.sleep(2000);
//        System.out.println("Keys after LFU: "+ cache.keys());

//        System.out.println("Keys before random: " + cache.keys());
//        Thread.sleep(2000);
//        System.out.println("keys after random: " + cache.keys());


        System.out.println("All Keys in Set: " + cache.keys());
        System.out.println(cache.get(1));
        System.out.println(cache.get(2));
        cache.remove(1);
        System.out.println("Size of KeySet after removing one key: " + cache.size());
        cache.clear();
        System.out.println("Size of the keySet after clearing the whole map: " + cache.size());

        cache.shutDown();

    }
}