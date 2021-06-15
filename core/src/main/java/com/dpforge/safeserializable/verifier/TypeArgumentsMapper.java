package com.dpforge.safeserializable.verifier;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class that maps type arguments to their real values.
 * <br/><br/>
 * <b>Example</b>
 * <pre>
 * class Foo {
 *      Map&lt;String, Integer&gt; map;
 * }
 * </pre>
 * <p>
 * For the class from above {@code TypeArgumentsMapper} returns the following map:
 * <pre>
 * {K = String.class, V = Integer.class}
 * </pre>
 */
class TypeArgumentsMapper {

    private TypeArgumentsMapper() {
    }

    static Map<String, Class<?>> map(Class<?> type, Type genericType) {
        Map<String, Class<?>> result = new HashMap<>();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            TypeVariable<? extends Class<?>>[] typeParameters = type.getTypeParameters();
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

            for (int i = 0; i < actualTypeArguments.length; i++) {
                final Class<?> typeArgument = (Class<?>) actualTypeArguments[i];
                result.put(typeParameters[i].getName(), typeArgument);
            }
        }
        return result;
    }
}
