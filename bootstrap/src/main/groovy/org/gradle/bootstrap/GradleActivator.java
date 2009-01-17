package org.gradle.bootstrap;

import org.osgi.framework.ServiceReference;

/**
 * @author Tom Eyckmans
 */
public interface GradleActivator {
    ServiceReference getGradleFactoryReference();
}
