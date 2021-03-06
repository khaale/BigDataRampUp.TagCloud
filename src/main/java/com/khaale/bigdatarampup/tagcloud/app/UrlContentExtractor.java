package com.khaale.bigdatarampup.tagcloud.app;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * Extracts tags source content directly from URL path.
 * Performs some basic cleanup like stripping _c124312.html postfixes.
 */
public class UrlContentExtractor implements ContentExtractor {

    @Override
    public Optional<String> getContent(String urlString) {

        try {
            URL url = new URL(urlString);

            String path = url.getPath();
            path = path.replaceFirst("_[\\w][\\d]+(\\.html)?", "");

            return Optional.of(path);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
