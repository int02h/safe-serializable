package com.dpforge.safeserializable.collector.classpath;

/**
 * Allows to filter out specific classpath items when collecting classes
 * with {@code ClasspathSerializableClassCollector}
 */
public interface ClasspathFilter {

    /**
     * Check whether classpath should be used for class collection
     * @param classpath some classpath
     * @return {@code true} means collect classes from specified classpath, {@code false} - skip this classpath
     */
    boolean shouldCollectClasspath(String classpath);
}
