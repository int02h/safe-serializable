package com.dpforge.safeserializable.collector;

import com.dpforge.safeserializable.collector.classpath.ClasspathSerializableClassCollector;
import com.dpforge.safeserializable.collector.classpath.DirectoryClasspathFilter;
import org.junit.Test;

import java.io.Serializable;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

public class ClasspathSerializableClassCollectorTest {

    @Test
    public void collect_whole_project() {
        List<Class<?>> result = ClasspathSerializableClassCollector.builder()
                .addClasspathFilter(DirectoryClasspathFilter.createForCurrentProject())
                .build()
                .collect();

        assertThat(result, hasItem(Foo.class));
        assertThat(result, hasItem(Foo.Bar.class));
    }

    static class Foo implements Serializable {
        private static class Bar implements Serializable {
        }
    }
}