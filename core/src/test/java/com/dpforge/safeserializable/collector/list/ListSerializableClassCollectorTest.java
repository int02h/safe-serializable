package com.dpforge.safeserializable.collector.list;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ListSerializableClassCollectorTest {

    @Test
    public void vararg_constructor() {
        List<Class<?>> list = new ListSerializableClassCollector(String.class, Integer.class, Boolean.class).collect();

        assertThat(list.size(), is(3));
        assertThat(list, hasItem(String.class));
        assertThat(list, hasItem(Integer.class));
        assertThat(list, hasItem(Boolean.class));
    }
}