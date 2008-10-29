/*
 * Copyright 2007-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.internal.dependencies.maven;

import static org.gradle.api.internal.dependencies.maven.PomWriter.NL;

import java.io.PrintWriter;

/**
 * @author Hans Dockter
 */
public interface PomHeaderWriter {
    public static final String HEADER_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    public static final String HEADER_XMLNS = "<" + PomWriter.ROOT_ELEMENT_NAME + " xmlns=\"http://maven.apache.org/POM/4.0.0\" " +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + NL +
            "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 " +
            "http://maven.apache.org/maven-v4_0_0.xsd\">";
    public static final String HEADER_MAVEN = "  <modelVersion>4.0.0</modelVersion>";

    public static final String GENERATE_TEXT_PRE = "<!--" + NL +
            "   Apache Maven 2 POM generated by Gradle" + NL + "   ";
    public static final String GENERATE_TEXT_VERSION = "   Gradle version: ";
    public static final String GENERATE_TEXT_POST = "-->" + NL;

    void convert(String licenseHeader, PrintWriter printWriter);
}