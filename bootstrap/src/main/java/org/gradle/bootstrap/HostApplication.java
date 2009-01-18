package org.gradle.bootstrap;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.Util;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Bundle;
import org.osgi.util.tracker.ServiceTracker;
import org.gradle.commandline.GradleCommandLine;

import java.util.Properties;
import java.util.Enumeration;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;

/**
 * @author Tom Eyckmans
 */
public class HostApplication {

    private Felix m_felix = null;
    private ServiceTracker gradleCommandLineTracker;

    public HostApplication()
    {
    }

    public void startApplication() throws Exception
    {
        // Create a case-insensitive configuration property map.
        final Properties configProps = loadConfigProperties();
        // Create host activator;

        // Now create an instance of the framework with
        // our configuration properties and activator.
        m_felix = new Felix(configProps);

        // Now start Felix instance.
        m_felix.start();

        final BundleContext bundleContext = m_felix.getBundleContext();

        bundleContext.installBundle("file:lib/jopt-simple-2.4.1.jar$");

        /*final Bundle commandLineApiBundle = bundleContext.installBundle("file:command-line-api/build/command-line-api-0.6.jar");
        final Bundle gradleApiBundle = bundleContext.installBundle("file:gradle-api/build/gradle-api-0.6.jar");
        final Bundle commandLineImplBundle = bundleContext.installBundle("file:command-line-impl/build/command-line-impl-0.6.jar");
        final Bundle gradleImplBundle = bundleContext.installBundle("file:gradle-impl/build/gradle-impl-0.6.jar");

        commandLineApiBundle.start();
        gradleApiBundle.start();
        commandLineImplBundle.start();
        gradleImplBundle.start();

        printState(commandLineApiBundle);
        printState(gradleApiBundle);
        printState(commandLineImplBundle);
        printState(gradleImplBundle);*/

        gradleCommandLineTracker = new ServiceTracker(bundleContext, bundleContext.createFilter("(objectClass="+GradleCommandLine.class.getName()+")"), null);
        gradleCommandLineTracker.open();
    }

    private void printState(Bundle bundle)
    {
        System.out.println(bundle.getSymbolicName()+ " = " + getBundleState(bundle));
    }

    private String getBundleState(Bundle bundle)
    {
        switch(bundle.getState()) {
            case Bundle.INSTALLED:
                return "INSTALLED";
            case Bundle.RESOLVED:
                return "RESOLVED";
            case Bundle.ACTIVE:
                return "ACTIVE";
            case Bundle.STARTING:
                return "STARTING";
            case Bundle.UNINSTALLED:
                return "UNINSTALLED";
            case Bundle.STOPPING:
                return "STOPPING";
            default:
                return "UNKNOWN";
        }
    }

    public GradleCommandLine getGradleCommandLine()
    {
        while ( gradleCommandLineTracker.getService() != null ) {
            
        }
        return (GradleCommandLine) gradleCommandLineTracker.getService();
    }

    public void stopApplication() throws Exception
    {
        // Shut down the felix framework when stopping the
        // host application.
        m_felix.stop();
        m_felix.waitForStop(0);
    }

    /**
     * The property name used to specify an URL to the system
     * property file.
    **/
    public static final String SYSTEM_PROPERTIES_PROP = "felix.system.properties";
    /**
     * The default name used for the system properties file.
    **/
    public static final String SYSTEM_PROPERTIES_FILE_VALUE = "system.properties";
    /**
     * The property name used to specify an URL to the configuration
     * property file to be used for the created the framework instance.
    **/
    public static final String CONFIG_PROPERTIES_PROP = "felix.config.properties";
    /**
     * The default name used for the configuration properties file.
    **/
    public static final String CONFIG_PROPERTIES_FILE_VALUE = "config.properties";
    /**
     * Name of the configuration directory.
     */
    public static final String CONFIG_DIRECTORY = "conf";
    /**
     * <p>
     * Loads the configuration properties in the configuration property file
     * associated with the framework installation; these properties
     * are accessible to the framework and to bundles and are intended
     * for configuration purposes. By default, the configuration property
     * file is located in the <tt>conf/</tt> directory of the Felix
     * installation directory and is called "<tt>config.properties</tt>".
     * The installation directory of Felix is assumed to be the parent
     * directory of the <tt>felix.jar</tt> file as found on the system class
     * path property. The precise file from which to load configuration
     * properties can be set by initializing the "<tt>felix.config.properties</tt>"
     * system property to an arbitrary URL.
     * </p>
     * @return A <tt>Properties</tt> instance or <tt>null</tt> if there was an error.
    **/
    public static Properties loadConfigProperties()
    {
        // The config properties file is either specified by a system
        // property or it is in the conf/ directory of the Felix
        // installation directory.  Try to load it from one of these
        // places.

        // See if the property URL was specified as a property.
        URL propURL = null;
        String custom = System.getProperty(CONFIG_PROPERTIES_PROP);
        if (custom != null)
        {
            try
            {
                propURL = new URL(custom);
            }
            catch (MalformedURLException ex)
            {
                System.err.print("Main: " + ex);
                return null;
            }
        }
        else
        {
            // Determine where the configuration directory is by figuring
            // out where felix.jar is located on the system class path.
            File confDir = null;
            String classpath = System.getProperty("java.class.path");
            int index = classpath.toLowerCase().indexOf("felix.jar");
            int start = classpath.lastIndexOf(File.pathSeparator, index) + 1;
            if (index >= start)
            {
                // Get the path of the felix.jar file.
                String jarLocation = classpath.substring(start, index);
                // Calculate the conf directory based on the parent
                // directory of the felix.jar directory.
                confDir = new File(
                    new File(new File(jarLocation).getAbsolutePath()).getParent(),
                    CONFIG_DIRECTORY);
            }
            else
            {
                // Can't figure it out so use the current directory as default.
                confDir = new File(System.getProperty("user.dir"), CONFIG_DIRECTORY);
            }

            try
            {
                propURL = new File(confDir, CONFIG_PROPERTIES_FILE_VALUE).toURL();
            }
            catch (MalformedURLException ex)
            {
                System.err.print("Main: " + ex);
                return null;
            }
        }

        // Read the properties file.
        Properties props = new Properties();
        InputStream is = null;
        try
        {
            // Try to load config.properties.
            is = propURL.openConnection().getInputStream();
            props.load(is);
            is.close();
        }
        catch (Exception ex)
        {
            // Try to close input stream if we have one.
            try
            {
                if (is != null) is.close();
            }
            catch (IOException ex2)
            {
                // Nothing we can do.
            }

            return null;
        }

        // Perform variable substitution for system properties.
        for (Enumeration e = props.propertyNames(); e.hasMoreElements(); )
        {
            String name = (String) e.nextElement();
            props.setProperty(name, Util.substVars(props.getProperty(name), name, null, props));
        }

        return props;
    }
}
