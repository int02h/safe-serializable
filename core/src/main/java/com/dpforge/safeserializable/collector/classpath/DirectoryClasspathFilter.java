package com.dpforge.safeserializable.collector.classpath;

import java.io.File;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of classpath filter that filters out items in classpath not belonging to the specific directory
 * or its subdirectories
 */
public class DirectoryClasspathFilter implements ClasspathFilter {

    private final String pathPrefix;

    private DirectoryClasspathFilter(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    @Override
    public boolean shouldCollectClasspath(String classpath) {
        return classpath.startsWith(pathPrefix);
    }

    public static DirectoryClasspathFilter createForCurrentProject() {
        File currentDir = new File(".").getAbsoluteFile();
        File mainModuleDir = requireNonNull(currentDir.getParentFile(), "No main module directory");
        File projectDir = requireNonNull(mainModuleDir.getParentFile(), "No project directory");
        String pathPrefix = projectDir.getAbsolutePath();
        return new DirectoryClasspathFilter(pathPrefix);
    }

}
