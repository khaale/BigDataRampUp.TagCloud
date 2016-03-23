package com.khaale.bigdatarampup.tagcloud.app;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Aleksander on 24.03.2016.
 */
public class Worker {

    private final static int KEYWORD_INDEX = 1;
    private final static int URL_INDEX = 5;

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

        String inputText = fsFacade.readFile(inputFilePath);

        if (inputText == null || inputText.isEmpty()) {
            return;
        }

        String[] allLines = inputText.split("\\r?\\n");

        Collection<String> filledRows = Stream.of(allLines)
                .skip(1)
                .filter(line -> !line.isEmpty())
                .map(this::fillKeywords)
                .collect(Collectors.toList());

        String outputText = String.join("\n", allLines[0], String.join("\n", filledRows));

        fsFacade.writeFile(inputFilePath + ".filled", outputText);
    }

    private String fillKeywords(String row) {

        String[] fields = row.split("\\t");
        String url = fields[URL_INDEX];

        String text = webScraper.getText(url);

        Collection<String> tags = tagExtractor.getTags(text);

        fields[KEYWORD_INDEX] = String.join(",", tags);

        return String.join("\t", (CharSequence[]) fields);
    }


}

