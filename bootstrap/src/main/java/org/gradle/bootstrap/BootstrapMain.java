package org.gradle.bootstrap;

import org.gradle.commandline.GradleCommandLine;
import org.gradle.bootstrap.HostApplication;

/**
 * @author Tom Eyckmans
 */
public class BootstrapMain {
    public static void main(String[] args) {
        String bootStrapDebugValue = System.getProperty("gradle.bootstrap.debug");
        boolean bootStrapDebug = bootStrapDebugValue != null && !bootStrapDebugValue.toUpperCase().equals("FALSE");

        processGradleHome(bootStrapDebug);

        final HostApplication hostApplication = new HostApplication();
        int exitCode = 1;
        try {
            hostApplication.startApplication();

            final GradleCommandLine gradleCommandLine = hostApplication.getGradleCommandLine();

            exitCode = gradleCommandLine.runGradle(args, System.getProperties(), System.getenv(), System.in, System.out, System.err);
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
        finally {
            try {
                hostApplication.stopApplication();
                System.exit(exitCode);
            }
            catch ( Exception e ) {
                e.printStackTrace();
                System.exit(1);
            }
        }
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
