package com.dpforge.safeserializable.verifier;

import java.util.HashMap;
import java.util.Map;

class TypeUtils {

    private static final Map<Class<?>, Class<?>> WRAPPER_TYPE_MAP;

    static {
        WRAPPER_TYPE_MAP = new HashMap<>();
        WRAPPER_TYPE_MAP.put(Integer.class, int.class);
        WRAPPER_TYPE_MAP.put(Byte.class, byte.class);
        WRAPPER_TYPE_MAP.put(Character.class, char.class);
        WRAPPER_TYPE_MAP.put(Boolean.class, boolean.class);
        WRAPPER_TYPE_MAP.put(Double.class, double.class);
        WRAPPER_TYPE_MAP.put(Float.class, float.class);
        WRAPPER_TYPE_MAP.put(Long.class, long.class);
        WRAPPER_TYPE_MAP.put(Short.class, short.class);
        WRAPPER_TYPE_MAP.put(Void.class, void.class);
    }

    private TypeUtils() {
    }

    static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() || WRAPPER_TYPE_MAP.containsKey(type);
    }
}
