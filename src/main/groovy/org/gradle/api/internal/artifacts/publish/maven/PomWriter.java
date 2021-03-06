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
package org.gradle.api.internal.artifacts.publish.maven;

import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.gradle.api.artifacts.maven.MavenPom;

import java.io.PrintWriter;
import java.util.List;

/**
 * @author Hans Dockter
 */
public interface PomWriter {
    String NL = System.getProperty("line.separator");
    String GROUP_ID = "groupId";
    String ARTIFACT_ID = "artifactId";
    String PACKAGING = "packaging";
    String VERSION = "version";
    String SCOPE = "scope";
    String TYPE = "type";
    String OPTIONAL = "optional";
    String CLASSIFIER = "classifier";
    String DEPENDENCY = "dependency";
    String EXCLUSIONS = "exclusions";
    String EXCLUSION = "exclusion";
    String ROOT_ELEMENT_NAME = "project";
    
    String DEFAULT_PACKAGING = "jar";
    int DEFAULT_INDENT = 2;

    void convert(MavenPom pom, List<DependencyDescriptor> dependencies, PrintWriter printWriter);
}
