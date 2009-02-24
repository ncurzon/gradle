package org.gradle.api.plugins;

/**
 * @author Tom Eyckmans
 */
public interface ConventionValue<T> {

    ConventionValueName<T> getName();

    T getValue();

    void setValue(T value);
}
