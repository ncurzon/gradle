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
package org.gradle.impl.api.internal.dependencies.maven.deploy.groovy;

import org.gradle.api.dependencies.maven.MavenPom;
import org.gradle.util.ConfigureUtil
import org.gradle.api.dependencies.maven.PublishFilter
import org.gradle.impl.api.internal.dependencies.maven.deploy.BasePomFilterContainer
import org.gradle.impl.api.internal.dependencies.maven.MavenPomFactory
import org.gradle.api.dependencies.maven.GroovyPomFilterContainer
import org.gradle.api.dependencies.maven.CopyablePomFilterContainer
import org.gradle.api.dependencies.maven.CopyableGroovyPomFilterContainer
import org.gradle.api.dependencies.maven.PomFilterContainer
import org.gradle.impl.api.internal.dependencies.maven.deploy.BasePomFilterContainer
import org.gradle.impl.api.internal.dependencies.maven.deploy.BasePomFilterContainer
import org.gradle.impl.api.internal.dependencies.maven.deploy.BasePomFilterContainer

/**
 * @author Hans Dockter
 */
public class DefaultGroovyPomFilterContainer extends BasePomFilterContainer implements CopyableGroovyPomFilterContainer {
    DefaultGroovyPomFilterContainer(MavenPomFactory mavenPomFactory) {
        super(mavenPomFactory);
    }

    void filter(Closure filter) {
        this.filter = filter as PublishFilter
    }

    MavenPom addFilter(String name, Closure filter) {
        addFilter(name, filter as PublishFilter)
    }

    MavenPom pom(Closure configureClosure) {
        ConfigureUtil.configure(configureClosure, pom)
    }

    MavenPom pom(String name, Closure configureClosure) {
        ConfigureUtil.configure(configureClosure, pom(name))
    }

    protected BasePomFilterContainer newInstance() {
        return new DefaultGroovyPomFilterContainer(mavenPomFactory);
    }
}