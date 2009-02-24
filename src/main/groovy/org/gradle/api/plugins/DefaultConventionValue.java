package org.gradle.api.plugins;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tom Eyckmans
 */
public class DefaultConventionValue<T> implements ConventionValue<T> {
    private final ConventionValueName<T> name;
    private final Lock valueLock;
    private T value;

    public DefaultConventionValue(ConventionValueName<T> name) {
        this(name, null);
    }

    public DefaultConventionValue(ConventionValueName<T> name, T value) {
        this.name = name;
        this.valueLock = new ReentrantLock();
        this.value = value;
    }

    public ConventionValueName<T> getName() {
        return name;
    }

    public T getValue() {
        valueLock.lock();
        try {
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
