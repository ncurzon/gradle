package org.gradle.commandline.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
//import org.osgi.framework.ServiceRegistration;
//import org.osgi.util.tracker.ServiceTracker;
//import org.gradle.commandline.impl.DefaultGradleCommandLine;
//import org.gradle.GradleFactory;

/**
 * @author Tom Eyckmans
 */
public class Activator implements BundleActivator {

    private BundleContext bundleContext;
//    private ServiceRegistration commandLineServiceRegistration;
//    private ServiceTracker gradleFactoryTracker;

    @Override
    public void start(BundleContext bundleContext) throws Exception
    {
        this.bundleContext = bundleContext;

//        gradleFactoryTracker = new ServiceTracker(bundleContext, "(&(objectClass="+ GradleFactory.class.getName()+"))", null);
//        gradleFactoryTracker.open();

        // TODO improve this, how should we proceed if the gradleFactory is not available ?
//        final GradleFactory gradleFactory = (GradleFactory) gradleFactoryTracker.getService();

//        final GradleCommandLine gradleCommandLine = new DefaultGradleCommandLine(gradleFactory);
//
//        commandLineServiceRegistration = bundleContext.registerService(GradleCommandLine.class.getName(), gradleCommandLine, null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception
    {
//        gradleFactoryTracker.close();
//        commandLineServiceRegistration.unregister();
        this.bundleContext = null;
    }
}
