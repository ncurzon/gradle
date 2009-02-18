package org.gradle.api.plugins;

import org.gradle.api.Project;

import java.io.File;
import java.util.*;

import groovy.lang.GroovyRuntimeException;

/**
 * @author Tom Eyckmans
 */
public abstract class AbstractPluginConvention {
    protected final Project project;
    protected final DefaultConvention convention;
    protected final Map<String, ?> customValues;

    protected AbstractPluginConvention(Project project, Map<String, ?> customValues) {
        this.project = project;
        this.convention = project.getConvention();
        this.customValues = customValues;
    }

    <T> ConventionValue<T> addValue(ConventionValue<T> conventionValue) {
        if ( customValues.containsKey(conventionValue.getName().toString()) ) {
            conventionValue.setValue((T) customValues.get(conventionValue.getName().toString()));
        }
        return convention.defineConventionValue(conventionValue);
    }

    ConventionValue<List<File>> addDerivedFileListValue(ConventionValueName conventionValueName, final ConventionValue<File> srcRoot, final ConventionValue<List<String>> dirNames, final ConventionValue<List<File>> additionDirs) {
        return addValue(new DerivedConventionValue<List<File>>(conventionValueName) {
            @Override
            public List<File> getValue() {
                final List<File> srcDirs = new ArrayList<File>();

                for(String srcDirName : dirNames.getValue()) {
                    srcDirs.add(new File(srcRoot.getValue(), srcDirName));
                }
                srcDirs.addAll(additionDirs.getValue());

                return Collections.unmodifiableList(srcDirs);
            }
        });
    }

    ConventionValue<File> addDerivedFileValue(ConventionValueName conventionValueName, final ConventionValue<File> srcRoot, final ConventionValue<String> subDirName) {
        return addValue(new FileStringDerivedFileConventionValue(conventionValueName, srcRoot, subDirName));
    }

    private class FileStringDerivedFileConventionValue extends DerivedConventionValue<File> {
        private final ConventionValue<File> parent;
        private final ConventionValue<String> subDirName;

        private FileStringDerivedFileConventionValue(ConventionValueName name, ConventionValue<File> parent, ConventionValue<String> subDirName) {
            super(name);
            this.parent = parent;
            this.subDirName = subDirName;
        }

        public File getValue() {
            try {
                System.out.println("[parent.getValue()]" + parent.getValue());
                return new File(parent.getValue(), subDirName.getValue());
            }
            catch ( GroovyRuntimeException t ) {
                t.printStackTrace();
                return null;
            }
        }
    }

    ConventionValue<File> addDerivedFileValue(ConventionValueName conventionValueName, final File srcRoot, final ConventionValue<String> subDirName) {
        return addValue(new DerivedConventionValue<File>(conventionValueName) {
            @Override
            public File getValue() {
                return new File(srcRoot, subDirName.getValue());
            }
        });
    }

    DefaultConventionValue<String> addStringValue(ConventionValueName conventionValueName, String defaultValue) {
        final DefaultConventionValue<String> stringValue = new DefaultConventionValue<String>(conventionValueName, defaultValue);
        addValue(stringValue);
        return stringValue;
    }

    DefaultConventionValue<List<String>> addStringListValue(ConventionValueName conventionValueName, String toAddValue) {
        final DefaultConventionValue<List<String>> stringListValue = new DefaultConventionValue<List<String>>(conventionValueName, new ArrayList<String>());
        stringListValue.getValue().add(toAddValue);
        addValue(stringListValue);
        return stringListValue;
    }

    <T> ConventionValue<T> getValue(ConventionValueName conventionValueName) {
        return convention.getConventionValue(conventionValueName);
    }
}
