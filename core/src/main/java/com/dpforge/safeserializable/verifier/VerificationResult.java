package com.dpforge.safeserializable.verifier;

import java.util.Collections;
import java.util.List;

public class VerificationResult {

    private final List<VerificationError> errors;
    private final List<VerificationWarning> warnings;

    VerificationResult() {
        this(Collections.emptyList(), Collections.emptyList());
    }

    VerificationResult(List<VerificationError> errors, List<VerificationWarning> warnings) {
        this.errors = errors;
        this.warnings = warnings;
    }

    public boolean isOk() {
        return errors.isEmpty() && warnings.isEmpty();
    }

    public List<VerificationError> getErrors() {
        return errors;
    }

    public List<VerificationWarning> getWarnings() {
        return warnings;
    }
}