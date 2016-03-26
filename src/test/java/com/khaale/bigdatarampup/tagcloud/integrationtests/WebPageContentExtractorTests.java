package com.khaale.bigdatarampup.tagcloud.integrationtests;

import com.khaale.bigdatarampup.tagcloud.app.WebPageContentExtractor;
import java.util.Optional;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 *
 * Created by Aleksander on 22.03.2016.
 *
 */
public class WebPageContentExtractorTests {

    @Test
    public void getText_shouldReturnNonEmptyString_WhenCalledWithValidUrl() {
        //arrange
        String url = "http://www.miniinthebox.com/12v-24v-digital-led-auto-car-truck-voltmeter-gauge_p284486.html";

        //act
        WebPageContentExtractor sut = new WebPageContentExtractor();
        Optional<String> actual = sut.getContent(url);

        //assert
        assertTrue(actual.isPresent());
        assertNotEquals(actual.get().length(), 0);
    }

    @Test
    public void getText_shouldReturnEmptyOption_WhenUrlNotFound() {
        //arrange
        String url = "http://www.miniinthebox.com/hope-there-is-no-such-page";

        //act
        WebPageContentExtractor sut = new WebPageContentExtractor();
        Optional<String> actual = sut.getContent(url);

        //assert
        assertFalse(actual.isPresent());
    }

    @Test
    public void getText_shouldReturnEmptyOption_WhenUrlNotExists() {
        //arrange
        String url = "http://localhost:123";

        //act
        WebPageContentExtractor sut = new WebPageContentExtractor();
        Optional<String> actual = sut.getContent(url);

        //assert
        assertFalse(actual.isPresent());
    }
}
