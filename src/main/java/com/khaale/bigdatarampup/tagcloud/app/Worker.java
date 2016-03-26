package com.khaale.bigdatarampup.tagcloud.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Performs keywords extraction and saving.
 */
public class Worker {

    private final static Logger logger = LoggerFactory.getLogger(Worker.class);

    private final static int KEYWORD_INDEX = 1;
    private final static int URL_INDEX = 5;
    private final static String ERROR_MARK = "!ERROR";

    private final FileSystemFacade fsFacade;
    private final ContentExtractor contentExtractor;
    private final TagExtractor tagExtractor;

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

    /**
     * Gets keywords by URL's in input file, saves them to output file.
     * Supports partial input file processing.
     * @param inputFilePath hdfs path to input file
     * @param outputFilePath hdfs path to output file
     * @param skipLines how much lines should be skipped in input file. 1 record (header) if not specified.
     * @param limitLines how much lines should be taken from input file. All records - 1(header) if not specified.
     */
    public void fillKeywordValues(String inputFilePath, String outputFilePath, Optional<Integer> skipLines, Optional<Integer> limitLines) {

        logger.info("Starting..");

        final String inputText = fsFacade.readFile(
                inputFilePath,
                skipLines.orElse(1),
                limitLines.orElse(fsFacade.getLinesCount(inputFilePath) - 1));

        if (inputText == null || inputText.isEmpty()) {
            logger.info("File is empty - exiting.");
            return;
        }

        final String[] allLines = inputText.split("\\r?\\n");
        logger.info("Read {} lines", allLines.length);

        final Collection<String> filledRows =
        Stream.of(allLines)
                .filter(line -> !line.isEmpty())
                .map(this::fillKeywords)
                .collect(Collectors.toList());

        final String result =
                String.join("\n", String.join(System.lineSeparator(), filledRows)) +
                System.lineSeparator();
        fsFacade.writeFile(outputFilePath, result);

        logger.info("Finished.");
    }

    private String fillKeywords(String row) {

        final String[] fields = row.split("\\t");
        final String url = fields[URL_INDEX];

        logger.info("Processing {}", url);

        final Optional<String> text = contentExtractor.getContent(url);

        if (text.isPresent()) {
            final Collection<String> tags = tagExtractor.getTags(text.get());
            fields[KEYWORD_INDEX] = String.join(",", tags);
        }
        else {
            fields[KEYWORD_INDEX] = ERROR_MARK;
        }
        logger.info("Extracted tags: {}", fields[KEYWORD_INDEX]);

        return String.join("\t", (CharSequence[]) fields);
    }


}

