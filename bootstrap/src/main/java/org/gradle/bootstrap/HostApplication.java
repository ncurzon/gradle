package org.gradle.bootstrap;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.Util;
import org.apache.commons.io.FileUtils;
import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;
import org.gradle.commandline.GradleCommandLine;

import java.util.*;
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
        //final Properties configProps = loadConfigProperties();
        final Properties configProps  = new Properties();
        configProps.put("felix.embedded.execution", "true");
        configProps.put("org.osgi.framework.bootdelegation", "" +
//                " org.slf4j, " +
//                " org.slf4j.impl, " +
//                " org.slf4j.helpers, " +
                "  javax.accessibility,\n" +
                " javax.activity,\n" +
                " javax.crypto,\n" +
                " javax.crypto.interfaces,\n" +
                " javax.crypto.spec,\n" +
                " javax.imageio,\n" +
                " javax.imageio.event,\n" +
                " javax.imageio.metadata,\n" +
                " javax.imageio.plugins.bmp,\n" +
                " javax.imageio.plugins.jpeg,\n" +
                " javax.imageio.spi,\n" +
                " javax.imageio.stream,\n" +
                " javax.management,\n" +
                " javax.management.loading,\n" +
                " javax.management.modelmbean,\n" +
                " javax.management.monitor,\n" +
                " javax.management.openmbean,\n" +
                " javax.management.relation,\n" +
                " javax.management.remote,\n" +
                " javax.management.remote.rmi,\n" +
                " javax.management.timer,\n" +
                " javax.naming,\n" +
                " javax.naming.directory,\n" +
                " javax.naming.event,\n" +
                " javax.naming.ldap,\n" +
                " javax.naming.spi,\n" +
                " javax.net,\n" +
                " javax.net.ssl,\n" +
                " javax.print,\n" +
                " javax.print.attribute,\n" +
                " javax.print.attribute.standard,\n" +
                " javax.print.event,\n" +
                " javax.rmi,\n" +
                " javax.rmi.CORBA,\n" +
                " javax.rmi.ssl,\n" +
                " javax.security.auth,\n" +
                " javax.security.auth.callback,\n" +
                " javax.security.auth.kerberos,\n" +
                " javax.security.auth.login,\n" +
                " javax.security.auth.spi,\n" +
                " javax.security.auth.x500,\n" +
                " javax.security.cert,\n" +
                " javax.security.sasl,\n" +
                " javax.sound.midi,\n" +
                " javax.sound.midi.spi,\n" +
                " javax.sound.sampled,\n" +
                " javax.sound.sampled.spi,\n" +
                " javax.sql,\n" +
                " javax.sql.rowset,\n" +
                " javax.sql.rowset.serial,\n" +
                " javax.sql.rowset.spi,\n" +
                " javax.swing,\n" +
                " javax.swing.border,\n" +
                " javax.swing.colorchooser,\n" +
                " javax.swing.event,\n" +
                " javax.swing.filechooser,\n" +
                " javax.swing.plaf,\n" +
                " javax.swing.plaf.basic,\n" +
                " javax.swing.plaf.metal,\n" +
                " javax.swing.plaf.multi,\n" +
                " javax.swing.plaf.synth,\n" +
                " javax.swing.table,\n" +
                " javax.swing.text,\n" +
                " javax.swing.text.html,\n" +
                " javax.swing.text.html.parser,\n" +
                " javax.swing.text.rtf,\n" +
                " javax.swing.tree,\n" +
                " javax.swing.undo,\n" +
                " javax.transaction,\n" +
                " javax.transaction.xa,\n" +
                " org.ietf.jgss,\n" +
                " org.omg.CORBA,\n" +
                " org.omg.CORBA_2_3,\n" +
                " org.omg.CORBA_2_3.portable,\n" +
                " org.omg.CORBA.DynAnyPackage,\n" +
                " org.omg.CORBA.ORBPackage,\n" +
                " org.omg.CORBA.portable,\n" +
                " org.omg.CORBA.TypeCodePackage,\n" +
                " org.omg.CosNaming,\n" +
                " org.omg.CosNaming.NamingContextExtPackage,\n" +
                " org.omg.CosNaming.NamingContextPackage,\n" +
                " org.omg.Dynamic,\n" +
                " org.omg.DynamicAny,\n" +
                " org.omg.DynamicAny.DynAnyFactoryPackage,\n" +
                " org.omg.DynamicAny.DynAnyPackage,\n" +
                " org.omg.IOP,\n" +
                " org.omg.IOP.CodecFactoryPackage,\n" +
                " org.omg.IOP.CodecPackage,\n" +
                " org.omg.Messaging,\n" +
                " org.omg.PortableInterceptor,\n" +
                " org.omg.PortableInterceptor.ORBInitInfoPackage,\n" +
                " org.omg.PortableServer,\n" +
                " org.omg.PortableServer.CurrentPackage,\n" +
                " org.omg.PortableServer.POAManagerPackage,\n" +
                " org.omg.PortableServer.POAPackage,\n" +
                " org.omg.PortableServer.portable,\n" +
                " org.omg.PortableServer.ServantLocatorPackage,\n" +
                " org.omg.SendingContext,\n" +
                " org.omg.stub.java.rmi,\n" +
                " org.omg.stub.javax.management.remote.rmi,\n" +
                " javax.xml,\n" +
                " javax.xml.datatype,\n" +
                " javax.xml.namespace,\n" +
                " javax.xml.parsers,\n" +
                " javax.xml.transform,\n" +
                " javax.xml.transform.dom,\n" +
                " javax.xml.transform.sax,\n" +
                " javax.xml.transform.stream,\n" +
                " javax.xml.validation,\n" +
                " javax.xml.xpath,\n" +
                " org.apache.xmlcommons,\n" +
                " org.w3c.dom,\n" +
                " org.w3c.dom.bootstrap,\n" +
                " org.w3c.dom.css,\n" +
                " org.w3c.dom.events,\n" +
                " org.w3c.dom.html,\n" +
                " org.w3c.dom.ls,\n" +
                " org.w3c.dom.ranges,\n" +
                " org.w3c.dom.stylesheets,\n" +
                " org.w3c.dom.traversal,\n" +
                " org.w3c.dom.views,\n" +
                " org.w3c.dom.xpath,\n" +
                " org.xml.sax,\n" +
                " org.xml.sax.ext,\n" +
                " org.xml.sax.helpers "
//                " ch.qos.logback.classic.spi, " +
//                " ch.qos.logback.classic.filter, " +
//                " ch.qos.logback.core.filter, " +
//                " ch.qos.logback.core.spi, " +
//                " ch.qos.logback.core, " +
//                " ch.qos.logback.classic "
//                "groovy.lang," +
//                "groovy.swing.factory, " +
//                "groovy.util, " +
//                "groovy.xml, " +
//                "org.codehaus.groovy.control, " +
//                "org.codehaus.groovy.runtime "
        );

        final File frameworkStorage = new File(System.getProperty("user.dir"), "felix-cache");

        FileUtils.deleteDirectory(frameworkStorage);

        configProps.put(Constants.FRAMEWORK_STORAGE, frameworkStorage.getAbsolutePath());

        // Create host activator;

        // Now create an instance of the framework with
        // our configuration properties and activator.
        m_felix = new Felix(configProps);

        // Now start Felix instance.
        m_felix.start();

        final BundleContext bundleContext = m_felix.getBundleContext();

        final List<Bundle> installedBundles = Arrays.asList(bundleContext.getBundles());
        for ( final Bundle bundle : installedBundles ) {
            printState(bundle);
        }

        final List<String> toInstallBundles = new ArrayList<String>();
