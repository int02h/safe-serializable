package com.dpforge.safeserializable.verifier;

public class VerificationWarning {

    private final Type type;
    private final Class<?> clazz;
    private final DereferencePath dereferencePath;

    VerificationWarning(Type type, Class<?> clazz, DereferencePath dereferencePath) {
        this.type = type;
        this.clazz = clazz;
        this.dereferencePath = dereferencePath;
    }

    public Type getType() {
        return type;
    }

    public Class<?> getNonSerializableClass() {
        return clazz;
    }

    public DereferencePath getDereferencePath() {
        return dereferencePath;
    }

    public enum Type {
        INTERFACE
    }

}
