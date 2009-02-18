package org.gradle.api.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.project.PluginRegistry;

import java.util.Map;

/**
 * @author Tom Eyckmans
 */
public class ProjectPlugin implements Plugin {

    public void apply(Project project, PluginRegistry pluginRegistry, Map<String, ?> customValues) {
        ProjectPluginConvention projectPluginConvention = new ProjectPluginConvention(project, customValues);
        
    }
}
