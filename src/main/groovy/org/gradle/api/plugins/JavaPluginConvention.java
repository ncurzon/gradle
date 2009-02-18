package org.gradle.api.plugins;

import org.gradle.api.tasks.bundling.GradleManifest;
import org.gradle.api.tasks.bundling.ArchiveType;
import org.gradle.api.Project;
import org.gradle.api.InvalidUserDataException;

import java.util.*;
import java.io.File;
import java.math.BigDecimal;

/**
 * @author Tom Eyckmans
 */
public class JavaPluginConvention extends AbstractPluginConvention {

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

    public static final Map<String, ArchiveType> DEFAULT_ARCHIVE_TYPES = new HashMap<String, ArchiveType>();
    static {
        DEFAULT_ARCHIVE_TYPES.put("jar", new ArchiveType("jar", DefaultConventionsToPropertiesMapping.JAR, org.gradle.api.tasks.bundling.Jar.class));
        DEFAULT_ARCHIVE_TYPES.put("zip", new ArchiveType("zip", DefaultConventionsToPropertiesMapping.ZIP, org.gradle.api.tasks.bundling.Zip.class));
        DEFAULT_ARCHIVE_TYPES.put("war", new ArchiveType("war", DefaultConventionsToPropertiesMapping.WAR, org.gradle.api.tasks.bundling.War.class));
        DEFAULT_ARCHIVE_TYPES.put("tar", new ArchiveType("tar", DefaultConventionsToPropertiesMapping.TAR, org.gradle.api.tasks.bundling.Tar.class));
        DEFAULT_ARCHIVE_TYPES.put("tar.gz", new ArchiveType("tar.gz", DefaultConventionsToPropertiesMapping.TAR, org.gradle.api.tasks.bundling.Tar.class));
        DEFAULT_ARCHIVE_TYPES.put("tar.bzip2", new ArchiveType("tar.bzip2", DefaultConventionsToPropertiesMapping.TAR, org.gradle.api.tasks.bundling.Tar.class));
    }

    private final ConventionValue<GradleManifest> manifest;
    private final ConventionValue<List> metaInf;
    private final ConventionValue<String> srcRootName;
    private final ConventionValue<String> srcDocsDirName;
    private final ConventionValue<String> webAppDirName;
    private final ConventionValue<String> classesDirName;
    private final ConventionValue<String> testClassesDirName;
    private final ConventionValue<String> distsDirName;
    private final ConventionValue<String> docsDirName;
    private final ConventionValue<String> javadocDirName;
    private final ConventionValue<String> uploadLibsPomDirName;
    private final ConventionValue<String> uploadDistsPomDirName;
    private final ConventionValue<String> testResultsDirName;
    private final ConventionValue<String> testReportDirName;
    private final ConventionValue<List<String>> srcDirNames;
    private final ConventionValue<List<String>> resourceDirNames;
    private final ConventionValue<List<String>> testSrcDirNames;
    private final ConventionValue<List<String>> testResourceDirNames;
    private final ConventionValue<List<File>> floatingSrcDirs;
    private final ConventionValue<List<File>> floatingResourceDirs;
    private final ConventionValue<List<File>> floatingTestSrcDirs;
    private final ConventionValue<List<File>> floatingTestResourceDirs;
    private final ConventionValue<BigDecimal> sourceCompatibility;
    private final ConventionValue<BigDecimal> targetCompatibility;

    private final ConventionValue<File> srcRoot;
    private final ConventionValue<List<File>> srcDirs;
    private final ConventionValue<List<File>> resourceDirs;
    private final ConventionValue<List<File>> testSrcDirs;
    private final ConventionValue<List<File>> testResourceDirs;
    private final ConventionValue<File> srcDocsDir;
    private final ConventionValue<File> webAppDir;
    private final ConventionValue<File> classesDir;
    private final ConventionValue<File> testClassesDir;
    private final ConventionValue<File> uploadLibsPomDir;
    private final ConventionValue<File> uploadDistsPomDir;
    private final ConventionValue<File> distsDir;
    private final ConventionValue<File> docsDir;
    private final ConventionValue<File> javadocDir;
    private final ConventionValue<File> testResultsDir;
    private final ConventionValue<File> testReportDir;
    private final ConventionValue<Map<String, ArchiveType>> archiveTypes;

