package com.github.tachesimazzoca.jruby.benchmark;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import org.jruby.embed.ScriptingContainer;

import com.github.tachesimazzoca.java.benchmark.task.Task;
import com.github.tachesimazzoca.java.benchmark.task.TaskSuite;

@Task(description = "Render 1000 pages")
public class TemplateEngineTaskSuite implements TaskSuite {
    public Runnable[] getTasks() {
        List<Map<String, String>> params = new ArrayList<Map<String, String>>();
        for (int n = 1; n <= 20; n++) {
            Map<String, String> map = new LinkedHashMap<String, String>() {
                {
                    put("title", "foo");
                    put("description", "bar");
                }
            };
            params.add(map);
        }
        int times = 1000;
        Runnable[] tasks = {
            new VelocityTask(times, params),
            new ERBTask(times, params),
            new HamlTask(times, params)
        };
        return tasks;
    }

    @Task(description = "Velocity")
    public static class VelocityTask implements Runnable {
        private int times;
        private Object params;

        private VelocityTask() {
        }

        public VelocityTask(int times, Object params) {
            this.times = times;
            this.params = params;
        }

        public void run() {
            try {
                Properties prop = new Properties();
                prop.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
                prop.setProperty(
                    "class.resource.loader.class",
                    "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
                VelocityEngine velocity = new VelocityEngine(prop);
                Template tem = velocity.getTemplate("views/app.html.vm");

                for (int i = 0; i < this.times; i++) {
                    VelocityContext context = new VelocityContext();
                    context.put("params", params);
                    StringWriter sw = new StringWriter();
                    tem.merge(context, sw);
                    String contents = sw.toString();
                    //System.out.println(contents);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Task(description = "ERB")
    public static class ERBTask implements Runnable {
        private int times;
        private Object params;

        private ERBTask() {
        }

        public ERBTask(int times, Object params) {
            this.times = times;
            this.params = params;
            // Create a dummy container before running the task
            // for a good performance. It makes about 2 seconds faster.
            ScriptingContainer container = new ScriptingContainer();
            container.runScriptlet("require 'java'");
        }

        public void run() {
            InputStream in = null;
            try {
              in = getClass().getClassLoader().getResourceAsStream(
                  "views/app.html.erb");
              String template = IOUtils.toString(in);
              for (int i = 0; i < this.times; i++) {
                  ScriptingContainer container = new ScriptingContainer();
                  container.put("template", template);
                  container.put("params", params);
                  String script = "require 'erb'; ERB.new(template, nil, '-').result(binding)";
                  String contents = (String) container.runScriptlet(script);
                  //System.out.println(contents);
              }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
    }

    @Task(description = "Haml")
    public static class HamlTask implements Runnable {
        private int times;
        private Object params;

        private HamlTask() {
        }

        public HamlTask(int times, Object params) {
            this.times = times;
            this.params = params;
            // Create a dummy container before running the task
            // for a good performance. It makes about 2 seconds faster.
            ScriptingContainer container = new ScriptingContainer();
            container.runScriptlet("require 'java'");
        }

        public void run() {
            InputStream in = null;
            try {
              in = getClass().getClassLoader().getResourceAsStream(
                  "views/app.html.haml");
              String template = IOUtils.toString(in);
              for (int i = 0; i < this.times; i++) {
                  ScriptingContainer container = new ScriptingContainer();
                  container.put("template", template);
                  container.put("params", params);
                  String script =
                      "require 'rubygems';"
                    + "require 'haml';"
                    + "Haml::Engine.new(template).render(Object.new, :params => params)";
                  String contents = (String) container.runScriptlet(script);
                  //System.out.println(contents);
              }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
    }
}
