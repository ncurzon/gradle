package org.gradle.initialization;

import org.gradle.api.initialization.ProjectDescriptor;
import org.gradle.api.internal.BuildInternal;
import org.gradle.api.internal.project.IProjectFactory;
import org.gradle.StartParameter;

import java.util.Map;

/**
 * @author Tom Eyckmans
 */
public interface BuildLoader {
    BuildInternal load(ProjectDescriptor rootProjectDescriptor, ClassLoader buildScriptClassLoader,
                              StartParameter startParameter,
                              Map<String, String> externalProjectProperties);

    IProjectFactory getProjectFactory();

    void setProjectFactory(IProjectFactory projectFactory);
}