    private final ConventionValue<File> buildDir;

    public JavaPluginConvention(final Project project, Map<String, ?> customValues) {
        super(project, customValues);

        manifest = addValue(new DefaultConventionValue<GradleManifest>(ValueNames.manifest, new GradleManifest()));
        metaInf = addValue(new DefaultConventionValue<List>(ValueNames.metaInf, new ArrayList()));
        srcRootName = addStringValue(ValueNames.srcRootName, "src");
        srcDocsDirName = addStringValue(ValueNames.srcDocsDirName, "docs");
        webAppDirName = addStringValue(ValueNames.webAppDirName, "main/webapp");
        classesDirName = addStringValue(ValueNames.classesDirName, "classes");
        testClassesDirName = addStringValue(ValueNames.testClassesDirName, "test-classes");
        distsDirName = addStringValue(ValueNames.distsDirName, "distributions");
        docsDirName = addStringValue(ValueNames.docsDirName, "docs");
        javadocDirName = addStringValue(ValueNames.javadocDirName, "javadoc");
        uploadLibsPomDirName = addStringValue(ValueNames.uploadLibsPomDirName, "libs-poms");
        uploadDistsPomDirName = addStringValue(ValueNames.uploadDistsPomDirName, "dists-poms");
        testResultsDirName = addStringValue(ValueNames.testResultsDirName, "test-results");
        testReportDirName = addStringValue(ValueNames.testReportDirName, "tests");
        srcDirNames = addStringListValue(ValueNames.srcDirNames, "main/java");
        resourceDirNames = addStringListValue(ValueNames.resourceDirNames, "main/resources");
        testSrcDirNames = addStringListValue(ValueNames.testSrcDirNames, "test/java");
        testResourceDirNames = addStringListValue(ValueNames.testResourceDirNames, "test/resources");
        floatingSrcDirs = addValue(new DefaultConventionValue<List<File>>(ValueNames.floatingSrcDirs, new ArrayList<File>()));
        floatingResourceDirs = addValue(new DefaultConventionValue<List<File>>(ValueNames.floatingResourceDirs, new ArrayList<File>()));
        floatingTestSrcDirs = addValue(new DefaultConventionValue<List<File>>(ValueNames.floatingTestSrcDirs, new ArrayList<File>()));
        floatingTestResourceDirs = addValue(new DefaultConventionValue<List<File>>(ValueNames.floatingTestResourceDirs, new ArrayList<File>()));
        sourceCompatibility = addValue(new DefaultConventionValue<BigDecimal>(ValueNames.sourceCompatibility, null));
        targetCompatibility = addValue(new DefaultConventionValue<BigDecimal>(ValueNames.targetCompatibility, null));

        srcRoot = addValue(new DerivedConventionValue<File>(ValueNames.srcRoot) {
            public File getValue() {
                return project.file(srcRootName.getValue());
            }
        });
        
        srcDirs = addDerivedFileListValue(ValueNames.srcDirs, srcRoot, srcDirNames, floatingSrcDirs);
        resourceDirs = addDerivedFileListValue(ValueNames.resourceDirs, srcRoot, resourceDirNames, floatingResourceDirs);
        testSrcDirs = addDerivedFileListValue(ValueNames.testSrcDirs, srcRoot, testSrcDirNames, floatingTestSrcDirs);
        testResourceDirs = addDerivedFileListValue(ValueNames.testResourceDirs, srcRoot, testResourceDirNames, floatingTestResourceDirs);
        srcDocsDir = addDerivedFileValue(ValueNames.srcDocsDir, srcRoot, srcDocsDirName);
        webAppDir = addDerivedFileValue(ValueNames.webAppDir, srcRoot, webAppDirName);
        classesDir = addDerivedFileValue(ValueNames.classesDir, srcRoot, classesDirName);
        testClassesDir = addDerivedFileValue(ValueNames.testClassesDir, srcRoot, testClassesDirName);
        buildDir = getValue(ProjectPluginConvention.ValueNames.buildDir);
        uploadLibsPomDir = addDerivedFileValue(ValueNames.uploadLibsPomDir, buildDir, uploadLibsPomDirName);
        uploadDistsPomDir = addDerivedFileValue(ValueNames.uploadDistsPomDir, buildDir, uploadDistsPomDirName);
        distsDir = addDerivedFileValue(ValueNames.distsDir, buildDir, distsDirName);
        docsDir = addDerivedFileValue(ValueNames.docsDir, buildDir, docsDirName);
        javadocDir = addDerivedFileValue(ValueNames.javadocDir, docsDir, javadocDirName);
        testResultsDir = addDerivedFileValue(ValueNames.testResultsDir, buildDir, testResultsDirName);
        final ConventionValue<File> reportsDir = getValue(ReportingBasePluginConvention.ValueNames.reportsDir);
        testReportDir = addDerivedFileValue(ValueNames.testReportDir, reportsDir, testReportDirName);
        archiveTypes = addValue(new DefaultConventionValue<Map<String, ArchiveType>>(ValueNames.archiveTypes, DEFAULT_ARCHIVE_TYPES));
    }

