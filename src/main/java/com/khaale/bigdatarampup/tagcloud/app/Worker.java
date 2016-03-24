package com.khaale.bigdatarampup.tagcloud.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Aleksander on 24.03.2016.
 */
public class Worker {

    private final static Logger logger = LoggerFactory.getLogger(Worker.class);

    private final static int KEYWORD_INDEX = 1;
    private final static int URL_INDEX = 5;
    private final static String ERROR_MARK = "!ERROR";

    private FileSystemFacade fsFacade;
    private WebScraper webScraper;
    private TagExtractor tagExtractor;

    public Worker(
            FileSystemFacade fsFacade,
            WebScraper webScraper,
            TagExtractor tagExtractor) {


        this.fsFacade = fsFacade;
        this.webScraper = webScraper;
        this.tagExtractor = tagExtractor;
    }

    public void fillKeywordValues(String inputFilePath) {

        logger.info("Starting..");

        String inputText = fsFacade.readFile(inputFilePath);

        if (inputText == null || inputText.isEmpty()) {
            logger.info("File is empty - exiting.");
            return;
        }

        String[] allLines = inputText.split("\\r?\\n");
        logger.info("Read {} lines", allLines.length);

        Collection<String> filledRows = Stream.of(allLines)
                .skip(1)
                .filter(line -> !line.isEmpty())
                .map(this::fillKeywords)
                .collect(Collectors.toList());

        String outputText = String.join("\n", allLines[0], String.join("\n", filledRows));

        fsFacade.writeFile(inputFilePath + ".filled", outputText);

        logger.info("Finished.");
    }

    private String fillKeywords(String row) {

        String[] fields = row.split("\\t");
        String url = fields[URL_INDEX];

        logger.info("Processing {}", url);

        Optional<String> text = webScraper.getText(url);

        if (text.isPresent()) {
            Collection<String> tags = tagExtractor.getTags(text.get());
            fields[KEYWORD_INDEX] = String.join(",", tags);
        }
        else {
            fields[KEYWORD_INDEX] = ERROR_MARK;
        }
        logger.info("Extracted tags: {}", fields[KEYWORD_INDEX]);

        return String.join("\t", (CharSequence[]) fields);
    }


}

