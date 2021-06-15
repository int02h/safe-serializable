package com.dpforge.safeserializable.verifier;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TypeArgumentsMapperTest {

    @Test
    public void map_field() throws NoSuchFieldException {
        Field field = FieldTest.class.getDeclaredField("map");
        Map<String, Class<?>> result = TypeArgumentsMapper.map(field.getType(), field.getGenericType());

        assertThat(result.size(), is(2));
        assertThat(result, hasEntry("K", String.class));
        assertThat(result, hasEntry("V", Date.class));
    }

    @Test
    public void map_superclass() {
        Map<String, Class<?>> result = TypeArgumentsMapper.map(
                SuperclassTest.class.getSuperclass(),
                SuperclassTest.class.getGenericSuperclass()
        );
        assertThat(result.size(), is(1));
        assertThat(result, hasEntry("E", String.class));
    }

    private static class FieldTest {
        Map<String, Date> map;
    }

    private static class SuperclassTest extends ArrayList<String> {
    }
}