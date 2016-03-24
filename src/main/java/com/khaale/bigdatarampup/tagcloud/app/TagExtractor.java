package com.khaale.bigdatarampup.tagcloud.app;

import com.google.common.io.Resources;
import org.apache.commons.io.Charsets;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Aleksander on 22.03.2016.
 */
public class TagExtractor {

    private Set<String> stopWords;

    public TagExtractor() {

        URL url = Resources.getResource("stopwords.txt");
        try {
            String text = Resources.toString(url, Charsets.UTF_8);

            stopWords = Stream.of(text.split("\\r?\\n")).collect(Collectors.toSet());

        } catch (IOException e) {
            stopWords = new HashSet<>();
            e.printStackTrace();
        }

    }

    public Collection<String> getTags(String text) {

        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        String clean = text
                .replaceAll("[^\\p{L}\\d]"," ")
                .replaceAll(" ", "  ")
                .replaceAll(" [\\d]+ ", " ")
                .replaceAll("[ ]+", " ");

        Map<String, Long> grouped =
                Stream.of(clean.split(" "))
                        .map(String::toLowerCase)
                        //it doesn't make sense to use small stop-words dictionary there
                        .filter(this::filterWord)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.<String>counting()));

        return grouped.entrySet().stream()
                .sorted(Comparator.comparing(e -> -e.getValue()))
                .map(Map.Entry::getKey)
                .limit(10)
                .collect(Collectors.toList());
    }

    private boolean filterWord(String word) {

        boolean result = true;

        if (word.length() <= 1) {
            result = false;
        }

        if (stopWords.contains(word)) {
            result = false;
        }

        return result;

    }
}
