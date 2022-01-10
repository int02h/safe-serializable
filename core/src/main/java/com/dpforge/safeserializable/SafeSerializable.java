package com.dpforge.safeserializable;

import com.dpforge.safeserializable.collector.SerializableClassCollector;
import com.dpforge.safeserializable.handler.AssertionErrorCheckHandler;
import com.dpforge.safeserializable.handler.CheckHandler;
import com.dpforge.safeserializable.verifier.SerializableVerifier;
import com.dpforge.safeserializable.verifier.VerificationError;
import com.dpforge.safeserializable.verifier.VerificationResult;
import com.dpforge.safeserializable.verifier.VerificationWarning;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Provides runtime check that ensures all necessary classes properly implement {@link Serializable} interface.
 */
public class SafeSerializable {

    private final SerializableClassCollector serializableClassCollector;
    private final SerializableVerifier verifier = new SerializableVerifier();
    private final CheckHandler checkHandler;
    private final Set<Class<?>> interfacesWithSafeImplementations;

    private SafeSerializable(Builder builder) {
        serializableClassCollector = builder.serializableClassCollector;
        checkHandler = builder.checkHandler;
        interfacesWithSafeImplementations = builder.interfacesWithSafeImplementations;
    }

    /**
     * Performs the check
     */
    public void checkSafety() {
        checkHandler.onStart();
        for (Class<?> clazz : serializableClassCollector.collect()) {
            VerificationResult result = verifier.verifyClass(clazz);
            for (VerificationWarning warning : result.getWarnings()) {
                handleWarning(warning);
            }
            for (VerificationError error : result.getErrors()) {
                checkHandler.onNonSerializableClassFound(error.getNonSerializableClass(), error.getDereferencePath());
            }
        }
        checkHandler.onFinish();
    }

    private void handleWarning(VerificationWarning warning) {
        switch (warning.getType()) {
            case INTERFACE:
                handleInterfaceWarning(warning);
                break;
            default:
                throw new IllegalStateException("Illegal warning type: " + warning.getType());
        }
    }

    private void handleInterfaceWarning(VerificationWarning warning) {
        if (!interfacesWithSafeImplementations.contains(warning.getNonSerializableClass())) {
            checkHandler.onNonSerializableClassFound(warning.getNonSerializableClass(), warning.getDereferencePath());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private SerializableClassCollector serializableClassCollector;
        private CheckHandler checkHandler = new AssertionErrorCheckHandler();

        private final Set<Class<?>> interfacesWithSafeImplementations = new HashSet<>();

        /**
         * @param serializableClassCollector collector providing a list of classes that will be checked
         */
        public Builder serializableClassCollector(SerializableClassCollector serializableClassCollector) {
            this.serializableClassCollector = serializableClassCollector;
            return this;
        }

        /**
         * @param checkHandler handler that will control the check process and receive found errors
         */
        public Builder checkHandler(CheckHandler checkHandler) {
            this.checkHandler = checkHandler;
            return this;
        }

        /**
         * Add interface classes to a special list meaning they have implementations
         * that properly implement {@link Serializable}.
         *
         * @param interfaceClasses list of interface classes
         */
        public Builder addInterfacesWithSafeImplementations(Collection<Class<?>> interfaceClasses) {
            ensureAllInterfaces(interfaceClasses);
            interfacesWithSafeImplementations.addAll(interfaceClasses);
            return this;
        }

        /**
         * Add interface classes to a special list meaning they have implementations
         * that properly implement {@link Serializable}.
         *
         * @param interfaceClasses array of interface classes
         */
        public Builder addInterfacesWithSafeImplementations(Class<?>... interfaceClasses) {
            return addInterfacesWithSafeImplementations(Arrays.asList(interfaceClasses));
        }

        public SafeSerializable build() {
            Objects.requireNonNull(serializableClassCollector, "serializableClassCollector is not set");
            return new SafeSerializable(this);
        }

        private static void ensureAllInterfaces(Iterable<Class<?>> interfaceClasses) {
            for (Class<?> clazz : interfaceClasses) {
                if (!clazz.isInterface()) {
                    throw new IllegalArgumentException("Class " + clazz + " is not an interface");
                }
            }
        }
    }

}
