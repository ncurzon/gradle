package org.gradle.api.plugins;

import org.gradle.api.Project;

import java.io.File;
import java.util.Map;

/**
 * @author Tom Eyckmans
 */
public class ProjectPluginConvention extends AbstractPluginConvention {

    public static class ValueNames {
        public static final ConventionValueName<String> defaultBuildFile = new ConventionValueName<String>();
        public static final ConventionValueName<String> embeddedScriptId = new ConventionValueName<String>();
        public static final ConventionValueName<String> defaultArchivesTaskBaseName = new ConventionValueName<String>();
        public static final ConventionValueName<String> hierarchyPathSeparator = new ConventionValueName<String>();
        public static final ConventionValueName<String> buildDirName = new ConventionValueName<String>();
        public static final ConventionValueName<String> gradlePropertiesName = new ConventionValueName<String>();
        public static final ConventionValueName<String> systemPropPrefix = new ConventionValueName<String>();
        public static final ConventionValueName<String> tmpDirName = new ConventionValueName<String>();
        public static final ConventionValueName<String> cacheDirName = new ConventionValueName<String>();
        public static final ConventionValueName<String> defaultGroup = new ConventionValueName<String>();
        public static final ConventionValueName<String> defaultVersion = new ConventionValueName<String>();

        //derived
        public static final ConventionValueName<File> rootDir = new ConventionValueName<File>();
        public static final ConventionValueName<File> projectDir = new ConventionValueName<File>();
        public static final ConventionValueName<File> buildDir = new ConventionValueName<File>();
        public static final ConventionValueName<File> tmpDir = new ConventionValueName<File>();
        public static final ConventionValueName<File> cacheDir = new ConventionValueName<File>();
    }

    protected ProjectPluginConvention(final Project project, Map<String, ?> customValues) {
        super(project, customValues);

        final ConventionValue<String> defaultBuildFile = addStringValue(ValueNames.defaultBuildFile, "build.gradle");
        final ConventionValue<String> embeddedScriptId = addStringValue(ValueNames.embeddedScriptId, "embedded_script");
        final ConventionValue<String> defaultArchivesTaskBaseName = addStringValue(ValueNames.defaultArchivesTaskBaseName, "archive");
        final ConventionValue<String> hierarchyPathSeparator = addStringValue(ValueNames.hierarchyPathSeparator, ":");
        final ConventionValue<String> buildDirName = addStringValue(ValueNames.buildDirName, "build");
        final ConventionValue<String> gradlePropertiesName = addStringValue(ValueNames.gradlePropertiesName, "gradle.properties");
        final ConventionValue<String> systemPropPrefix = addStringValue(ValueNames.systemPropPrefix, "systemProp");
        final ConventionValue<String> tmpDirName = addStringValue(ValueNames.tmpDirName, ".gradle");
        final ConventionValue<String> cacheDirName = addStringValue(ValueNames.cacheDirName, "cache");
        final ConventionValue<String> defaultGroup = addStringValue(ValueNames.defaultGroup, "unspecified");
        final ConventionValue<String> defaultVersion = addStringValue(ValueNames.defaultVersion, "unspecified");
        final ConventionValue<File> rootDir = addValue(new DerivedConventionValue<File>(ValueNames.rootDir) {
            @Override
            public File getValue() {
                return project.getRootDir();
            }
        });
        final ConventionValue<File> projectDir = addValue(new DerivedConventionValue<File>(ValueNames.projectDir) {
            @Override
            public File getValue() {
                return project.getProjectDir();
            }
        });
        final ConventionValue<File> buildDir = addDerivedFileValue(ValueNames.buildDir, projectDir, buildDirName);
        final ConventionValue<File> tmpDir = addDerivedFileValue(ValueNames.tmpDir, projectDir, tmpDirName);
        final ConventionValue<File> cacheDir = addDerivedFileValue(ValueNames.cacheDir, tmpDir, cacheDirName);
    }
}
