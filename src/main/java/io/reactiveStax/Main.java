package io.reactiveStax;

import java.util.HashMap;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        GenericHashMap<Integer, String> genericHashMap = new GenericHashMap<>();
//        CacheEntry<String> cacheEntry = new CacheEntry<>("Testing Cache",5);
//        System.out.println("Is expired? "+cacheEntry.isExpired());
//        Thread.sleep(6000);
//        System.out.println("Is expired? "+cacheEntry.isExpired());

        Map<Integer, String> cacheMap = new HashMap<>();
        genericHashMap.put(1, "One");
        genericHashMap.put(2, "Two", -1);
        genericHashMap.put(3,"Three",1);
        genericHashMap.put(4,"Four",10);
        System.out.println("Initial Size: "+genericHashMap.size());
        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

        System.out.println("size after expire: "+genericHashMap.size());


        System.out.println("All Keys in Set: "+genericHashMap.keys());
        System.out.println(genericHashMap.get(1));
        System.out.println(genericHashMap.get(2));
        genericHashMap.remove(1);
        System.out.println("Size of KeySet after removing one key: "+genericHashMap.size());
        genericHashMap.clear();
        System.out.println("Size of the keySet after clearing the whole map: "+genericHashMap.size());

        genericHashMap.shutDown();

    }
}