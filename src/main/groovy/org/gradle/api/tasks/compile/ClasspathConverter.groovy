/*
 * Copyright 2007 the original author or authors.
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

package org.gradle.api.tasks.compile

import org.gradle.api.InvalidUserDataException
import org.gradle.api.PathValidation
import org.gradle.api.tasks.util.BaseDirConverter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Hans Dockter
 */
class ClasspathConverter {
    private static Logger logger = LoggerFactory.getLogger(ClasspathConverter)

    BaseDirConverter baseDirConverter = new BaseDirConverter()
    
    List createFileClasspath(File baseDir, List args) {
        List flattenedArgs = args ? args.flatten() : []
        logger.debug("Create additional classpath from {}", flattenedArgs);
        List classpath = []
        flattenedArgs.each {classpathElement ->
            if (!classpathElement) {
                throw new IllegalArgumentException('Classpath elements must not be null or empty')
            }
            def elementType = classpathElement.class
            switch (elementType) {
                case String: classpath << baseDirConverter.baseDir(classpathElement, baseDir, PathValidation.EXISTS); break
                case File: classpath << classpathElement; break
//                case Collection: classpathElement.flatten().each() {classpath += createFileClasspath(baseDir, it)[0]}; break
                default: throw new InvalidUserDataException ("Illegal classpathelement " + classpathElement.getClass())
            }
        }
        logger.debug("Created path as: {}", classpath)
        classpath
    }
}
