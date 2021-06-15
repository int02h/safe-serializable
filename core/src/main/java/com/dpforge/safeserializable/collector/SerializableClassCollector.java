package com.dpforge.safeserializable.collector;

import java.io.Serializable;
import java.util.List;

/**
 * Collect all classes including inner classes that implements {@link Serializable} interface.
 */
public interface SerializableClassCollector {

    /**
     * @return list of collected classes
     */
    List<Class<?>> collect();
}
