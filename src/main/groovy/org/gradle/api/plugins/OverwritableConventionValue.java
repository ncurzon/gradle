package org.gradle.api.plugins;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tom Eyckmans
 */
public class OverwritableConventionValue<T> implements ConventionValue<T>{
    private final ConventionValue<T> defaultValue;
    private final Lock valueLock;
    private T value;

    public OverwritableConventionValue(ConventionValue<T> defaultValue) {
        this.defaultValue = defaultValue;
        this.valueLock = new ReentrantLock();
    }

    public ConventionValueName getName() {
        return defaultValue.getName();
    }

    public T getValue() {
        valueLock.lock();
        try {
            T value = this.value;

            if ( value == null )
                return defaultValue.getValue();
            else
                return value;
        }
        finally {
            valueLock.unlock();
        }
    }

    public void setValue(T value) {
        valueLock.lock();
        try {
            this.value = value;
        }
        finally {
            valueLock.unlock();
        }
    }
}
