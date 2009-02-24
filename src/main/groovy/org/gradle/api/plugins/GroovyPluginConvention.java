package org.gradle.api.plugins;

import org.gradle.api.Project;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.File;

/**
 * @author Tom Eyckmans
 */
public class GroovyPluginConvention extends AbstractPluginConvention {

    public static class ValueNames {
        public static final ConventionValueName<List<String>> groovySrcDirNames = new ConventionValueName<List<String>>();
        public static final ConventionValueName<List<String>> groovyTestSrcDirNames = new ConventionValueName<List<String>>();
        public static final ConventionValueName<List<File>> floatingGroovySrcDirs = new ConventionValueName<List<File>>();
        public static final ConventionValueName<List<File>> floatingGroovyTestSrcDirs = new ConventionValueName<List<File>>();
        public static final ConventionValueName<String> groovydocDirName = new ConventionValueName<String>();

        // derived
        public static final ConventionValueName<List<File>> groovySrcDirs = new ConventionValueName<List<File>>();
        public static final ConventionValueName<List<File>> groovyTestSrcDirs = new ConventionValueName<List<File>>();
        public static final ConventionValueName<File> groovydocDir = new ConventionValueName<File>();
    }

    private final ConventionValue<List<String>> groovySrcDirNames;
    private final ConventionValue<List<String>> groovyTestSrcDirNames;
    private final ConventionValue<String> groovydocDirName;
    private final ConventionValue<List<File>> floatingGroovySrcDirs;
    private final ConventionValue<List<File>> floatingGroovyTestSrcDirs;
    private final ConventionValue<List<File>> groovySrcDirs;
    private final ConventionValue<List<File>> groovyTestSrcDirs;
    private final ConventionValue<File> groovydocDir;

    protected GroovyPluginConvention(Project project, Map<String, ?> customValues) {
        super(project, customValues);

        groovySrcDirNames = addStringListValue(ValueNames.groovySrcDirNames, "main/groovy");
        groovyTestSrcDirNames = addStringListValue(ValueNames.groovyTestSrcDirNames, "test/groovy");
        groovydocDirName = addStringValue(ValueNames.groovydocDirName, "groovydoc");
        floatingGroovySrcDirs = addValue(new DefaultConventionValue<List<File>>(ValueNames.floatingGroovySrcDirs, new ArrayList<File>()));
        floatingGroovyTestSrcDirs = addValue(new DefaultConventionValue<List<File>>(ValueNames.floatingGroovyTestSrcDirs, new ArrayList<File>()));
        final ConventionValue<File> srcRoot = getValue(JavaPluginConvention.ValueNames.srcRoot);
        groovySrcDirs = addDerivedFileListValue(ValueNames.groovySrcDirs, srcRoot, groovySrcDirNames, floatingGroovySrcDirs);
        groovyTestSrcDirs = addDerivedFileListValue(ValueNames.groovySrcDirs, srcRoot, groovyTestSrcDirNames, floatingGroovyTestSrcDirs);
        final ConventionValue<File> docsDir = getValue(JavaPluginConvention.ValueNames.docsDir);
        groovydocDir = addDerivedFileValue(ValueNames.groovydocDir, docsDir, groovydocDirName);
    }

    public List<File> getGroovyTestSrcDirs() {
        return groovyTestSrcDirs.getValue();
    }

    public List<File> getGroovySrcDirs() {
        return groovySrcDirs.getValue();
    }

    public File getGroovydocDir() {
        return groovydocDir.getValue();
    }
}
