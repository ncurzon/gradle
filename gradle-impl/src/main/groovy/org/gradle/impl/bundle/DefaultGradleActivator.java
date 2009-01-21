package org.gradle.impl.bundle;

import org.gradle.GradleFactory;
import org.gradle.impl.initialization.DefaultLoggingConfigurer;
import org.gradle.impl.DefaultGradleFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Tom Eyckmans
 */
public class DefaultGradleActivator implements BundleActivator {

    private BundleContext bundleContext;
    private ServiceRegistration gradleFactoryRegistration;

    public void start(BundleContext bundleContext) throws Exception
    {
        this.bundleContext = bundleContext;

//        org.slf4j.impl.OSGILogFactory.initOSGI(bundleContext);

        this.gradleFactoryRegistration = bundleContext.registerService(GradleFactory.class.getName(), new DefaultGradleFactory(new DefaultLoggingConfigurer()), null);

        System.out.println("GRADLE FACTORY SERVICE REGISTERED");
    }

    public ServiceReference getGradleFactoryReference()
    {
        return gradleFactoryRegistration.getReference();
    }

    public void stop(BundleContext bundleContext) throws Exception
    {
        this.gradleFactoryRegistration.unregister();
        this.bundleContext = null;
    }
}
