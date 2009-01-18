package org.gradle.impl.execution;

import org.gradle.execution.BuildExecuter;

public class MergingBuildExecuter extends DelegatingBuildExecuter {
    public MergingBuildExecuter(BuildExecuter delegate) {
        super(delegate);
    }

    @Override
    public boolean requiresProjectReload() {
        return false;
    }
}
