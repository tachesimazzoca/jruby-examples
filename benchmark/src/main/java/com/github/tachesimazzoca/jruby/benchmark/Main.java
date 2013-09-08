package com.github.tachesimazzoca.jruby.benchmark;

import com.github.tachesimazzoca.java.benchmark.Runner;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Runner runner = new Runner();
        runner.run(new TemplateEngineTaskSuite());
    }
}
