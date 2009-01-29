package org.gradle.bootstrap;

import org.gradle.bootstrap.util.GradleHomeUtil;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Map;
import java.io.PrintStream;
import java.io.File;

/**
 * @author Tom Eyckmans
 */
public class BootstrapMain {
    public static void main(String[] args) {
        final File gradleHomeDirectory = processGradleHome();

        final HostApplication hostApplication = new HostApplication(gradleHomeDirectory);
        int exitCode = -1;
        try {
            hostApplication.startApplication(System.getProperties(), System.getenv());

            final Object gradleCommandLine = hostApplication.getGradleCommandLine();

            final Method runGradleMethod = gradleCommandLine.getClass().getMethod("runGradle", String[].class, Properties.class, Map.class, PrintStream.class, PrintStream.class);

            exitCode = (Integer)runGradleMethod.invoke(gradleCommandLine, args, System.getProperties(), System.getenv(), System.out, System.err);
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

    private static File processGradleHome() {
        final boolean bootStrapDebug = determineBootstrapDebug();

        final File gradleHomeDirectory = GradleHomeUtil.determineGradleHome(System.getProperties(), System.getenv(), bootStrapDebug, System.out);

        GradleHomeUtil.validateGradleHome(gradleHomeDirectory, true);

        return gradleHomeDirectory;
    }

    private static boolean determineBootstrapDebug()
    {
        final String bootStrapDebugValue = System.getProperty("gradle.bootstrap.debug");

        return bootStrapDebugValue != null && !bootStrapDebugValue.toUpperCase().equals("FALSE");
    }
}
