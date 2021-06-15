package com.dpforge.safeserializable.handler;

import java.io.Serializable;
import com.dpforge.safeserializable.SafeSerializable;
import com.dpforge.safeserializable.verifier.DereferencePath;

/**
 * Represents result of {@link SafeSerializable} work
 */
public interface CheckHandler {

    /**
     * Called at the beginning of the safety checking process
     */
    default void onStart() {}

    /**
     * Called every time a non-serializable class is found
     * @param nonSerializableClass class that does not implement {@link Serializable}
     * @param dereferencePath path that leads to {@code nonSerializableClass} through the chain of dereferences
     */
    void onNonSerializableClassFound(Class<?> nonSerializableClass, DereferencePath dereferencePath);

    /**
     * Called at the end of the safety checking process
     */
    default void onFinish() {}

}
