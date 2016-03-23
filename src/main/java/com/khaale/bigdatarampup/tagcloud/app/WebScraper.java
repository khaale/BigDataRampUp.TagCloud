package com.khaale.bigdatarampup.tagcloud.app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Aleksander on 22.03.2016.
 */
public class WebScraper {

    public String getText(String url) {

        try {
            Document doc = Jsoup.connect(url).get();
            //Element title = doc.select("div.widget.prod-info-title h1").first();
            //return title.text();

            Stream<String> lines =
                    Stream.of(doc.text()
                            .split("\\r?\\n"))
                            //perhaps a jsoup bug - some weird markup isn't removed
                            .filter(s -> !s.contains("\t"));

            return lines.collect(Collectors.joining(" "));
        }
        catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }
}

