package org.gradle.bootstrap;

import org.apache.felix.framework.Felix;
import org.apache.commons.io.FileUtils;
import org.osgi.framework.*;
import org.osgi.framework.launch.Framework;
import org.gradle.commandline.GradleCommandLine;
import org.gradle.bootstrap.util.OsgiConfigUtil;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

/**
 * @author Tom Eyckmans
 */
public class HostApplication {

    private final File gradleHomeDirectory;
    private Framework osgiFramework = null;

    public HostApplication(File gradleHomeDirectory)
    {
        this.gradleHomeDirectory = gradleHomeDirectory;
    }

    public void startApplication(Properties systemProperties, Map<String, String> systemEnv) throws Exception
    {
        // Create a case-insensitive configuration property map.
        File osgiConfigFile = OsgiConfigUtil.determineOsgiConfigFile(systemProperties, systemEnv);
        if ( osgiConfigFile == null ) {
            osgiConfigFile = new File(HostApplication.class.getResource("/org/gradle/boostrap/default_osgi_config.properties").getFile());
        }
        
        final Properties gradleOsgiConfig = new Properties();

        gradleOsgiConfig.load(new BufferedInputStream(new FileInputStream(osgiConfigFile)));

        final Properties osgiContainerConfig  = new Properties();
        final List<String> toInstallBundles = new ArrayList<String>();

        final Enumeration osgiConfigKeys = gradleOsgiConfig.propertyNames();
        while ( osgiConfigKeys.hasMoreElements() ) {
            final String currentKey = (String) osgiConfigKeys.nextElement();
            final String currentValue = gradleOsgiConfig.getProperty(currentKey);

            if ( currentKey.startsWith("bundle") ) {
                if ( currentValue.startsWith("file:") ) {
                    toInstallBundles.add(currentValue);
                }
                else {
                    toInstallBundles.add("file:" + new File(gradleHomeDirectory, currentValue).getAbsolutePath());
                }
            }
            else {
                osgiContainerConfig.setProperty(currentKey, currentValue);
            }
        }

        final File frameworkStorage = new File(new File(systemProperties.getProperty("user.dir"), ".gradle"), "osgi-framework-cache");

        FileUtils.deleteDirectory(frameworkStorage);

        osgiContainerConfig.put(Constants.FRAMEWORK_STORAGE, frameworkStorage.getAbsolutePath());

        // Create host activator;

        // Now create an instance of the framework with
        // our configuration properties and activator.
        osgiFramework = new Felix(osgiContainerConfig);

        // Now start Felix instance.
        osgiFramework.start();

        final BundleContext bundleContext = osgiFramework.getBundleContext();

        final List<Bundle> installedBundles = new ArrayList<Bundle>();

        for ( String toInstallBundle : toInstallBundles ) {
            final Bundle installedBundle = bundleContext.installBundle(toInstallBundle);

            printState(installedBundle);

            installedBundles.add(installedBundle);
        }

        for ( final Bundle installedBundle : installedBundles ) {
            installedBundle.start();

            printState(installedBundle);
        }
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

    public Object getGradleCommandLine()
    {
        final BundleContext bundleContext = osgiFramework.getBundleContext();
        final ServiceReference serviceRef = bundleContext.getServiceReference(GradleCommandLine.class.getName());

        return bundleContext.getService(serviceRef);
    }

    public void stopApplication() throws Exception
    {
        // Shut down the osgiFramework framework when stopping the
        // host application.
        osgiFramework.stop();
        osgiFramework.waitForStop(0);
    }
}
