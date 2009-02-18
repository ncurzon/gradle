package org.gradle.api.plugins;

import org.gradle.api.internal.DynamicObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tom Eyckmans
 */
public abstract class Convention implements DynamicObject {

    protected final Map<ConventionValueName, ConventionValue> conventionValues = new HashMap<ConventionValueName, ConventionValue>();
    private final Map<String, ConventionValue> strConventionValues = new HashMap<String, ConventionValue>();

    public boolean hasConventionValue(ConventionValueName conventionValueName) {
        return conventionValues.containsKey(conventionValueName);
    }

    public boolean hasConventionValue(String conventionValueName) {
        return strConventionValues.containsKey(conventionValueName);
    }

    public <T> ConventionValue<T> getConventionValue(ConventionValueName conventionValueName) {
        return conventionValues.get(conventionValueName);
    }

    public <T> ConventionValue<T> getConventionValue(String conventionValueName) {
        return strConventionValues.get(conventionValueName);
    }

    public <T> ConventionValue<T> defineConventionValue(ConventionValue<T> conventionValue) {
        conventionValues.put(conventionValue.getName(), conventionValue);
        strConventionValues.put(conventionValue.getName().toString(), conventionValue);
        return conventionValue;
    }

}
