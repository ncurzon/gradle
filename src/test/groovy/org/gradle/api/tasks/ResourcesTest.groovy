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

package org.gradle.api.tasks

import groovy.mock.interceptor.MockFor
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.util.CopyInstruction
import org.gradle.api.tasks.util.CopyInstructionFactory
import org.gradle.api.tasks.util.ExistingDirsFilter
import org.gradle.util.JUnit4GroovyMockery
import org.jmock.lib.legacy.ClassImposteriser
import static org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Hans Dockter
 */
@RunWith (org.jmock.integration.junit4.JMock)
class ResourcesTest extends AbstractTaskTest {
    Resources resources

    CopyInstructionFactory copyInstructionFactoryMock

    MockFor copyInstructionFactoryMocker

    ResourcesTestConvention pluginConvention

    JUnit4GroovyMockery context = new JUnit4GroovyMockery();

    @Before
    public void setUp() {
        super.setUp()
        context.setImposteriser(ClassImposteriser.INSTANCE)
        resources = new Resources(project, AbstractTaskTest.TEST_TASK_NAME)
        pluginConvention = new ResourcesTestConvention()
        pluginConvention.classesDir = new File('/classes')
        project.convention.plugins.test = pluginConvention
        resources.conventionMapping = [destinationDir: { convention, task -> convention.classesDir } as ConventionValue]
        copyInstructionFactoryMock = context.mock(CopyInstructionFactory.class)
        resources.copyInstructionFactory = copyInstructionFactoryMock
    }

    AbstractTask getTask() {resources}

    @Test public void testExecute() {
        assertEquals(pluginConvention.classesDir, resources.destinationDir)

        File sourceDir1 = new File('/source1')
        File sourceDir2 = new File('/source2')
        File sourceDir3 = new File('/source3')

        File targetDir = pluginConvention.classesDir

        // We test also that the respective methods returns the resource object
        String globalPattern1 = 'gi1'
        String globalPattern2 = 'gi2'
        String globalPattern3 = 'gi3'
        String sourceDir1Pattern1 = 'sd11'
        String sourceDir1Pattern2 = 'sd12'
        String sourceDir1Pattern3 = 'sd13'
        String sourceDir2Pattern1 = 'sd21'
        String sourceDir2Pattern2 = 'sd22'
        String sourceDir2Pattern3 = 'sd23'
        Map globalFilter1 = [gf1Token: 'gf1']
        Map globalFilter2 = [gf2Token: 'gf2']
        Map sourceDir1Filter1 = [sdf11Token: 'sdf11']
        Map sourceDir1Filter2 = [sdf12Token: 'sdf12']
        Map sourceDir2Filter1 = [sdf21Token: 'sdf21']
        Map sourceDir2Filter2 = [sdf22Token: 'sdf22']
        assert resources.is(resources.from(sourceDir1, sourceDir2))
        assert resources.is(resources.from(sourceDir3))
        assert resources.is(resources.into(targetDir))
        ['includes', 'excludes'].each {
            assert resources.is(resources."$it"(globalPattern1, globalPattern2))
            assert resources.is(resources."$it"(globalPattern3))
            assert resources.is(resources."$it"(sourceDir1, sourceDir1Pattern1, sourceDir1Pattern2))
            assert resources.is(resources."$it"(sourceDir1, sourceDir1Pattern3))
            assert resources.is(resources."$it"(sourceDir2, sourceDir2Pattern1, sourceDir2Pattern2))
            assert resources.is(resources."$it"(sourceDir2, sourceDir2Pattern3))
        }
        assert resources.is(resources.filter(globalFilter1))
        assert resources.is(resources.filter(globalFilter2))
        assert resources.is(resources.filter(sourceDir1, sourceDir1Filter1))
        assert resources.is(resources.filter(sourceDir1, sourceDir1Filter2))
        assert resources.is(resources.filter(sourceDir2, sourceDir2Filter1))
        assert resources.is(resources.filter(sourceDir2, sourceDir2Filter2))

        assertEquals([sourceDir1, sourceDir2, sourceDir3], resources.srcDirs)
        assertEquals(targetDir, resources.destinationDir)

        Map instructionExecuted = [:]

        resources.existentDirsFilter = [checkDestDirAndFindExistingDirsAndThrowStopActionIfNone: {File destDir, Collection srcDirs ->
            assert destDir.is(resources.destinationDir)
            assert srcDirs.is(resources.srcDirs)
            resources.srcDirs
        }] as ExistingDirsFilter

        int executeCounter = 0
        CopyInstruction copyInstructionMock = [execute: {executeCounter++}] as CopyInstruction
       
        context.checking {
            one(copyInstructionFactoryMock).createCopyInstruction(
                    sourceDir1,
                    targetDir,
                    [globalPattern1, globalPattern2, globalPattern3, sourceDir1Pattern1, sourceDir1Pattern2, sourceDir1Pattern3] as Set,
                    [globalPattern1, globalPattern2, globalPattern3, sourceDir1Pattern1, sourceDir1Pattern2, sourceDir1Pattern3] as Set,
                    globalFilter1 + globalFilter2 + sourceDir1Filter1 + sourceDir1Filter2
            )
            will(returnValue(copyInstructionMock))
            one(copyInstructionFactoryMock).createCopyInstruction(
                    sourceDir2,
                    targetDir,
                    [globalPattern1, globalPattern2, globalPattern3, sourceDir2Pattern1, sourceDir2Pattern2, sourceDir2Pattern3] as Set,
                    [globalPattern1, globalPattern2, globalPattern3, sourceDir2Pattern1, sourceDir2Pattern2, sourceDir2Pattern3] as Set,
                    globalFilter1 + globalFilter2 + sourceDir2Filter1 + sourceDir2Filter2
            )
            will(returnValue(copyInstructionMock))
            one(copyInstructionFactoryMock).createCopyInstruction(
                    sourceDir3,
                    targetDir,
                    [globalPattern1, globalPattern2, globalPattern3] as Set,
                    [globalPattern1, globalPattern2, globalPattern3] as Set,
                    globalFilter1 + globalFilter2
            )
            will(returnValue(copyInstructionMock))
//            exactly(3).of(copyInstructionMock).execute()
        }


        resources.existentDirsFilter = [checkDestDirAndFindExistingDirsAndThrowStopActionIfNone: {File destDir, Collection srcDirs ->
            assert destDir.is(resources.destinationDir)
            assert srcDirs.is(resources.srcDirs)
            resources.srcDirs
        }] as ExistingDirsFilter

        resources.execute()

        assert executeCounter == 3
        
    }

    private void checkCopyInstructionCall(List calledValues, File targetDir, List includes, List excludes, Map filter) {
        assertEquals(targetDir, calledValues[0])
        assertEquals(includes as HashSet, calledValues[1] as HashSet)
        assertEquals(excludes as HashSet, calledValues[2] as HashSet)
        assertEquals(filter, calledValues[3])
    }
}

public class ResourcesTestConvention {
    File classesDir
}
