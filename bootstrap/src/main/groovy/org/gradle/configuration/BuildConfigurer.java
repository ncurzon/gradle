package org.gradle.configuration;

import org.gradle.api.Project;

/**
 * @author Tom Eyckmans
 */
public interface BuildConfigurer {
    void process(Project rootProject);
}
