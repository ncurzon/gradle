package org.gradle.bootstrap.util;

import java.io.File;
import java.io.PrintStream;
import java.util.Map;
import java.util.Properties;

/**
 * @author Tom Eyckmans
 */
public class GradleHomeUtil {

    private static class GradleHomeGetSystemValueTracer implements SystemUtilGetSystemValueTracer
    {
        private final boolean debug;
        private final PrintStream debugOutputStream;

        private GradleHomeGetSystemValueTracer(boolean debug, PrintStream debugOutputStream) {
            this.debug = debug;
            this.debugOutputStream = debugOutputStream;
        }

        public void valueFromSystemProperties(String systemPropertyKey, String systemPropertiesValue) {
            if (debug) {
                debugOutputStream.println("Gradle Home is declared by system property gradle.home to: " + systemPropertiesValue);
            }
        }

        public void valueFromSystemEnv(String systemEnvKey, String systemEnvValue) {
            if (debug) {
                debugOutputStream.println("Gradle Home is declared by environment variable GRADLE_HOME to: " + systemEnvValue);
            }
        }
    }

    public static File determineGradleHome(Properties systemProperties, Map<String, String> systemEnv, boolean debug, PrintStream debugOutputStream)
    {
        final String gradleHome = SystemUtil.getSystemValue(
                systemProperties, systemEnv,
                "gradle.home", "GRADLE_HOME",
                null,
                new GradleHomeGetSystemValueTracer(debug, debugOutputStream)
        );

        if ( gradleHome == null )
            return null;
        else {
            System.setProperty("gradle.home", gradleHome);

            return new File(gradleHome);
        }
    }

    public static boolean validateGradleHome(File gradleHomeDirectory, boolean throwWhenInvalid)
    {
        boolean valid = true;

        if ( gradleHomeDirectory == null ) {
            if ( throwWhenInvalid )
                throw new RuntimeException("The gradle home must be set either via the system property gradle.home or via the environment variable GRADLE_HOME!");
            else
                valid = false;
        }

        if ( valid && !gradleHomeDirectory.exists() ) {
            if ( throwWhenInvalid )
                throw new RuntimeException("The set gradle home " + gradleHomeDirectory.getAbsolutePath() + " does not exist!");
            else
                valid = false;
        }

        if ( valid && !gradleHomeDirectory.isDirectory() ) {
            if ( throwWhenInvalid )
                throw new RuntimeException("The set gradle home " + gradleHomeDirectory.getAbsolutePath() + " is not a directory!");
            else
                valid = false;
        }

        // TODO check for default bundles ?

        return valid;
    }

}
