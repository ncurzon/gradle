package org.gradle;

import org.gradle.bootstrap.HostApplication;

/**
 * @author Tom Eyckmans
 */
public class OsgiBootstrapMain {
    public static void main(String[] args) throws Exception {
        final HostApplication application = new HostApplication();

        application.startApplication();

        Thread.sleep(1500);

        application.stopApplication();
    }
}
