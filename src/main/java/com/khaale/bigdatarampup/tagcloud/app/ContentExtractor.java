package com.khaale.bigdatarampup.tagcloud.app;

import java.util.Optional;

/**
 * Base intetface for tag source content extractors.
 */
public interface ContentExtractor {

    /**
     * Gets content by given URL
     * @param url - url
     * @return empty option in case of error
     */
    Optional<String> getContent(String url);
}
