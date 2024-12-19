package io.reactiveStax;

public interface EvictionPolicyManager {
    void applyTTLEvictionPolicy();
    void applyLRUEvictionPolicy();
    void applyFIFOEvictionPolicy();
    void applyLFUEvictionPolicy();
    void applyRandomReplacement();
}
