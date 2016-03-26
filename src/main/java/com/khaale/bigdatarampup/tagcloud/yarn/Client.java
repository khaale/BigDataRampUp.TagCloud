package com.khaale.bigdatarampup.tagcloud.yarn;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.util.Records;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * YARN Client.
 * Runs YARN Application Master.
 */
public class Client {

    private final static Logger logger = LoggerFactory.getLogger(Client.class);

    public void run(String[] args) throws Exception {

        logger.info("Starting ..");

        final String command = args[0];
        final int n = Integer.valueOf(args[1]);
        final Path jarPath = new Path(args[2]);

        // Create yarnClient
        YarnConfiguration conf = new YarnConfiguration();
        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(conf);
        yarnClient.start();

        // Create application via yarnClient
        YarnClientApplication app = yarnClient.createApplication();

        // Set up the container launch context for the application master
        YarnSetupUtils setup = new YarnSetupUtils(conf);

        ContainerLaunchContext amContainer =
                Records.newRecord(ContainerLaunchContext.class);
        String containerCommand = setup.getClassExecuteCommand(ApplicationMaster.class, command, String.valueOf(n), args[2]);
        logger.info("Container command: {}", containerCommand);

        amContainer.setCommands(
                Collections.singletonList(containerCommand)
        );

        // Setup jar for ApplicationMaster
        LocalResource appMasterJar = Records.newRecord(LocalResource.class);
        setup.setupJarResource(jarPath, appMasterJar);
        amContainer.setLocalResources(
                Collections.singletonMap("tagcloud.jar", appMasterJar));

        // Setup CLASSPATH for ApplicationMaster
        Map<String, String> appMasterEnv = new HashMap<>();
        setup.setupJarEnv(appMasterEnv);
        amContainer.setEnvironment(appMasterEnv);

        // Set up resource type requirements for ApplicationMaster
        Resource capability = Records.newRecord(Resource.class);
        capability.setMemory(128);
        capability.setVirtualCores(1);

        // Finally, set-up ApplicationSubmissionContext for the application
        ApplicationSubmissionContext appContext =
                app.getApplicationSubmissionContext();
        appContext.setApplicationName("tagcloud"); // application name
        appContext.setAMContainerSpec(amContainer);
        appContext.setResource(capability);
        appContext.setQueue("default"); // queue

        // Submit application
        ApplicationId appId = appContext.getApplicationId();
        System.out.println("Submitting application " + appId);
        yarnClient.submitApplication(appContext);

        ApplicationReport appReport = yarnClient.getApplicationReport(appId);
        YarnApplicationState appState = appReport.getYarnApplicationState();
        while (appState != YarnApplicationState.FINISHED &&
                appState != YarnApplicationState.KILLED &&
                appState != YarnApplicationState.FAILED) {
            Thread.sleep(100);
            appReport = yarnClient.getApplicationReport(appId);
            appState = appReport.getYarnApplicationState();
        }

        System.out.println(
                "Application " + appId + " finished with" +
                        " state " + appState +
                        " at " + appReport.getFinishTime());

    }

    public static void main(String[] args) throws Exception {
        Client c = new Client();
        c.run(args);
    }
}