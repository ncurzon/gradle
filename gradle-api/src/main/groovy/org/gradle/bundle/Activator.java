package org.gradle.bundle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Tom Eyckmans
 */
public class Activator implements BundleActivator {

    private BundleContext bundleContext;

    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;

//        org.slf4j.impl.OSGILogFactory.initOSGI(bundleContext);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        this.bundleContext = null;
    }
}
