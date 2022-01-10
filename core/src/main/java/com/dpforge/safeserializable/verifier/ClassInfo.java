package com.dpforge.safeserializable.verifier;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.Map;

class ClassInfo {

    private final Class<?> clazz;
    private final Map<String, Class<?>> typeArguments;

    ClassInfo(Class<?> clazz) {
        this(clazz, Collections.emptyMap());
    }

    ClassInfo(Class<?> clazz, Map<String, Class<?>> typeArguments) {
        this.clazz = clazz;
        this.typeArguments = typeArguments;
    }

    Class<?> getWrappedClass() {
        return clazz;
    }

    boolean isPrimitiveOrWrapper() {
        return TypeUtils.isPrimitiveOrWrapper(clazz);
    }

    boolean isArray() {
        return clazz.isArray();
    }

    ClassInfo getArrayItemClassInfo() {
        return new ClassInfo(clazz.getComponentType());
    }

    boolean isInterface() {
        return clazz.isInterface();
    }

    // Maybe use java.lang.Iterable for better performance (no need to create separate array)
    FieldInfo[] getDeclaredFields() {
        Field[] originalFields = clazz.getDeclaredFields();
        FieldInfo[] fields = new FieldInfo[originalFields.length];
        for (int i = 0; i < originalFields.length; i++) {
            fields[i] = new FieldInfo(this, originalFields[i]);
        }
        return fields;
    }

    Class<?> resolveType(Class<?> type, Type genericType) {
        if (genericType instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) genericType;
            Class<?> resolved = typeArguments.get(typeVariable.getName());
            if (resolved == null) {
                throw new IllegalStateException();
            }
            return resolved;
        }
        return type;
    }

    @Nullable
    ClassInfo getSuperclassInfo() {
        Type supertype = clazz.getGenericSuperclass();
        if (supertype == null || supertype == Object.class) {
            return null;
        }
        Class<?> superclass = clazz.getSuperclass();
        Map<String, Class<?>> result = TypeArgumentsMapper.map(superclass, supertype);
        return new ClassInfo(superclass, result);
    }

    boolean isSerializable() {
        return Serializable.class.isAssignableFrom(clazz);
    }

    boolean hasNonResolvableTypeArguments() {
        return clazz.getTypeParameters().length != typeArguments.size();
    }
}
