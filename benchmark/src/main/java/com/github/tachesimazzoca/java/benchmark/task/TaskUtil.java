package com.github.tachesimazzoca.java.benchmark.task;

public final class TaskUtil {
    private TaskUtil() {
    }

    public static String getTaskDescription(Class<?> clazz) {
        Task an = (Task) clazz.getAnnotation(Task.class);
        String desc = (an != null) ? an.description() : clazz.getSimpleName();
        return desc;
    }
}
