package com.ca.ms.cup.common.route;

/**
 *
 */
public class OriginalSourceContext {
    private static final ThreadLocal<String> context = new ThreadLocal<String>();

    public static void set(String originalSource) {
        String current = context.get();
        if (current != null && current.length() > 0) {
            return;
        }
        context.set(originalSource);
    }

    public static String get() {
        return context.get();
    }

    public static void remove() {
        context.remove();
    }

}
