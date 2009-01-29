package org.gradle.bootstrap.util;

import java.util.Properties;
import java.util.Map;
import java.io.File;

/**
 * @author Tom Eyckmans
 */
public class OsgiConfigUtil {
    public static File determineOsgiConfigFile(Properties systemProperties, Map<String, String> systemEnv)
    {
        final String osgiSystemConfig = SystemUtil.getSystemValue(
            systemProperties, systemEnv,
            "gradle.osgi.config", "GRADLE_OSGI_CONFIG",
            null,
            null
        );

        if ( osgiSystemConfig == null )
            return null;
        else {
            systemProperties.setProperty("gradle.osgi.config", osgiSystemConfig);

            return new File(osgiSystemConfig);
        }
    }
}
