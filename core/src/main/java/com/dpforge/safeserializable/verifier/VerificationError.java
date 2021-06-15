package com.dpforge.safeserializable.verifier;

public class VerificationError {

    private final Class<?> clazz;
    private final DereferencePath dereferencePath;

    VerificationError(Class<?> clazz, DereferencePath dereferencePath) {
        this.clazz = clazz;
        this.dereferencePath = dereferencePath;
    }

    public Class<?> getNonSerializableClass() {
        return clazz;
    }

    public DereferencePath getDereferencePath() {
        return dereferencePath;
    }
}
