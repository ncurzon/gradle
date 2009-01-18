package org.gradle;

import org.gradle.initialization.ISettingsFinder;
import org.gradle.initialization.IGradlePropertiesLoader;
import org.gradle.initialization.SettingsProcessor;
import org.gradle.initialization.BuildLoader;
import org.gradle.configuration.BuildConfigurer;

/**
 * @author Tom Eyckmans
 */
public interface Gradle {
    BuildResult run();

    StartParameter getStartParameter();

    ISettingsFinder getSettingsFinder();

    void setSettingsFinder(ISettingsFinder settingsFinder);

    IGradlePropertiesLoader getGradlePropertiesLoader();

    void setGradlePropertiesLoader(IGradlePropertiesLoader gradlePropertiesLoader);

    SettingsProcessor getSettingsProcessor();

    void setSettingsProcessor(SettingsProcessor settingsProcessor);

    BuildLoader getBuildLoader();

    void setBuildLoader(BuildLoader buildLoader);

    BuildConfigurer getBuildConfigurer();

    void setBuildConfigurer(BuildConfigurer buildConfigurer);

    void addBuildListener(BuildListener buildListener);
}
