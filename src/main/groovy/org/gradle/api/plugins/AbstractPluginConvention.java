package org.gradle.api.plugins;

import org.gradle.api.Project;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Tom Eyckmans
 */
public abstract class AbstractPluginConvention {
    protected final Project project;
    protected final DefaultConvention convention;

    protected AbstractPluginConvention(Project project) {
        this.project = project;
        this.convention = project.getConvention();
    }

    <T> ConventionValue<T> addValue(ConventionValue<T> conventionValue) {
        return convention.addValue(conventionValue);
    }

    ConventionValue<List<File>> addDerivedFileListValue(ConventionValueName conventionValueName, final ConventionValue<File> srcRoot, final ConventionValue<List<String>> dirNames, final ConventionValue<List<File>> additionDirs) {
        return addValue(new DerivedConventionValue<List<File>>(conventionValueName) {
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
        return addValue(new DerivedConventionValue<File>(conventionValueName) {
            public File getValue() {
                return new File(srcRoot.getValue(), subDirName.getValue());
            }
        });
    }

    ConventionValue<File> addDerivedFileValue(ConventionValueName conventionValueName, final File srcRoot, final ConventionValue<String> subDirName) {
        return addValue(new DerivedConventionValue<File>(conventionValueName) {
            public File getValue() {
                return new File(srcRoot, subDirName.getValue());
            }
        });
    }

    DefaultConventionValue<String> addStringValue(ConventionValueName conventionValueName, String value) {
        final DefaultConventionValue<String> stringValue = new DefaultConventionValue<String>(conventionValueName, value);
        addValue(stringValue);
        return stringValue;
    }

    DefaultConventionValue<List<String>> addStringListValue(ConventionValueName conventionValueName, String toAddValue) {
        final DefaultConventionValue<List<String>> stringListValue = new DefaultConventionValue<List<String>>(conventionValueName, new ArrayList<String>());
        stringListValue.getValue().add(toAddValue);
        addValue(stringListValue);
        return stringListValue;
    }
}
