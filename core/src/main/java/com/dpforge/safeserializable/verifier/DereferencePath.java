package com.dpforge.safeserializable.verifier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DereferencePath {

    private final List<Field> fields;
    private final String representation;

    private DereferencePath(Builder builder) {
        fields = builder.fields;
        representation = builder.stringRepresentation.toString();
    }

    public String getRepresentation() {
        return representation;
    }

    @Override
    public String toString() {
        return representation;
    }

    public static class Builder {
        private final StringBuilder stringRepresentation = new StringBuilder();
        private final List<Field> fields = new ArrayList<>();

        public Builder addField(Field field) {
            if (fields.isEmpty()) {
                stringRepresentation.append(field.getDeclaringClass().getName());
            }
            fields.add(field);
            stringRepresentation.append(".").append(field.getName());
            if (field.getType().isArray()) {
                stringRepresentation.append("[]");
            }
            return this;
        }

        public DereferencePath build() {
            return new DereferencePath(this);
        }
    }
}
