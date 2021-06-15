package com.dpforge.safeserializable.verifier;

import org.junit.Test;

import java.io.Serializable;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;

public class SerializableVerifierTest {

    @Test
    public void verify_class_itself() {
        VerificationError error = verifySingleError(NonSerializable.class);
        assertThat(error.getNonSerializableClass(), sameInstance(NonSerializable.class));
        assertThat(error.getDereferencePath().getRepresentation(), is(""));
    }

    @Test
    public void verify_field_in_non_serializable() {
        VerificationError error = verifySingleError(FailA.class);
        assertThat(error.getNonSerializableClass(), sameInstance(NonSerializable.class));
        assertThat(
                error.getDereferencePath().getRepresentation(),
                is("com.dpforge.safeserializable.verifier.SerializableVerifierTest$FailA.failB.nonSerializable")
        );
    }

    @Test
    public void verify_primitives() {
        verifyOk(OkPrimitives.class);
    }

    @Test
    public void verify_primitive_wrappers() {
        verifyOk(OkWrappers.class);
    }

    @Test
    public void verify_strings() {
        verifyOk(OkString.class);
    }

    @Test
    public void verify_array() {
        verifyOk(OkArray.class);
    }

    @Test
    public void verify_array_fail() {
        VerificationError error = verifySingleError(FailArray.class);
        assertThat(error.getNonSerializableClass(), sameInstance(NonSerializable.class));
        assertThat(
                error.getDereferencePath().getRepresentation(),
                is("com.dpforge.safeserializable.verifier.SerializableVerifierTest$FailArray.array[].nonSerializable")
        );
    }

    @Test
    public void verify_generic_field() {
        verifyOk(OkGenericField.class);
    }

    @Test
    public void verify_generic_superclass() {
        verifyOk(OkGenericSuperclass.class);
    }

    @Test
    public void verify_transient() {
        verifyOk(TransientField.class);
    }

    @Test
    public void verify_static() {
        verifyOk(StaticField.class);
    }

    @Test
    public void verify_generic_without_actual_type_info() {
        // Just to check if code doesn't crash. Please refer comments inside SerializableVerifier
        verifyOk(SomeGeneric.class);
    }

    private void verifyOk(Class<?> clazz) {
        assertThat(new SerializableVerifier().verifyClass(clazz).isOk(), is(true));
    }

    private VerificationError verifySingleError(Class<?> clazz) {
        VerificationResult result = new SerializableVerifier().verifyClass(clazz);
        assertThat(result.isFail(), is(true));
        assertThat(result.getErrors().size(), is(1));
        return result.getErrors().get(0);
    }

    private static class NonSerializable {
    }

    private static class FailA implements Serializable {
        FailB failB;
    }

    private static class FailB implements Serializable {
        NonSerializable nonSerializable;
    }

    private static class OkPrimitives implements Serializable {
        int a;
        float b;
        boolean c;
    }

    private static class OkWrappers implements Serializable {
        Integer a;
        Short b;
        Character c;
        Boolean d;
    }

    private static class OkString implements Serializable {
        String s;
    }

    private static class OkArray implements Serializable {
        OkWrappers[] array;
    }

    private static class FailArray implements Serializable {
        FailB[] array;
    }

    static class SomeGeneric<T1, T2> implements Serializable {
        T1 data1;
        T2 data2;
    }

    private static class OkGenericField implements Serializable {
        SomeGeneric<OkPrimitives, OkWrappers> someGeneric;
    }

    private static class OkGenericSuperclass implements Serializable {
        Foo foo;

        static class Foo extends SomeGeneric<OkString, OkString> {
        }
    }

    private static class TransientField implements Serializable {
        transient NonSerializable nonSerializable;
    }

    private static class StaticField implements Serializable {
        static NonSerializable nonSerializable;
    }
}