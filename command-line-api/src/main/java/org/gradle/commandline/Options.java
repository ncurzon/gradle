package org.gradle.commandline;

/**
 * @author Tom Eyckmans
 */
public interface Options {
    String NO_SEARCH_UPWARDS = "u";
    String PROJECT_DIR = "p";
    String PLUGIN_PROPERTIES_FILE = "l";
    String DEFAULT_IMPORT_FILE = "K";
    String BUILD_FILE = "b";
    String SETTINGS_FILE = "c";
    String TASKS = "t";
    String PROPERTIES = "r";
    String DEPENDENCIES = "n";
    String DEBUG = "d";
    String INFO = "i";
    String QUIET = "q";
    String FULL_STACKTRACE = "f";
    String STACKTRACE = "s";
    String SYSTEM_PROP = "D";
    String PROJECT_PROP = "P";
    String NO_DEFAULT_IMPORTS = "I";
    String GRADLE_USER_HOME = "g";
    String EMBEDDED_SCRIPT = "e";
    String VERSION = "v";
    String CACHE = "C";
    String HELP = "h";
    String MERGED_BUILD = "m";
}