    File mkdir(File parent, String name) {
        if (name == null || "".equals(name)) throw new InvalidUserDataException("You must specify the name of the directory");

        File baseDir = (parent != null ) ? parent : buildDir.getValue();
        File result = new File(baseDir, name);
        result.mkdirs();
        
        return result;
    }

    public GradleManifest getManifest() {
        return manifest.getValue();
    }

    public List getMetaInf() {
        return metaInf.getValue();
    }

    public BigDecimal getSourceCompatibility() {
        return sourceCompatibility.getValue();
    }

    public void setSourceCompatibility(BigDecimal sourceCompatibility) {
        this.sourceCompatibility.setValue(sourceCompatibility);
    }

    public BigDecimal getTargetCompatibility() {
        return targetCompatibility.getValue();
    }

    public void setTargetCompatibility(BigDecimal targetCompatibility) {
        this.sourceCompatibility.setValue(targetCompatibility);
    }

    public File getSrcRoot() {
        return srcRoot.getValue();
    }

    public List<File> getSrcDirs() {
        return srcDirs.getValue();
    }

    public List<File> getResourceDirs() {
        return resourceDirs.getValue();
    }

    public List<File> getTestSrcDirs() {
        return testSrcDirs.getValue();
    }

    public List<File> getTestResourceDirs() {
        return testResourceDirs.getValue();
    }

    public File getSrcDocsDir() {
        return srcDocsDir.getValue();
    }

    public File getWebAppDir() {
        return webAppDir.getValue();
    }

    public File getClassesDir() {
        return classesDir.getValue();
    }

    public File getTestClassesDir() {
        return testClassesDir.getValue();
    }

    public File getUploadLibsPomDir() {
        return uploadLibsPomDir.getValue();
    }

    public File getUploadDistsPomDir() {
        return uploadDistsPomDir.getValue();
    }

    public File getDistsDir() {
        return distsDir.getValue();
    }

    public File getDocsDir() {
        return docsDir.getValue();
    }

    public File getJavadocDir() {
        return javadocDir.getValue();
    }

    public File getTestResultsDir() {
        return testResultsDir.getValue();
    }

    public File getTestReportDir() {
        return testReportDir.getValue();
    }

    public Map<String, ArchiveType> getArchiveTypes() {
        return archiveTypes.getValue();
    }
}
