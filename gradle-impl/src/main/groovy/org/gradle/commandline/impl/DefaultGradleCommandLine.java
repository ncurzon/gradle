/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.commandline.impl;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.gradle.*;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;
import org.gradle.api.GradleException;
import org.gradle.api.initialization.Settings;
import org.gradle.commandline.GradleCommandLine;
import static org.gradle.commandline.Options.*;
import org.gradle.execution.ProcessMode;
import org.gradle.util.GUtil;
import org.gradle.util.GradleVersion;
import org.gradle.util.WrapUtil;
import org.gradle.util.BootstrapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Hans Dockter
 */
public class DefaultGradleCommandLine implements GradleCommandLine {
    private static Logger logger = LoggerFactory.getLogger(GradleCommandLine.class);

    public static final String GRADLE_HOME_PROPERTY_KEY = "gradle.home";
    public static final String GRADLE_USER_HOME_PROPERTY_KEY = "gradle.user.home";
    public static final String DEFAULT_GRADLE_USER_HOME = System.getProperty("user.home") + "/.gradle";
    public final static String NL = System.getProperty("line.separator");

    private final GradleFactory gradleFactory;
    protected BuildResultLogger resultLogger;
    protected BuildExceptionReporter exceptionReporter;

    public DefaultGradleCommandLine(GradleFactory gradleFactory) {
        this.gradleFactory = gradleFactory;
    }

