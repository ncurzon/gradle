/*
 * Copyright 2009 the original author or authors.
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
package org.gradle.api.plugins;

import org.gradle.api.Project;

import java.io.File;
import java.util.Map;

/**
 * <p>A {@code BasePluginConvention} defines the convention properties and methods used by the {@link ReportingBasePlugin}</p>
 */
public class ReportingBasePluginConvention extends AbstractPluginConvention {
    public static class ValueNames {
        public static final ConventionValueName<String> reportsDirName = new ConventionValueName<String>();

        // derived
        public static final ConventionValueName<File> reportsDir = new ConventionValueName<File>();
    }

    private final ConventionValue<String> reportsDirName;
    private final ConventionValue<File> reportsDir;

    public ReportingBasePluginConvention(Project project, Map<String, ?> customValues) {
        super(project, customValues);

        reportsDirName = addStringValue(ValueNames.reportsDirName, "reports");
        reportsDir = addDerivedFileValue(ValueNames.reportsDir, project.getBuildDir(), reportsDirName);
    }

    public String getReportsDirName() {
        return reportsDirName.getValue();
    }

    public void setReportsDirName(String reportsDirName) {
        this.reportsDirName.setValue(reportsDirName);
    }

    public File getReportsDir() {
        return reportsDir.getValue();
    }
}