//        toInstallBundles.add("file:bundle/logback-core-0.9.9.jar$");
//        toInstallBundles.add("file:bundle/logback-classic-0.9.9.jar$");
//        toInstallBundles.add("file:bundle/slf4j-api-1.5.6.jar");
//        toInstallBundles.add("file:bundle/jcl-over-slf4j-1.5.6.jar");
//        toInstallBundles.add("file:bundle/log.jar");
//        toInstallBundles.add("file:lib/LogbackBundle-1.0.jar");
//        toInstallBundles.add("file:/home/teyckmans/lib/org/gradle/git/gradle/bundle/com.springsource.org.apache.xmlcommons-1.3.3.jar");
//        toInstallBundles.add("file:/home/teyckmans/lib/org/gradle/git/gradle/bundle/dom4j-1.6.1.jar$");
//        toInstallBundles.add("file:/home/teyckmans/lib/org/gradle/git/gradle/lib/groovy-all-1.5.6.jar");
        toInstallBundles.add("file:/home/teyckmans/lib/org/gradle/git/gradle/command-line-api/build/command-line-api-0.6.jar");
//        toInstallBundles.add("file:/home/teyckmans/lib/org/gradle/git/gradle/gradle-api/build/gradle-api-0.6.jar");
        toInstallBundles.add("file:/home/teyckmans/lib/org/gradle/git/gradle/gradle-impl/build/gradle-impl-0.6.jar");
//        toInstallBundles.add("file:/home/teyckmans/lib/org/gradle/git/gradle/command-line-impl/build/command-line-impl-0.6.jar");

        for ( String toInstallBundle : toInstallBundles ) {
            final Bundle installedBundle = bundleContext.installBundle(toInstallBundle);

            installedBundle.start();

            printState(installedBundle);
        }

//        bundleContext.installBundle("file:lib/jopt-simple-2.4.1.jar$");

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
        final BundleContext bundleContext = m_felix.getBundleContext();
        final ServiceReference serviceRef = bundleContext.getServiceReference(GradleCommandLine.class.getName());

        final Object serviceObject = bundleContext.getService(serviceRef);

        return serviceObject;
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
