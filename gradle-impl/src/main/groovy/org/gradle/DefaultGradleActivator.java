package org.gradle;

import org.gradle.commandline.GradleCommandLine;
import org.gradle.commandline.impl.DefaultGradleCommandLine;
import org.gradle.initialization.DefaultLoggingConfigurer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Tom Eyckmans
 */
public class DefaultGradleActivator implements BundleActivator {

    private BundleContext bundleContext;
    private ServiceRegistration commandLineServiceRegistration;
    private ServiceRegistration gradleFactoryRegistration;

    public void start(BundleContext bundleContext) throws Exception
    {
        this.bundleContext = bundleContext;

//        org.slf4j.impl.OSGILogFactory.initOSGI(bundleContext);

        final GradleFactory gradleFactory = new DefaultGradleFactory(new DefaultLoggingConfigurer());

        this.gradleFactoryRegistration = bundleContext.registerService(GradleFactory.class.getName(), gradleFactory, null);

        final GradleCommandLine gradleCommandLine = new DefaultGradleCommandLine(gradleFactory);

        this.commandLineServiceRegistration = bundleContext.registerService(GradleCommandLine.class.getName(), gradleCommandLine, null);

        System.out.println("GRADLE FACTORY SERVICE REGISTERED");
    }

    public ServiceReference getGradleFactoryReference()
    {
        return gradleFactoryRegistration.getReference();
    }

    public void stop(BundleContext bundleContext) throws Exception
    {
        this.gradleFactoryRegistration.unregister();
        this.commandLineServiceRegistration.unregister();

        this.bundleContext = null;
    }
}
