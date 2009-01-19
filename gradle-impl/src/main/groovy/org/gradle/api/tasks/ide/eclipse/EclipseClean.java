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
package org.gradle.api.tasks.ide.eclipse;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.TaskAction;
import org.gradle.impl.api.internal.ConventionTask;
import org.gradle.util.GFileUtils;

import java.io.File;

/**
 * Deletes files generated by various Eclipse tasks. The files which are deleted by this task are:
 *
 * <ul>
 * <li>{@link EclipseProject#PROJECT_FILE_NAME}</li>
 * <li>{@link EclipseClasspath#CLASSPATH_FILE_NAME}</li>
 * <li>{@link EclipseWtp#WTP_FILE_NAME}</li>
 * </ul>
 *
 * If one of those files does not exists nothing happens.
 *
 * @author Hans Dockter
 */
public class EclipseClean extends ConventionTask {
    public EclipseClean(Project project, String name) {
        super(project, name);
        doFirst(new TaskAction() {
            public void execute(Task task) {
                deleteEclipseFiles();
            }
        });
    }

    private void deleteEclipseFiles() {
        GFileUtils.deleteQuietly(getProject().file(EclipseProject.PROJECT_FILE_NAME));
        GFileUtils.deleteQuietly(getProject().file(EclipseClasspath.CLASSPATH_FILE_NAME));
        GFileUtils.deleteQuietly(getProject().file(new File(EclipseWtp.WTP_FILE_DIR, EclipseWtp.WTP_FILE_NAME)));
    }

}
