package com.dpforge.safeserializable.verifier;

import java.util.List;

public class VerificationResult {

    private final List<VerificationError> errors;

    VerificationResult(List<VerificationError> errors) {
        this.errors = errors;
    }

    public boolean isOk() {
        return errors.isEmpty();
    }

    public boolean isFail() {
        return !errors.isEmpty();
    }

    public List<VerificationError> getErrors() {
        return errors;
    }
}