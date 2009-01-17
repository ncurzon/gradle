package org.gradle.bootstrap;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.StringMap;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.framework.cache.BundleCache;
import org.osgi.framework.Constants;
import org.osgi.framework.BundleActivator;
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
        configMap.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA,
            "org.gradle.bootstrap"
//            "org.osgi.framework; version=1.3.0," +
//            "org.osgi.service.packageadmin; version=1.2.0," +
//            "org.osgi.service.startlevel; version=1.0.0," +
//            "org.osgi.service.url; version=1.0.0");
        );

        configMap.put(BundleCache.CACHE_ROOTDIR_PROP, "felix_cache");

        final List<BundleActivator> systemBundleActivators = new ArrayList<BundleActivator>();

//        systemBundleActivators.add(m_activator);

        configMap.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, systemBundleActivators);

        // Create host activator;

        // Now create an instance of the framework with
        // our configuration properties and activator.
        m_felix = new Felix(configMap);

        // Now start Felix instance.
        m_felix.start();

        gradleFactoryTracker = new ServiceTracker(m_felix.getBundleContext(), "(&(objectclass="+GradleFactory.class.getName()+"))", null);
        gradleFactoryTracker.open();
    }

    public GradleFactory getGradleFactory()
    {
        return (GradleFactory)gradleFactoryTracker.getService();
    }

    public void stopApplication() throws Exception
    {
        // Shut down the felix framework when stopping the
        // host application.
        m_felix.stop();
        m_felix.waitForStop(Long.MAX_VALUE);
    }
}
