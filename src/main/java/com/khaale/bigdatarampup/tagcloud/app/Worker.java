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
    private ContentExtractor contentExtractor;
    private TagExtractor tagExtractor;

    public Worker(
            FileSystemFacade fsFacade,
            ContentExtractor contentExtractor,
            TagExtractor tagExtractor) {


        this.fsFacade = fsFacade;
        this.contentExtractor = contentExtractor;
        this.tagExtractor = tagExtractor;
    }

    public void fillKeywordValues(String inputFilePath) {

        fillKeywordValues(
                inputFilePath,
                inputFilePath + ".out",
                Optional.empty(),
                Optional.empty());
    }

    public void fillKeywordValues(String inputFilePath, String outputFilePath, Optional<Integer> skipRecords, Optional<Integer> limitRecords) {

        logger.info("Starting..");

        String inputText = fsFacade.readFile(
                inputFilePath,
                skipRecords.orElse(1),
                limitRecords.orElse(fsFacade.getLinesCount(inputFilePath) - 1));

        if (inputText == null || inputText.isEmpty()) {
            logger.info("File is empty - exiting.");
            return;
        }

        String[] allLines = inputText.split("\\r?\\n");
        logger.info("Read {} lines", allLines.length);

        Collection<String> filledRows =
        Stream.of(allLines)
                .filter(line -> !line.isEmpty())
                .map(this::fillKeywords)
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append(String.join("\n", String.join(System.lineSeparator(), filledRows)));
        sb.append(System.lineSeparator());
        fsFacade.writeFile(outputFilePath, sb.toString());

        logger.info("Finished.");
    }

    private String fillKeywords(String row) {

        String[] fields = row.split("\\t");
        String url = fields[URL_INDEX];

        logger.info("Processing {}", url);

        Optional<String> text = contentExtractor.getContent(url);

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

