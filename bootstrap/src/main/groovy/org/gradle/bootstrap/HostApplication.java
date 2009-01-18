package org.gradle.bootstrap;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.StringMap;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.framework.cache.BundleCache;
import org.osgi.framework.Constants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Bundle;
import org.osgi.util.tracker.ServiceTracker;
import org.gradle.GradleFactory;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Eyckmans
 */
public class HostApplication {

    private Felix m_felix = null;
    private ServiceTracker gradleFactoryTracker;

    public HostApplication()
    {
    }

    public void startApplication() throws Exception
    {
        // Create a case-insensitive configuration property map.
        final Map configMap = new StringMap(false);

        // Configure the Felix instance to be embedded.
        configMap.put("felix.embedded.execution", "true");

        // Add core OSGi packages to be exported from the class path
        // via the system bundle.
        configMap.put(Constants.FRAMEWORK_SYSTEMPACKAGES,
            "org.osgi.framework," +
            "org.osgi.service.packageadmin," +
            "org.osgi.service.startlevel," +
            "org.osgi.service.url");
//        configMap.put("felix.auto.start.1", "file:gradle.jar");
        configMap.put(BundleCache.CACHE_ROOTDIR_PROP, "felix_cache");

//        final List<BundleActivator> systemBundleActivators = new ArrayList<BundleActivator>();

//        systemBundleActivators.add(m_activator);

//        configMap.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, systemBundleActivators);

        // Create host activator;

        // Now create an instance of the framework with
        // our configuration properties and activator.
        m_felix = new Felix(configMap);

        // Now start Felix instance.
        m_felix.start();

        final BundleContext bundleContext = m_felix.getBundleContext();

        bundleContext.installBundle("file:gradle.jar");

        gradleFactoryTracker = new ServiceTracker(bundleContext, bundleContext.createFilter("(objectClass="+GradleFactory.class.getName()+")"), null);
        gradleFactoryTracker.open();
    }

    public GradleFactory getGradleFactory()
    {
        while ( gradleFactoryTracker.getService() != null ) {
            
        }
        return (GradleFactory)gradleFactoryTracker.getService();
    }

    public void stopApplication() throws Exception
    {
        // Shut down the felix framework when stopping the
        // host application.
        m_felix.stop();
        m_felix.waitForStop(0);
    }
}
