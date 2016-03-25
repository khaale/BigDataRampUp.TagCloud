package com.khaale.bigdatarampup.tagcloud.yarn;

/**
 * Created by Aleksander on 23.03.2016.
 */

import java.io.IOException;
import java.util.*;

import com.khaale.bigdatarampup.tagcloud.app.FileSystemFacade;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.protocolrecords.AllocateResponse;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.AMRMClient;
import org.apache.hadoop.yarn.client.api.AMRMClient.ContainerRequest;
import org.apache.hadoop.yarn.client.api.NMClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.util.Records;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationMaster {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationMaster.class);

    FileSystemFacade fs = new FileSystemFacade();
    Configuration conf = new YarnConfiguration();

    public static void main(String[] args) throws Exception {

        ApplicationMaster appMaster = new ApplicationMaster();
        appMaster.run(args);
    }

    public void run(String[] args) throws Exception {

        final String inputFilePath = args[0];
        final int n = Integer.valueOf(args[1]);
        final String jarPath = args[2];

        // Initialize clients to ResourceManager and NodeManagers

        AMRMClient<ContainerRequest> rmClient = AMRMClient.createAMRMClient();
        rmClient.init(conf);
        rmClient.start();

        NMClient nmClient = NMClient.createNMClient();
        nmClient.init(conf);
        nmClient.start();

        // Register with ResourceManager
        logger.info("registerApplicationMaster 0");
        rmClient.registerApplicationMaster("", 0, "");
        logger.info("registerApplicationMaster 1");

        // Priority for worker containers - priorities are intra-application
        Priority priority = Records.newRecord(Priority.class);
        priority.setPriority(0);

        // Resource requirements for worker containers
        Resource capability = Records.newRecord(Resource.class);
        capability.setMemory(128);
        capability.setVirtualCores(1);

        // Make container requests to ResourceManager
        for (int i = 0; i < n; ++i) {
            ContainerRequest containerAsk = new ContainerRequest(capability, null, null, priority);
            logger.info("Making res-req {}", i);
            rmClient.addContainerRequest(containerAsk);
        }

        // Obtain allocated containers, launch and check for responses

        int responseId = 0;
        int completedContainers = 0;
        List<ContainerParameters> launchedContainerParams = new ArrayList<>();
        Queue<ContainerParameters> containerParams = new LinkedList<>(prepareArguments(inputFilePath, n));

        while (completedContainers < n) {

            AllocateResponse response = rmClient.allocate(responseId++);

            for (Container container : response.getAllocatedContainers()) {

                ContainerParameters params = containerParams.poll();

                //preparing file
                prepareOutputFile(params.getOutputFileName());

                // Launch container by create ContainerLaunchContext
                ContainerLaunchContext ctx = getContainerLaunchContext(conf, jarPath, params);
                logger.info("Launching container " + container.getId());
                nmClient.startContainer(container, ctx);

                launchedContainerParams.add(params);
            }
            for (ContainerStatus status : response.getCompletedContainersStatuses()) {
                ++completedContainers;
                System.out.println("Completed container " + status.getContainerId());
            }
            Thread.sleep(100);
        }

        collectContainersOutput(inputFilePath, launchedContainerParams);

        // Un-register with ResourceManager
        rmClient.unregisterApplicationMaster(
                FinalApplicationStatus.SUCCEEDED, "", "");
    }

    private void collectContainersOutput(String inputFilePath, List<ContainerParameters> containerParams) {

        String collectedFilePath = inputFilePath + ".out";
        logger.info("Collecting container's output to {}", collectedFilePath);

        fs.removeFile(collectedFilePath);

        String header = fs.readFile(inputFilePath, 0, 1);
        fs.writeFile(collectedFilePath, header);

        for(ContainerParameters params : containerParams) {

            logger.info("Appending data from {}",params.getOutputFileName());
            String content = fs.readFile(params.getOutputFileName());
            fs.appendToFile(collectedFilePath, content);
        }
    }

    private ContainerLaunchContext getContainerLaunchContext(Configuration conf, String jarPath, ContainerParameters containerParameters) throws IOException {

        ContainerSetupUtils setup = new ContainerSetupUtils(conf);
        ContainerLaunchContext ctx =
                Records.newRecord(ContainerLaunchContext.class);

        // Setup jar for ContainerMaster
        LocalResource appMasterJar = Records.newRecord(LocalResource.class);
        setup.setupJarResource(new Path(jarPath), appMasterJar);
        ctx.setLocalResources(
                Collections.singletonMap("tagcloud.jar", appMasterJar));

        // Setup CLASSPATH for ContainerMaster
        Map<String, String> env = new HashMap<>();
        setup.setupJarEnv(env);
        ctx.setEnvironment(env);

        ctx.setCommands(
                Collections.singletonList(
                        setup.getClassExecuteCommand(
                                ContainerMaster.class,
                                containerParameters.toArgs())
                )
        );
        return ctx;
    }

    private List<ContainerParameters> prepareArguments(String inputFilePath, int containersCount) {

        int linesCount = fs.getLinesCount(inputFilePath);

        return ContainerParameters.get(inputFilePath, linesCount, containersCount);
    }

    private void prepareOutputFile(String outputFilePath) {

        fs.removeFile(outputFilePath);
    }

}