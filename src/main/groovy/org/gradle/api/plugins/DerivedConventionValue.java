package org.gradle.api.plugins;

/**
 * @author Tom Eyckmans
 */
public abstract class DerivedConventionValue<T> implements ConventionValue<T> {
    private final ConventionValueName name;

    public DerivedConventionValue(ConventionValueName name) {
        this.name = name;
    }

    public ConventionValueName getName() {
        return name;
    }

    public abstract T getValue();

    public void setValue(T value) {
        throw new UnsupportedOperationException("setValue not allowed on DerivedConventionValue");
    }
}
