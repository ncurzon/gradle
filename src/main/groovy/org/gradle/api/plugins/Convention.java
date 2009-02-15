package org.gradle.api.plugins;

import org.gradle.api.internal.DynamicObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tom Eyckmans
 */
public abstract class Convention implements DynamicObject {

    private final Map<ConventionValueName, ConventionValue> conventionValues = new HashMap<ConventionValueName, ConventionValue>();

    public <T> ConventionValue<T> getValue(ConventionValueName conventionValueName) {
        return conventionValues.get(conventionValueName);
    }

    public <T> ConventionValue<T> addValue(ConventionValue<T> conventionValue) {
        conventionValues.put(conventionValue.getName(), conventionValue);
        return conventionValue;
    }

}
