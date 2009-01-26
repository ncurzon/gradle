package org.gradle;

import org.gradle.api.initialization.Settings;

/**
 * @author Tom Eyckmans
 */
public interface BuildResult {
    Settings getSettings();

    Throwable getFailure();

    void rethrowFailure();
}
