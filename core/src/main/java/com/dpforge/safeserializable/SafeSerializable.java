package com.dpforge.safeserializable;

import java.io.Serializable;

import com.dpforge.safeserializable.collector.SerializableClassCollector;
import com.dpforge.safeserializable.handler.AssertionErrorCheckHandler;
import com.dpforge.safeserializable.handler.CheckHandler;
import com.dpforge.safeserializable.verifier.SerializableVerifier;
import com.dpforge.safeserializable.verifier.VerificationError;
import com.dpforge.safeserializable.verifier.VerificationResult;

/**
 * Provides runtime check that ensures all necessary classes properly implement {@link Serializable} interface.
 */
public class SafeSerializable {

    private final SerializableClassCollector classCollector;
    private final SerializableVerifier verifier = new SerializableVerifier();
    private final CheckHandler checkHandler;

    /**
     * Create instance that uses {@link AssertionErrorCheckHandler} as default {@link CheckHandler}
     *
     * @param classCollector collector providing a list of classes that will be checked
     */
    public SafeSerializable(SerializableClassCollector classCollector) {
        this(classCollector, new AssertionErrorCheckHandler());
    }

    /**
     * @param classCollector collector providing a list of classes that will be checked
     * @param checkHandler   handler that will control the check process and receive found errors
     */
    public SafeSerializable(SerializableClassCollector classCollector, CheckHandler checkHandler) {
        this.classCollector = classCollector;
        this.checkHandler = checkHandler;
    }

    /**
     * Performs the check
     */
    public void checkSafety() {
        checkHandler.onStart();
        for (Class<?> clazz : classCollector.collect()) {
            VerificationResult result = verifier.verifyClass(clazz);
            for (VerificationError error : result.getErrors()) {
                checkHandler.onNonSerializableClassFound(error.getNonSerializableClass(), error.getDereferencePath());
            }
        }
        checkHandler.onFinish();
    }

}
