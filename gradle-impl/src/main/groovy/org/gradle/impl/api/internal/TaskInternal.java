package org.gradle.impl.api.internal;

import org.gradle.api.Task;

public interface TaskInternal extends Task {
    void execute();
}
