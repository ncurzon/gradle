package org.gradle.commandline;

import java.util.Properties;
import java.util.Map;
import java.io.PrintStream;
import java.io.InputStream;

/**
 * @author Tom Eyckmans
 */
public interface GradleCommandLine {
    int runGradle(String[] args, Properties properties, Map<String, String> env, InputStream in, PrintStream out, PrintStream err);
}
