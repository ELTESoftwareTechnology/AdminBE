package com.app.util;

public class ExceptionUtil {
    public static boolean isCausedBy(Class<? extends Throwable> expected, Throwable exc) {
        return expected.isInstance(exc) || (
                exc != null && isCausedBy(expected, exc.getCause())
        );
    }
}
