package com.khaale.bigdatarampup.tagcloud.yarn;

import com.khaale.bigdatarampup.tagcloud.app.FileSystemFacade;
import com.khaale.bigdatarampup.tagcloud.app.TagExtractor;
import com.khaale.bigdatarampup.tagcloud.app.UrlContentExtractor;
import com.khaale.bigdatarampup.tagcloud.app.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by Aleksander_Khanteev on 3/25/2016.
 */
public class ContainerMaster {


    private final static Logger logger = LoggerFactory.getLogger(ContainerMaster.class);

    public static void main(String[] args) {

        logger.info("Starting with args: ", args);

        ContainerParameters params = new ContainerParameters(args);

        Worker worker = new Worker(new FileSystemFacade(), new UrlContentExtractor(), new TagExtractor());
        worker.fillKeywordValues(
                params.getInputFileName(),
                params.getOutputFileName(),
                Optional.of(params.getSkip()),
                Optional.of(params.getLimit())
        );

        logger.info("Finished!", args);
    }
}
