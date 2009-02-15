package org.gradle.api.plugins;

/**
 * @author Tom Eyckmans
 */
public interface ConventionValue<T> {
    ConventionValueName getName();

    T getValue();

    void setValue(T value);
}
