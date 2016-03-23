package com.khaale.bigdatarampup.tagcloud.app;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Aleksander on 22.03.2016.
 */
public class WebScraperTests {

    @Test
    public void getText_shouldReturnText_WhenCalled() {
        //arrange
        String url = "http://www.miniinthebox.com/12v-24v-digital-led-auto-car-truck-voltmeter-gauge_p284486.html";

        //act
        WebScraper sut = new WebScraper();
        String actual = sut.getText(url);

        //assert
        assertNotNull(actual);
    }
}
