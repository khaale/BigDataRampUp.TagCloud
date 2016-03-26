package com.khaale.bigdatarampup.tagcloud.yarn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.records.LocalResource;
import org.apache.hadoop.yarn.api.records.LocalResourceType;
import org.apache.hadoop.yarn.api.records.LocalResourceVisibility;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.util.Apps;
import org.apache.hadoop.yarn.util.ConverterUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Provides common functionality.
 */
public class YarnSetupUtils {

    private Configuration conf;

    public YarnSetupUtils(Configuration conf) {

        this.conf = conf;
    }

    /**
     * Prepares jar resource.
     * @param jarPath path ot jar
     * @param resource resource record to set up
     * @throws IOException
     */
    public void setupJarResource(Path jarPath, LocalResource resource) throws IOException {
        FileStatus jarStat = FileSystem.get(conf).getFileStatus(jarPath);
        resource.setResource(ConverterUtils.getYarnUrlFromPath(jarPath));
        resource.setSize(jarStat.getLen());
        resource.setTimestamp(jarStat.getModificationTime());
        resource.setType(LocalResourceType.FILE);
        resource.setVisibility(LocalResourceVisibility.PUBLIC);
    }

    /**
     * Prepares jar environment
     * @param jarEnvironment env to set up.
     */
    public void setupJarEnv(Map<String, String> jarEnvironment) {
        for (String c : conf.getStrings(
                YarnConfiguration.YARN_APPLICATION_CLASSPATH,
                YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH)) {
            Apps.addToEnvironment(jarEnvironment, ApplicationConstants.Environment.CLASSPATH.name(),
                    c.trim(), File.pathSeparator);
        }
        Apps.addToEnvironment(jarEnvironment,
                ApplicationConstants.Environment.CLASSPATH.name(),
                ApplicationConstants.Environment.PWD.$() + File.separator + "*",
                File.pathSeparator);
    }

    /**
     * Generates shell command to execute given class with provided arguments on YARN.
     * @param klass Class to execute
     * @param args arguments to pass to class' main method.
     * @return shell command.
     */
    public String getClassExecuteCommand(Class klass, String... args) {
        return "$JAVA_HOME/bin/java" +
                " -Xmx128M" +
                " " + klass.getName() +
                " " + String.join(" ", args) +
                " 1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout" +
                " 2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr";
    }
}
