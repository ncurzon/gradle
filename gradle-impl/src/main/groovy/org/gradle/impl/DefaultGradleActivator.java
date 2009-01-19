package org.gradle.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.ServiceReference;
import org.gradle.GradleFactory;
import org.gradle.impl.DefaultGradleFactory;
import org.gradle.impl.initialization.DefaultLoggingConfigurer;

/**
 * @author Tom Eyckmans
 */
public class DefaultGradleActivator implements BundleActivator {

    private BundleContext bundleContext;
    private ServiceRegistration gradleFactoryRegistration;

    public void start(BundleContext bundleContext) throws Exception
    {
        this.bundleContext = bundleContext;
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
