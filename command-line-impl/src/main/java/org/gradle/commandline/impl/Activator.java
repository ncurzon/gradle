package org.gradle.commandline.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.ServiceReference;
import org.gradle.GradleFactory;
import org.gradle.commandline.GradleCommandLine;

/**
 * @author Tom Eyckmans
 */
public class Activator implements BundleActivator {

    private BundleContext bundleContext;
    private ServiceRegistration commandLineServiceRegistration;

    public void start(BundleContext bundleContext) throws Exception
    {
        this.bundleContext = bundleContext;
//        org.slf4j.impl.OSGILogFactory.initOSGI(bundleContext);

        // TODO improve this, how should we proceed if the gradleFactory is not available ?
        final ServiceReference serviceRef = bundleContext.getServiceReference(GradleFactory.class.getName());
        final GradleFactory gradleFactory = (GradleFactory) bundleContext.getService(serviceRef);

        final GradleCommandLine gradleCommandLine = new DefaultGradleCommandLine(gradleFactory);

        commandLineServiceRegistration = bundleContext.registerService(GradleCommandLine.class.getName(), gradleCommandLine, null);
    }

    public void stop(BundleContext bundleContext) throws Exception
    {
        commandLineServiceRegistration.unregister();
        this.bundleContext = null;
    }
}
