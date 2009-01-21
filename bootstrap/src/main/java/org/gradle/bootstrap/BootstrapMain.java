package org.gradle.bootstrap;

import org.gradle.commandline.GradleCommandLine;
import org.gradle.bootstrap.HostApplication;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Map;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Tom Eyckmans
 */
public class BootstrapMain {
    public static void main(String[] args) {
        String bootStrapDebugValue = System.getProperty("gradle.bootstrap.debug");
        boolean bootStrapDebug = bootStrapDebugValue != null && !bootStrapDebugValue.toUpperCase().equals("FALSE");

        processGradleHome(bootStrapDebug);

        final HostApplication hostApplication = new HostApplication();
        int exitCode = -1;
        try {
            hostApplication.startApplication();

            final Object gradleCommandLine = hostApplication.getGradleCommandLine();

            final Method runGradleMethod = gradleCommandLine.getClass().getMethod("runGradle",
                    new Class[]{String[].class, Properties.class, Map.class, InputStream.class, PrintStream.class, PrintStream.class});

            exitCode = (Integer)runGradleMethod.invoke(gradleCommandLine, args, System.getProperties(), System.getenv(), System.in, System.out, System.err);
        }
        catch ( Exception e ) {
            e.printStackTrace();

            exitCode = 1;
        }
        finally {
            try {
                hostApplication.stopApplication();
            }
            catch ( Exception e ) {
                e.printStackTrace();
                
                if ( exitCode == -1 )
                    exitCode = 1;
            }
        }

        System.exit(exitCode);
    }

    private static String processGradleHome(boolean bootStrapDebug) {
        String gradleHome = System.getProperty("gradle.home");
        if (gradleHome == null) {
            gradleHome = System.getenv("GRADLE_HOME");
            if (gradleHome == null) {
                throw new RuntimeException("The gradle home must be set either via the system property gradle.home or via the environment variable GRADLE_HOME!");
            }
            if (bootStrapDebug) {
                System.out.println("Gradle Home is declared by environment variable GRADLE_HOME to: " + gradleHome);
            }
            System.setProperty("gradle.home", gradleHome);
        } else {
            if (bootStrapDebug) {
                System.out.println("Gradle Home is declared by system property gradle.home to: " + gradleHome);
            }
        }
        return gradleHome;
    }
}
