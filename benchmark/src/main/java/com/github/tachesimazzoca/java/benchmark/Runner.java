package com.github.tachesimazzoca.java.benchmark;

import java.io.PrintStream;

import com.github.tachesimazzoca.java.benchmark.task.TaskSuite;
import com.github.tachesimazzoca.java.benchmark.task.TaskUtil;

public class Runner {
    private PrintStream out;

    public Runner() {
        this.out = System.out;
    }

    public Runner out(PrintStream out) {
        this.out = out;
        return this;
    }

    public void run(TaskSuite suite) {
        Runnable[] tasks = suite.getTasks();
        this.out.println(
                "suite:" + TaskUtil.getTaskDescription(suite.getClass()));
        for (int i = 0; i < tasks.length; i++) {
            this.run(tasks[i]);
        }
    }

    public void run(Runnable task) {
        long nsec = this.measure(task);
        this.out.println(
                "sec:" + (nsec / 1000000000.0) + "\t"
                + "task:" + TaskUtil.getTaskDescription(task.getClass()));
    }

    public long measure(Runnable task) {
        long start = System.nanoTime();
        task.run();
        return System.nanoTime() - start;
    }
}
