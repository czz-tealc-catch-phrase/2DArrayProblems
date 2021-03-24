package com.chirs.co.app;

public class AssertionErrorStruct {
    Throwable t;
    Object input;
    Object actual;
    Object expected;

    public AssertionErrorStruct(Throwable t, Object input, Object expected, Object actual) {
        this.t = t;
        this.input = input;
        this.expected = expected;
        this.actual = actual;
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(
                "Message: %s\n" +
                "StackTrace:\n%s\n" +
                "Input: %s\n" +
                "Expected: %s\n" +
                "Actual: %s\n",
                t.getMessage(),
                printStackTrace(t.getStackTrace()),
                printIfNotNull(input),
                printIfNotNull(expected),
                printIfNotNull(actual)));
        return sb.toString();
    }

    private String printStackTrace(StackTraceElement[] stack) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stack) {
            sb.append("\t\t" + element.toString() + "\n");
        }
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private String printIfNotNull(Object o) {
        return o != null ? o.toString() : "null";
    }

    public static RuntimeException nope() {
        return new RuntimeException("Not Implemented");
    }
}
