package com.khaale.bigdatarampup.tagcloud.app;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Aleksander on 22.03.2016.
 */
public class TagExtractor {

    private static Set<String> stopWords = new HashSet<>(Arrays.asList(
            "usd",
            "to",
            "the",
            "a",
            "for",
            "in",
            "all",
            "your"
    ));

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
                        //.filter(s -> !stopWords.contains(s))
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.<String>counting()));

        return grouped.entrySet().stream()
                .sorted(Comparator.comparing(e -> -e.getValue()))
                .map(Map.Entry::getKey)
                .limit(10)
                .collect(Collectors.toList());


    }
}
