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
package org.gradle.impl;

import org.gradle.api.internal.project.*;
import org.gradle.impl.api.internal.dependencies.DependencyManagerFactory;
import org.gradle.impl.api.internal.dependencies.DefaultDependencyManagerFactory;
import org.gradle.impl.api.internal.project.DefaultAntBuilderFactory;
import org.gradle.impl.api.internal.project.ProjectFactory;
import org.gradle.impl.api.internal.project.TaskFactory;
import org.gradle.api.logging.LogLevel;
import org.gradle.groovy.scripts.*;
import org.gradle.initialization.*;
import org.gradle.util.WrapUtil;
import org.gradle.logging.AntLoggingAdapter;
import org.gradle.impl.configuration.DefaultBuildConfigurer;
import org.gradle.impl.configuration.ProjectDependencies2TaskResolver;
import org.gradle.GradleFactory;
import org.gradle.StartParameter;
import org.gradle.impl.groovy.scripts.*;
import org.gradle.impl.initialization.*;

/**
 * @author Hans Dockter
*/
public class DefaultGradleFactory implements GradleFactory {
    private LoggingConfigurer loggingConfigurer;

    public DefaultGradleFactory(LoggingConfigurer loggingConfigurer) {
        this.loggingConfigurer = loggingConfigurer;
    }

    public LoggingConfigurer getLoggingConfigurer() {
        return loggingConfigurer;
    }

    public void setLoggingConfigurer(LoggingConfigurer loggingConfigurer) {
        this.loggingConfigurer = loggingConfigurer;
    }

    @Override
    public StartParameter createStartParameter() {
        return new DefaultStartParameter();
    }

    @Override
    public DefaultGradle newInstance(StartParameter startParameter) {
        loggingConfigurer.configure(startParameter.getLogLevel());
        ImportsReader importsReader = new ImportsReader(startParameter.getDefaultImportsFile());
        IScriptProcessor scriptProcessor = new DefaultScriptProcessor(
                new DefaultScriptCompilationHandler(new DefaultCachePropertiesHandler()),
                startParameter.getCacheUsage());
        ISettingsFinder settingsFinder = startParameter.getSettingsScriptSource() == null
                ? new DefaultSettingsFinder(WrapUtil.<ISettingsFileSearchStrategy>toList(
                new MasterDirSettingsFinderStrategy(),
                new ParentDirSettingsFinderStrategy()))
                : new EmbeddedScriptSettingsFinder();
        DependencyManagerFactory dependencyManagerFactory = new DefaultDependencyManagerFactory(settingsFinder, startParameter.getCacheUsage());
        DefaultGradle gradle = new DefaultGradle(
                startParameter,
                settingsFinder,
                new DefaultGradlePropertiesLoader(),
                new ScriptLocatingSettingsProcessor(
                        new ScriptEvaluatingSettingsProcessor(
                                new DefaultSettingsScriptMetaData(),
                                scriptProcessor,
                                importsReader,
                                new SettingsFactory(
                                        new DefaultProjectDescriptorRegistry(),
                                        dependencyManagerFactory,
                                        new BuildSourceBuilder(new DefaultGradleFactory(
                                                new LoggingConfigurer() {
                                                    public void configure(LogLevel logLevel) {
                                                        // do nothing
                                                    }
                                                }
                                        ), new DefaultCacheInvalidationStrategy())))
                ),
                new DefaultBuildLoader(
                        new ProjectFactory(
                                new TaskFactory(),
                                dependencyManagerFactory,
                                new BuildScriptProcessor(
                                        scriptProcessor,
                                        new DefaultProjectScriptMetaData(),
                                        importsReader
                                ),
                                new PluginRegistry(
                                        startParameter.getPluginPropertiesFile()),
                                startParameter,
                                startParameter.getBuildScriptSource(),
                                new DefaultAntBuilderFactory(new AntLoggingAdapter()))
                ),
                new DefaultBuildConfigurer(new ProjectDependencies2TaskResolver()));

        return gradle;
    }
}
