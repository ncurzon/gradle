package org.gradle.bootstrap;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.ServiceReference;
import org.gradle.GradleFactory;
import org.gradle.DefaultGradleFactory;
import org.gradle.initialization.DefaultLoggingConfigurer;

/**
 * @author Tom Eyckmans
 */
public class HostActivator implements BundleActivator {

    private BundleContext bundleContext;
    private ServiceRegistration gradleFactoryRegistration;

    @Override
    public void start(BundleContext bundleContext) throws Exception
    {
        this.bundleContext = bundleContext;
        this.gradleFactoryRegistration = bundleContext.registerService(GradleFactory.class.getName(), new DefaultGradleFactory(new DefaultLoggingConfigurer()), null);
    }

    public ServiceReference getGradleFactoryReference()
    {
        return gradleFactoryRegistration.getReference();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception
    {
        this.gradleFactoryRegistration.unregister();
        this.bundleContext = null;
    }
}
