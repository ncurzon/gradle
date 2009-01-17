package org.gradle;

import org.gradle.groovy.scripts.ScriptSource;
import org.gradle.execution.BuildExecuter;
import org.gradle.execution.ProcessMode;
import org.gradle.api.logging.LogLevel;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Tom Eyckmans
 */
public interface StartParameter {
    StartParameter newInstance();

    StartParameter newBuild();

    String getBuildFileName();

    void setBuildFileName(String buildFileName);

    File getGradleHomeDir();

    void setGradleHomeDir(File gradleHomeDir);

    ScriptSource getBuildScriptSource();

    ScriptSource getSettingsScriptSource();

    void setSettingsScriptSource(ScriptSource settingsScriptSource);

    StartParameter useEmbeddedBuildFile(String buildScript);

    BuildExecuter getBuildExecuter();

//    void setBuildExecuter(BuildExecuter buildExecuter);
    void setProcessMode(ProcessMode processMode);

    List<String> getTaskNames();

    void setTaskNames(List<String> taskNames);

    File getCurrentDir();

    void setCurrentDir(File currentDir);

    boolean isSearchUpwards();

    void setSearchUpwards(boolean searchUpwards);

    Map<String, String> getProjectProperties();

    void setProjectProperties(Map<String, String> projectProperties);

    Map<String, String> getSystemPropertiesArgs();

    void setSystemPropertiesArgs(Map<String, String> systemPropertiesArgs);

    File getGradleUserHomeDir();

    void setGradleUserHomeDir(File gradleUserHomeDir);

    File getDefaultImportsFile();

    void setDefaultImportsFile(File defaultImportsFile);

    File getPluginPropertiesFile();

    void setPluginPropertiesFile(File pluginPropertiesFile);

    CacheUsage getCacheUsage();

    void setCacheUsage(CacheUsage cacheUsage);

    String getSettingsFileName();

    void setSettingsFileName(String settingsFileName);

    boolean isMergedBuild();

    void setMergedBuild(boolean merged);

    LogLevel getLogLevel();

    void setLogLevel(LogLevel logLevel);
}
