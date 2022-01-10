package com.dpforge.safeserializable.sample;

import com.dpforge.safeserializable.SafeSerializable;
import com.dpforge.safeserializable.collector.SerializableClassCollector;
import com.dpforge.safeserializable.collector.classpath.ClasspathSerializableClassCollector;
import com.dpforge.safeserializable.collector.classpath.DirectoryClasspathFilter;

public class SampleMain {

    public static void main(String[] args) {
        SerializableClassCollector classCollector = ClasspathSerializableClassCollector.builder()
                .addClasspathFilter(DirectoryClasspathFilter.createForCurrentProject())
                .build();
        try {
            SafeSerializable.builder()
                    .serializableClassCollector(classCollector)
                    .build()
                    .checkSafety();
            System.out.println("All classes implements java.io.Serializable properly");
        } catch (AssertionError error) {
            System.out.println(error.getMessage());
        }
        System.out.println("Done");
    }
}
