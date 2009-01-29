package org.gradle.bootstrap.util;

import java.util.Properties;
import java.util.Map;

/**
 * @author Tom Eyckmans
 */
public class SystemUtil {
    public static String getSystemValue(
            Properties systemProperties, Map<String, String> systemEnv,
            String systemPropertyKey, String systemEnvKey,
            String defaultValue,
            SystemUtilGetSystemValueTracer tracer)
    {
        String value = systemProperties.getProperty(systemPropertyKey);
        if (value == null) {
            value = systemEnv.get(systemEnvKey);
            if (value == null) {
                return defaultValue;
            }
            if (tracer!=null) {
                tracer.valueFromSystemEnv(systemEnvKey, value);
            }
        } else {
            if (tracer!=null) {
                tracer.valueFromSystemProperties(systemPropertyKey, value);
            }
        }
        return value;
    }
}
