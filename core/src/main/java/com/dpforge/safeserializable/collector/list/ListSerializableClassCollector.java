package com.dpforge.safeserializable.collector.list;

import com.dpforge.safeserializable.collector.SerializableClassCollector;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link SerializableClassCollector} which returns fixed predefined list of classes
 */
public class ListSerializableClassCollector implements SerializableClassCollector {

    private final List<Class<?>> classes;

    public ListSerializableClassCollector(List<Class<?>> classes) {
        this.classes = classes;
    }

    public ListSerializableClassCollector(Class<?>... classes) {
        this(Arrays.asList(classes.clone()));
    }

    @Override
    public List<Class<?>> collect() {
        return classes;
    }
}
