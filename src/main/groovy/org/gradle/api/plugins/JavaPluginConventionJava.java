package org.gradle.api.plugins;

import org.gradle.api.tasks.bundling.GradleManifest;
import org.gradle.api.Project;

import java.util.*;
import java.io.File;

/**
 * @author Tom Eyckmans
 */
public class JavaPluginConventionJava extends AbstractPluginConvention {

    public enum ValueNames implements ConventionValueName {
        srcRootName,
        srcDocsDirName,
        classesDirName,
        testClassesDirName,
        distsDirName,
        docsDirName,
        javadocDirName,
        testResultsDirName,
        webAppDirName,
        testReportDirName,
        srcDirNames,
        resourceDirNames,
        testSrcDirNames,
        testResourceDirNames,
        floatingSrcDirs,
        floatingTestSrcDirs,
        floatingResourceDirs,
        floatingTestResourceDirs,
        uploadDistsPomDirName,
        uploadLibsPomDirName,
        sourceCompatibility,
        targetCompatibility,
        archiveTypes,
        manifest,
        metaInf,

        // derived
        srcRoot,
        srcDirs,
        resourceDirs,
        testSrcDirs,
        testResourceDirs,
        srcDocsDir,
        webAppDir,
        classesDir,
        testClassesDir,
        uploadLibsPomDir,
        uploadDistsPomDir,
        distsDir,
        docsDir,
        javadocDir,
        testResultsDir,
        testReportDir
    }

    public JavaPluginConventionJava(final Project project) {
        super(project);

        final ConventionValue<GradleManifest> manifest = addValue(new DefaultConventionValue<GradleManifest>(ValueNames.manifest, new GradleManifest()));
        final ConventionValue<List> metaInf = addValue(new DefaultConventionValue<List>(ValueNames.metaInf, new ArrayList()));
        final ConventionValue<String> srcRootName = addStringValue(ValueNames.srcRootName, "src");
        final ConventionValue<String> srcDocsDirName = addStringValue(ValueNames.srcDocsDirName, "docs");
        final ConventionValue<String> webAppDirName = addStringValue(ValueNames.webAppDirName, "main/webapp");
        final ConventionValue<String> classesDirName = addStringValue(ValueNames.classesDirName, "classes");
        final ConventionValue<String> testClassesDirName = addStringValue(ValueNames.testClassesDirName, "test-classes");
        final ConventionValue<String> distsDirName = addStringValue(ValueNames.distsDirName, "distributions");
        final ConventionValue<String> docsDirName = addStringValue(ValueNames.docsDirName, "docs");
        final ConventionValue<String> javadocDirName = addStringValue(ValueNames.javadocDirName, "javadoc");
        final ConventionValue<String> uploadLibsPomDirName = addStringValue(ValueNames.uploadLibsPomDirName, "libs-poms");
        final ConventionValue<String> uploadDistsPomDirName = addStringValue(ValueNames.uploadDistsPomDirName, "dists-poms");
        final ConventionValue<String> testResultsDirName = addStringValue(ValueNames.testResultsDirName, "test-results");
        final ConventionValue<String> testReportDirName = addStringValue(ValueNames.testReportDirName, "tests");
        final ConventionValue<List<String>> srcDirNames = addStringListValue(ValueNames.srcDirNames, "main/java");
        final ConventionValue<List<String>> resourceDirNames = addStringListValue(ValueNames.resourceDirNames, "main/resources");
        final ConventionValue<List<String>> testSrcDirNames = addStringListValue(ValueNames.testSrcDirNames, "test/java");
        final ConventionValue<List<String>> testResourceDirNames = addStringListValue(ValueNames.testResourceDirNames, "test/resources");
        final ConventionValue<List<File>> floatingSrcDirs = addValue(new DefaultConventionValue<List<File>>(ValueNames.floatingSrcDirs, new ArrayList<File>()));
        final ConventionValue<List<File>> floatingResourceDirs = addValue(new DefaultConventionValue<List<File>>(ValueNames.floatingResourceDirs, new ArrayList<File>()));
        final ConventionValue<List<File>> floatingTestSrcDirs = addValue(new DefaultConventionValue<List<File>>(ValueNames.floatingTestSrcDirs, new ArrayList<File>()));
        final ConventionValue<List<File>> floatingTestResourceDirs = addValue(new DefaultConventionValue<List<File>>(ValueNames.floatingTestResourceDirs, new ArrayList<File>()));

        final ConventionValue<File> srcRoot = addValue(new DerivedConventionValue<File>(ValueNames.srcRoot) {
            public File getValue() {
                return project.file(srcRootName.getValue());
            }
        });
        final ConventionValue<List<File>> srcDirs = addDerivedFileListValue(ValueNames.srcDirs, srcRoot, srcDirNames, floatingSrcDirs);
        final ConventionValue<List<File>> resourceDirs = addDerivedFileListValue(ValueNames.resourceDirs, srcRoot, resourceDirNames, floatingResourceDirs);
        final ConventionValue<List<File>> testSrcDirs = addDerivedFileListValue(ValueNames.testSrcDirs, srcRoot, testSrcDirNames, floatingTestSrcDirs);
        final ConventionValue<List<File>> testResourceDirs = addDerivedFileListValue(ValueNames.testResourceDirs, srcRoot, testResourceDirNames, floatingTestResourceDirs);
        final ConventionValue<File> srcDocsDir = addDerivedFileValue(ValueNames.srcDocsDir, srcRoot, srcDocsDirName);
        final ConventionValue<File> webAppDir = addDerivedFileValue(ValueNames.webAppDir, srcRoot, webAppDirName);
        final ConventionValue<File> classesDir = addDerivedFileValue(ValueNames.classesDir, srcRoot, classesDirName);
        final ConventionValue<File> testClassesDir = addDerivedFileValue(ValueNames.testClassesDir, srcRoot, testClassesDirName);
        final ConventionValue<File> uploadLibsPomDir = addDerivedFileValue(ValueNames.uploadLibsPomDir, project.getBuildDir(), uploadLibsPomDirName);
        final ConventionValue<File> uploadDistsPomDir = addDerivedFileValue(ValueNames.uploadDistsPomDir, project.getBuildDir(), uploadDistsPomDirName);
        final ConventionValue<File> distsDir = addDerivedFileValue(ValueNames.distsDir, project.getBuildDir(), distsDirName);
        final ConventionValue<File> docsDir = addDerivedFileValue(ValueNames.docsDir, project.getBuildDir(), docsDirName);
        final ConventionValue<File> javadocDir = addDerivedFileValue(ValueNames.javadocDir, docsDir, javadocDirName);
        final ConventionValue<File> testResultsDir = addDerivedFileValue(ValueNames.testResultsDir, project.getBuildDir(), testResultsDirName);
        final ConventionValue<File> reportsDir = convention.getValue(ReportingBasePluginConvention.ValueNames.reportsDir);
        final ConventionValue<File> testReportDir = addDerivedFileValue(ValueNames.testReportDir, reportsDir, testReportDirName);
    }
}
