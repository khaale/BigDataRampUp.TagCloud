package com.khaale.bigdatarampup.tagcloud.app;

import org.apache.commons.httpclient.HttpException;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Aleksander on 22.03.2016.
 */
public class WebScraper {

    private final static int MAX_RETRY_COUNT = 3;
    private final static Logger logger = LoggerFactory.getLogger(WebScraper.class);

    public Optional<String> getText(String url) {

        Optional<String> result = Optional.empty();
        boolean needRetry;
        int trialCount = 0;

        do {
            needRetry = false;
            trialCount ++;

            try {

                result = Optional.of(getContentInternal(url));

            } catch (HttpStatusException e) {

                logger.warn("{}. Status: {}. Url: {}",  e.getMessage(), e.getStatusCode(), e.getUrl());

            } catch (SocketTimeoutException e) {

                logger.warn(e.getMessage());
                needRetry = true;

            } catch (IOException e) {

                logger.error("Error fetching data.", e);

            }
        } while (needRetry && trialCount <= MAX_RETRY_COUNT);

        return result;
    }

    private String getContentInternal(String url) throws IOException {
        String result;Connection connection = Jsoup.connect(url);
        connection.timeout(5 * 1000);
        Document doc = connection.get();
        //Element title = doc.select("div.widget.prod-info-title h1").first();
        //return title.text();

        Stream<String> lines =
                Stream.of(doc.text()
                        .split("\\r?\\n"))
                        //perhaps a jsoup bug - some weird markup isn't removed
                        .filter(s -> !s.contains("\t"));

        result = lines.collect(Collectors.joining(" "));
        return result;
    }
}

