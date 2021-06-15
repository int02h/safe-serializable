package com.dpforge.safeserializable.verifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Verifies whether a class properly implements {@link Serializable}.
 * <p>
 * Proper implementation means all its fields and fields of its fields and etc are also implements {@link Serializable}.
 */
public class SerializableVerifier {

    public VerificationResult verifyClass(Class<?> clazz) {
        final Context context = new Context();
        final ClassInfo classInfo = new ClassInfo(clazz);
        if (classInfo.hasNonResolvableTypeArguments()) {
            // TODO handle somehow. Maybe add warning.
            // This makes no sense to check class if we don't know actual type arguments. This class can be
            // instantiated in any place in the code with any arguments that might be non-serializable. It is very
            // difficult to check all caller-side places.
            return new VerificationResult(Collections.emptyList());
        }
        verifyClass(context, classInfo);
        return new VerificationResult(context.errors);
    }

    private void verifyClass(Context context, ClassInfo classInfo) {
        if (classInfo.isPrimitiveOrWrapper() || classInfo.getWrappedClass() == String.class) {
            return;
        }

        if (classInfo.isArray()) {
            verifyClass(context, classInfo.getArrayItemClassInfo());
            return;
        }

        if (!classInfo.isSerializable()) {
            context.setNotSerializable(classInfo.getWrappedClass());
            return;
        }

        for (FieldInfo fieldInfo : classInfo.getDeclaredFields()) {
            context.startField(fieldInfo);
            verifyField(context, fieldInfo);
            context.endField();
        }

        ClassInfo superclassInfo = classInfo.getSuperclassInfo();
        while (superclassInfo != null) {
            verifyClass(context, superclassInfo);
            superclassInfo = superclassInfo.getSuperclassInfo();
        }
    }

    private void verifyField(Context context, FieldInfo fieldInfo) {
        if (fieldInfo.isStatic() || fieldInfo.isTransient()) {
            return;
        }
        ClassInfo classInfo = new ClassInfo(fieldInfo.getActualType(), fieldInfo.getTypeArguments());
        verifyClass(context, classInfo);
    }

    private static class Context {

        private final Stack<FieldInfo> fieldInfoStack = new Stack<>();
        private final List<VerificationError> errors = new ArrayList<>();

        void startField(FieldInfo fieldInfo) {
            fieldInfoStack.push(fieldInfo);
        }

        void endField() {
            fieldInfoStack.pop();
        }

        void setNotSerializable(Class<?> clazz) {
            DereferencePath.Builder builder = new DereferencePath.Builder();

            if (!fieldInfoStack.isEmpty()) {
                for (FieldInfo fieldInfo : fieldInfoStack) {
                    builder.addField(fieldInfo.getField());
                }
            }
            errors.add(new VerificationError(clazz, builder.build()));
        }
    }


}

