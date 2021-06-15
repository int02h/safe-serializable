package com.dpforge.safeserializable.collector.classpath;

import com.dpforge.safeserializable.collector.SerializableClassCollector;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link SerializableClassCollector} based on classpath scanning
 */
public class ClasspathSerializableClassCollector implements SerializableClassCollector {

    private final List<ClasspathFilter> classpathFilters;

    private ClasspathSerializableClassCollector(Builder builder) {
        classpathFilters = builder.classpathFilters;
    }

    @Override
    public List<Class<?>> collect() {
        ClassGraph classGraph = new ClassGraph()
                .enableClassInfo()
                .ignoreClassVisibility()
                .filterClasspathElements(this::filterClasspathElement);

        try (ScanResult result = classGraph.scan()) {
            return result.getAllClasses()
                    .stream()
                    .filter(this::filterClassInfo)
                    .map(ClassInfo::loadClass)
                    .collect(Collectors.toList());
        }
    }

    private boolean filterClasspathElement(String classpath) {
        return classpathFilters.stream().anyMatch(filter -> filter.shouldCollectClasspath(classpath));
    }

    private boolean filterClassInfo(ClassInfo classInfo) {
        return classInfo.implementsInterface(Serializable.class.getName());
    }

    /**
     * @return new empty builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<ClasspathFilter> classpathFilters = new ArrayList<>();

        /**
         * Add classpath filter to filters list of this config
         *
         * @param filter filter to add
         */
        public Builder addClasspathFilter(ClasspathFilter filter) {
            classpathFilters.add(filter);
            return this;
        }

        public ClasspathSerializableClassCollector build() {
            return new ClasspathSerializableClassCollector(this);
        }
    }

}
