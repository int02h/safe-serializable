package com.dpforge.safeserializable.verifier;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

class FieldInfo {

    private final ClassInfo classInfo;
    private final Field field;

    FieldInfo(ClassInfo classInfo, Field field) {
        this.classInfo = classInfo;
        this.field = field;
    }

    String getName() {
        return field.getName();
    }

    Field getField() {
        return field;
    }

    Class<?> getType() {
        return field.getType();
    }

    Class<?> getActualType() {
        return classInfo.resolveType(field.getType(), field.getGenericType());
    }

    Map<String, Class<?>> getTypeArguments() {
        return TypeArgumentsMapper.map(field.getType(), field.getGenericType());
    }

    boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }

    boolean isTransient() {
        return Modifier.isTransient(field.getModifiers());
    }
}