    public int runGradle(String[] args, Properties properties, Map<String, String> env, PrintStream out, PrintStream err) {
        resultLogger = new BuildResultLogger(logger);
        exceptionReporter = new BuildExceptionReporter(logger);

        final StartParameter startParameter = gradleFactory.createStartParameter();

        OptionParser parser = new OptionParser() {
            {
                acceptsAll(WrapUtil.toList(NO_DEFAULT_IMPORTS, "no-imports"), "Disable usage of default imports for build script files.");
                acceptsAll(WrapUtil.toList(NO_SEARCH_UPWARDS, "no-search-upward"), String.format("Don't search in parent folders for a %s file.", Settings.DEFAULT_SETTINGS_FILE));
                acceptsAll(WrapUtil.toList(MERGED_BUILD, "merged-build"), "Merge all tasks into a single build.");
                acceptsAll(WrapUtil.toList(CACHE, "cache"), "Specifies how compiled build scripts should be cached. Possible values are: 'rebuild', 'off', 'on'. Default value is 'on'").withRequiredArg().ofType(String.class);
                acceptsAll(WrapUtil.toList(VERSION, "version"), "Print version info.");
                acceptsAll(WrapUtil.toList(DEBUG, "debug"), "Log in debug mode (includes normal stacktrace).");
                acceptsAll(WrapUtil.toList(QUIET, "quiet"), "Log errors only.");
                acceptsAll(WrapUtil.toList(INFO, "info"), "Set log level to info.");
                acceptsAll(WrapUtil.toList(STACKTRACE, "stacktrace"), "Print out the stacktrace also for user exceptions (e.g. compile error).");
                acceptsAll(WrapUtil.toList(FULL_STACKTRACE, "full-stacktrace"), "Print out the full (very verbose) stacktrace for any exceptions.");
                acceptsAll(WrapUtil.toList(TASKS, "tasks"), "Show list of all available tasks and their dependencies.");
                acceptsAll(WrapUtil.toList(PROPERTIES, "properties"), "Show list of all available project properties.");
                acceptsAll(WrapUtil.toList(DEPENDENCIES, "dependencies"), "Show list of all project dependencies.");
                acceptsAll(WrapUtil.toList(PROJECT_DIR, "project-dir"), "Specifies the start dir for Gradle. Defaults to current dir.").withRequiredArg().ofType(String.class);
                acceptsAll(WrapUtil.toList(GRADLE_USER_HOME, "gradle-user-home"), "Specifies the gradle user home dir.").withRequiredArg().ofType(String.class);
                acceptsAll(WrapUtil.toList(PLUGIN_PROPERTIES_FILE, "plugin-properties-file"), "Specifies the plugin.properties file.").withRequiredArg().ofType(String.class);
                acceptsAll(WrapUtil.toList(DEFAULT_IMPORT_FILE, "default-import-file"), "Specifies the default import file.").withRequiredArg().ofType(String.class);
                acceptsAll(WrapUtil.toList(SETTINGS_FILE, "settingsfile"), String.format("Specifies the settings file name. Defaults to %s.", Settings.DEFAULT_SETTINGS_FILE)).withRequiredArg().ofType(String.class);
                acceptsAll(WrapUtil.toList(BUILD_FILE, "buildfile"), String.format("Specifies the build file name (also for subprojects). Defaults to %s.", Project.DEFAULT_BUILD_FILE)).withRequiredArg().ofType(String.class);
                acceptsAll(WrapUtil.toList(SYSTEM_PROP, "systemprop"), "Set system property of the JVM (e.g. -Dmyprop=myvalue).").withRequiredArg().ofType(String.class);
                acceptsAll(WrapUtil.toList(PROJECT_PROP, "projectprop"), "Set project property for the build script (e.g. -Pmyprop=myvalue).").withRequiredArg().ofType(String.class);
                acceptsAll(WrapUtil.toList(EMBEDDED_SCRIPT, "embedded"), "Specify an embedded build script.").withRequiredArg().ofType(String.class);
                acceptsAll(WrapUtil.toList(HELP, "?"), "Shows this help message");
            }
        };

        OptionSet options = null;
        try {
            try {
                options = parser.parse(args);
            } catch (OptionException e) {
                err.println(e.getMessage());
                parser.printHelpOn(err);
                err.flush();

                return 1;
            }

            exceptionReporter.setOptions(options);

            if (options.has(HELP)) {
                parser.printHelpOn(out);
                out.flush();

                return 0;
            }
        }
        catch ( IOException e ) {
            e.printStackTrace(err);
            err.flush();

            return 1;
        }

        if (options.has(VERSION)) {
            out.println(new GradleVersion().prettyPrint());
            out.flush();

            return 0;
        }

        String gradleHome = System.getProperty(GRADLE_HOME_PROPERTY_KEY);
        if (!GUtil.isTrue(gradleHome)) {
            err.println("The gradle.home property is not set. Please set it and try again.");
            err.flush();

            return 1;
        }
        startParameter.setGradleHomeDir(new File(gradleHome));

        if (options.has(NO_DEFAULT_IMPORTS)) {
            startParameter.setDefaultImportsFile(null);
        } else if (options.has(DEFAULT_IMPORT_FILE)) {
            startParameter.setDefaultImportsFile(new File(options.argumentOf(DEFAULT_IMPORT_FILE)));
        }

        if (options.has(SYSTEM_PROP)) {
            List<String> props = options.argumentsOf(SYSTEM_PROP);
            for (String keyValueExpression : props) {
                String[] elements = keyValueExpression.split("=");
                startParameter.getSystemPropertiesArgs().put(elements[0], elements.length == 1 ? "" : elements[1]);
            }
        }

        if (options.has(PROJECT_PROP)) {
            List<String> props = options.argumentsOf(PROJECT_PROP);
            for (String keyValueExpression : props) {
                String[] elements = keyValueExpression.split("=");
                startParameter.getProjectProperties().put(elements[0], elements.length == 1 ? "" : elements[1]);
            }
        }

        startParameter.setSearchUpwards(!options.has(NO_SEARCH_UPWARDS));

        if (options.has(PROJECT_DIR)) {
            startParameter.setCurrentDir(new File(options.argumentOf(PROJECT_DIR)));
            if (!startParameter.getCurrentDir().isDirectory()) {
                try {
                    err.println("Error: Directory " + startParameter.getCurrentDir().getCanonicalFile() + " does not exist!");
                    err.flush();
                }
                catch ( IOException e ) {
                    err.println("Error: Directory " + startParameter.getCurrentDir().getAbsolutePath() + " does not exist!");
                    err.flush();
                }

                return 1;
            }
        }

        if (options.hasArgument(GRADLE_USER_HOME)) {
            startParameter.setGradleUserHomeDir(new File(options.argumentOf(GRADLE_USER_HOME)));
        }
        if (options.hasArgument(BUILD_FILE)) {
            startParameter.setBuildFileName(options.argumentOf(BUILD_FILE));
        }
        if (options.hasArgument(SETTINGS_FILE)) {
            startParameter.setSettingsFileName(options.argumentOf(SETTINGS_FILE));
        }
        if (options.hasArgument(PLUGIN_PROPERTIES_FILE)) {
            startParameter.setPluginPropertiesFile(new File(options.argumentOf(PLUGIN_PROPERTIES_FILE)));
        }

        if (options.has(CACHE)) {
            try {
                startParameter.setCacheUsage(CacheUsage.fromString(options.valueOf(CACHE).toString()));
            } catch (InvalidUserDataException e) {
                err.println(e.getMessage());

                return 1;
            }
        }

        if (options.has(EMBEDDED_SCRIPT)) {
            if (options.has(BUILD_FILE) || options.has(NO_SEARCH_UPWARDS) || options.has(SETTINGS_FILE)) {
                err.println(String.format("Error: The -%s option can't be used together with the -%s, -%s or -%s options.", EMBEDDED_SCRIPT, BUILD_FILE, SETTINGS_FILE, NO_SEARCH_UPWARDS));
                err.flush();
                return 1;
            }
            startParameter.useEmbeddedBuildFile(options.argumentOf(EMBEDDED_SCRIPT));
        }

        if (options.has(TASKS) && options.has(PROPERTIES)) {
            err.println(String.format("Error: The -%s and -%s options cannot be used together.", TASKS, PROPERTIES));
            err.flush();

            return 1;
        }
        if (options.has(TASKS)) {
            startParameter.setProcessMode(ProcessMode.TASKS);
        } else if (options.has(PROPERTIES)) {
            startParameter.setProcessMode(ProcessMode.PROPERTIES);
        } else if (options.has(DEPENDENCIES)) {
            startParameter.setProcessMode(ProcessMode.DEPENDENCIES);
        } else {
            startParameter.setTaskNames(options.nonOptionArguments());
        }
        startParameter.setMergedBuild(options.has(MERGED_BUILD));

        try {
            startParameter.setLogLevel(getLogLevel(options, err));
        }
        catch ( InvalidUserDataException e ) {
            return 1;
        }

        try {
            final Gradle gradle = gradleFactory.newInstance(startParameter);

            gradle.addBuildListener(exceptionReporter);
            gradle.addBuildListener(resultLogger);

            final BuildResult buildResult = gradle.run();
            if (buildResult.getFailure() != null) {
                return 1;
            }
        } catch (Throwable e) {
            e.printStackTrace(err);
            err.flush();
            exceptionReporter.buildFinished(new DefaultBuildResult(null, e));
            return 1;
        }

        return 0;
    }

    private static String getLogLevel(OptionSet options, PrintStream err) {
        String logLevel = null;
        if (options.has(QUIET)) {
            logLevel = "QUIET";
        }
        if (options.has(INFO)) {
            quitWithErrorIfLogLevelAlreadyDefined(logLevel, INFO, err);
            logLevel = "INFO";
        }
        if (options.has(DEBUG)) {
            quitWithErrorIfLogLevelAlreadyDefined(logLevel, DEBUG, err);
            logLevel = "DEBUG";
        }
        if (logLevel == null) {
            logLevel = "LIFECYCLE";
        }
        return logLevel;
    }

    private static void quitWithErrorIfLogLevelAlreadyDefined(String logLevel, String option, PrintStream err) {
        if (logLevel != null) {
            err.println(String.format("Error: The log level is already defined by another option. Therefore the option %s is invalid.", option));
            err.flush();
            throw new InvalidUserDataException();
        }
    }
}
