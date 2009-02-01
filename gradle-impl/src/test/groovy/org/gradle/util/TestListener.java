package org.gradle.util;

/**
 * @author Tom Eyckmans
 */
public interface TestListener {
     void event1(String param);

        void event2(int value, String other);
}
