package com.dpforge.safeserializable.handler;

import com.dpforge.safeserializable.verifier.DereferencePath;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * {@link CheckHandler} implementation that throws {@link AssertionError} if there is at least one error. The message
 * in {@link AssertionError} contains information about classes that don't implement {@link Serializable} with
 * dereference paths leading to them.
 */
public class AssertionErrorCheckHandler extends MapBasedCheckHandler {

    @Override
    protected void onError(Map<Class<?>, List<DereferencePath>> errorMap) {
        throw new AssertionError(formatErrorMessage(errorMap));
    }

    private static String formatErrorMessage(Map<Class<?>, List<DereferencePath>> errorMap) {
        StringBuilder builder = new StringBuilder();
        errorMap.entrySet()
                .stream()
                .sorted(Comparator.comparing(o -> o.getKey().getName()))
                .forEach(entry -> {
                    builder.append("Class ")
                            .append(entry.getKey().getName())
                            .append(" does not implement ")
                            .append(Serializable.class.getName())
                            .append("\n");
                    builder.append("Dereference paths:\n");
                    for (DereferencePath dereferencePath : entry.getValue()) {
                        builder.append("- ").append(dereferencePath.getRepresentation()).append("\n");
                    }
                });
        return builder.toString();
    }
}
