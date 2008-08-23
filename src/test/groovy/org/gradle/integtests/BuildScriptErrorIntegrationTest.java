/*
 * Copyright 2008 the original author or authors.
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
package org.gradle.integtests;

import org.junit.Test;
import org.junit.Ignore;

import java.io.File;

public class BuildScriptErrorIntegrationTest extends AbstractIntegrationTest {
    @Test
    public void reportsProjectEvaulationFailsWithGroovyException() {
        GradleExecutionFailure failure = usingBuildScript("createTakk('do-stuff')").runTasksAndExpectFailure();

        failure.assertHasLineNumber(1);
    }

    @Ignore
    public void reportsTaskActionExecutionFailsWithError() {
        // todo We need to figure when the Groovy compile provided line info and when not. I can't easily produce a runtime
        // error with line info information although I know there are runtime errors with line info. 
        GradleExecutionFailure failure = usingBuildScript("createTask('do-stuff')\n{ 1 / 0 }").runTasksAndExpectFailure("do-stuff");
        failure.assertHasLineNumber(2);
    }

    @Test
    public void reportsTaskActionExecutionFailsWithRuntimeException() {
        File gradleFile = getTestBuildFile("task-action-execution-failure.gradle");
        GradleExecutionFailure failure = usingBuildFile(gradleFile).runTasksAndExpectFailure("broken");
        failure.assertHasLineNumber(3);
    }
}