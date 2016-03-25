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
 * Created by Aleksander_Khanteev on 3/25/2016.
 */
public class ContainerSetupUtils {

    private Configuration conf;

    public ContainerSetupUtils(Configuration conf) {

        this.conf = conf;
    }

    public void setupJarResource(Path jarPath, LocalResource appMasterJar) throws IOException {
        FileStatus jarStat = FileSystem.get(conf).getFileStatus(jarPath);
        appMasterJar.setResource(ConverterUtils.getYarnUrlFromPath(jarPath));
        appMasterJar.setSize(jarStat.getLen());
        appMasterJar.setTimestamp(jarStat.getModificationTime());
        appMasterJar.setType(LocalResourceType.FILE);
        appMasterJar.setVisibility(LocalResourceVisibility.PUBLIC);
    }

    public void setupJarEnv(Map<String, String> appMasterEnv) {
        for (String c : conf.getStrings(
                YarnConfiguration.YARN_APPLICATION_CLASSPATH,
                YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH)) {
            Apps.addToEnvironment(appMasterEnv, ApplicationConstants.Environment.CLASSPATH.name(),
                    c.trim(), File.pathSeparator);
        }
        Apps.addToEnvironment(appMasterEnv,
                ApplicationConstants.Environment.CLASSPATH.name(),
                ApplicationConstants.Environment.PWD.$() + File.separator + "*",
                File.pathSeparator);
    }

    public String getClassExecuteCommand(Class klass, String... args) {
        return "$JAVA_HOME/bin/java" +
                " -Xmx128M" +
                " " + klass.getName() +
                " " + String.join(" ", args) +
                " 1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout" +
                " 2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr";
    }
}
