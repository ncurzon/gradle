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
package org.gradle.api.internal.dependencies.maven.deploy;

import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.gradle.api.dependencies.maven.PublishFilter;
import org.gradle.api.dependencies.maven.MavenPom;

import java.io.File;
import java.util.List;

/**
 * @author Hans Dockter
 */
public interface ArtifactPom {
    String getName();

    Artifact getArtifact();

    File getArtifactFile();

    PublishFilter getFilter();

    void setFilter(PublishFilter filter);

    MavenPom getPom();

    void setPom(MavenPom pom);

    void addArtifact(Artifact artifact, File src);

    void toPomFile(File pomFile, List<DependencyDescriptor> dependencies);
}
