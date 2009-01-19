package org.gradle.impl.api.internal

class MissingMethodException extends groovy.lang.MissingMethodException {
    private final String displayName;

    public MissingMethodException(String displayName, String name, Object... arguments) {
        super(name, null, arguments);
        this.displayName = displayName;
    }

    public String getMessage() {
        return String.format("Could not find method %s() for arguments %s on %s.", getMethod(), Arrays.toString(
                getArguments()), displayName);
    }
}