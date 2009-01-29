package org.gradle.bootstrap.util;

/**
 * @author Tom Eyckmans
 */
public interface SystemUtilGetSystemValueTracer {
    void valueFromSystemProperties(String systemPropertyKey, String systemPropertiesValue);
    
    void valueFromSystemEnv(String systemEnvKey, String systemEnvValue);
}
