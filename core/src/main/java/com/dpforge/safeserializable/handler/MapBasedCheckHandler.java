package com.dpforge.safeserializable.handler;

import com.dpforge.safeserializable.verifier.DereferencePath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link CheckHandler} that accumulates all found dereference paths for every non-serializable class
 */
public abstract class MapBasedCheckHandler implements CheckHandler {

    private final Map<Class<?>, List<DereferencePath>> errorMap = new HashMap<>();

    /**
     * @return map that links non-serializable classes with all dereference path leading to them
     */
    public Map<Class<?>, List<DereferencePath>> getErrorMap() {
        return errorMap;
    }

    protected abstract void onError(Map<Class<?>, List<DereferencePath>> errorMap);

    protected void onSuccess() {
    }

    @Override
    public void onNonSerializableClassFound(Class<?> nonSerializableClass, DereferencePath dereferencePath) {
        List<DereferencePath> dereferencePaths = errorMap.computeIfAbsent(nonSerializableClass, key -> new ArrayList<>());
        dereferencePaths.add(dereferencePath);
    }

    @Override
    public final void onFinish() {
        if (!errorMap.isEmpty()) {
            onError(errorMap);
        } else {
            onSuccess();
        }
    }

}
