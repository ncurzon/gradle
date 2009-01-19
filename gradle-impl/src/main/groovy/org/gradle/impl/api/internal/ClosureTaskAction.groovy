package org.gradle.impl.api.internal

import org.gradle.api.Task
import org.gradle.api.TaskAction

class ClosureTaskAction implements TaskAction {
    private final Closure closure;

    def ClosureTaskAction(Closure closure) {
        this.closure = closure;
    }

    public void execute(Task task) {
        if (closure.maximumNumberOfParameters == 0) {
            closure.call()
        }
        else {
            closure.call(task);
        }
    }

}