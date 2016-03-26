package com.khaale.bigdatarampup.tagcloud.app;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 *
 * Created by Aleksander_Khanteev on 3/25/2016.
 *
 */
public class UrlContentExtractorTests {

    @Test
    public void shouldGetContent_FromUrlPath() {
       //arrange
        String url = "http://www.miniinthebox.com/oil-pollution-cleaning-automobile-engine-pipe-with-reinigungspistole-spray-gun-tool_p4815979.html";
        //act
        UrlContentExtractor sut = new UrlContentExtractor();
        Optional<String> actual = sut.getContent(url);
        //assert
        assertEquals("/oil-pollution-cleaning-automobile-engine-pipe-with-reinigungspistole-spray-gun-tool", actual.get());
    }
}
