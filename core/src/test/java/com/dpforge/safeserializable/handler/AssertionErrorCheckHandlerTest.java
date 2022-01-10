package com.dpforge.safeserializable.handler;

import com.dpforge.safeserializable.verifier.DereferencePath;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class AssertionErrorCheckHandlerTest {

    @Test
    public void class_name_order() {
        AssertionErrorCheckHandler report = new AssertionErrorCheckHandler();
        report.onStart();
        report.onNonSerializableClassFound(C.class, testDereferencePath(C.class));
        report.onNonSerializableClassFound(B.class, testDereferencePath(B.class));
        report.onNonSerializableClassFound(A.class, testDereferencePath(A.class));

        assertMessage(
                report,
                "Class com.dpforge.safeserializable.handler.AssertionErrorCheckHandlerTest$A does not implement java.io.Serializable\n" +
                        "Dereference paths:\n" +
                        "- com.dpforge.safeserializable.handler.AssertionErrorCheckHandlerTest$A.a\n" +
                        "\n" +
                        "Class com.dpforge.safeserializable.handler.AssertionErrorCheckHandlerTest$B does not implement java.io.Serializable\n" +
                        "Dereference paths:\n" +
                        "- com.dpforge.safeserializable.handler.AssertionErrorCheckHandlerTest$B.b\n" +
                        "\n" +
                        "Class com.dpforge.safeserializable.handler.AssertionErrorCheckHandlerTest$C does not implement java.io.Serializable\n" +
                        "Dereference paths:\n" +
                        "- com.dpforge.safeserializable.handler.AssertionErrorCheckHandlerTest$C.c\n"
        );

    }

    @Test
    public void many_dereference_paths() {
        AssertionErrorCheckHandler report = new AssertionErrorCheckHandler();
        report.onStart();
        report.onNonSerializableClassFound(A.class, testDereferencePath(A.class, 0));
        report.onNonSerializableClassFound(B.class, testDereferencePath(B.class, 0));
        report.onNonSerializableClassFound(A.class, testDereferencePath(A.class, 1));
        report.onNonSerializableClassFound(B.class, testDereferencePath(B.class, 1));

        assertMessage(
                report,
                "Class com.dpforge.safeserializable.handler.AssertionErrorCheckHandlerTest$A does not implement java.io.Serializable\n" +
                        "Dereference paths:\n" +
                        "- com.dpforge.safeserializable.handler.AssertionErrorCheckHandlerTest$A.a\n" +
                        "- com.dpforge.safeserializable.handler.AssertionErrorCheckHandlerTest$A.a2\n" +
                        "\n" +
                        "Class com.dpforge.safeserializable.handler.AssertionErrorCheckHandlerTest$B does not implement java.io.Serializable\n" +
                        "Dereference paths:\n" +
                        "- com.dpforge.safeserializable.handler.AssertionErrorCheckHandlerTest$B.b\n" +
                        "- com.dpforge.safeserializable.handler.AssertionErrorCheckHandlerTest$B.b2\n"
        );
    }

    @Test
    public void no_errors() {
        AssertionErrorCheckHandler report = new AssertionErrorCheckHandler();
        report.onStart();
        report.onFinish();
    }

    private static DereferencePath testDereferencePath(Class<?> clazz) {
        return testDereferencePath(clazz, 0);
    }

    private static DereferencePath testDereferencePath(Class<?> clazz, int fieldIndex) {
        return new DereferencePath.Builder()
                .addField(clazz.getDeclaredFields()[fieldIndex])
                .build();
    }

    private static void assertMessage(AssertionErrorCheckHandler report, String message) {
        try {
            report.onFinish();
            fail("No exception thrown");
        } catch (AssertionError error) {
            assertThat(error.getMessage(), is(message));
        }
    }

    private static class A {
        String a;
        boolean a2;
    }

    private static class B {
        int b;
        short b2;
    }

    private static class C {
        char c;
    }
}