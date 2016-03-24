package com.khaale.bigdatarampup.tagcloud.app;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Created by Aleksander on 24.03.2016.
 */
public class WorkerTests {

    @Test
    public void shouldCallFileSystemFacade_toReadInputText() {
        //arrange
        String inputFilePath = "data.txt";

        FileSystemFacade fsFacadeMock = mock(FileSystemFacade.class);

        //act
        Worker sut = new Worker(fsFacadeMock, mock(WebScraper.class), mock(TagExtractor.class));
        sut.fillKeywordValues(inputFilePath);

        //assert
        verify(fsFacadeMock).readFile(inputFilePath);
    }

    @Test
    public void shouldCallWebScraper_toGetTextForEachLinkInInputText() {
        //arrange
        final String inputFilePath = "data.txt";
        final String input =
                "ID\tKeyword Value\tKeyword Status\tPricing Type\tKeyword Match Type\tDestination URL\r\n" +
                "282163091263\t\tON\tCPC\tBROAD\thttp://www.leningrad-sbp.ru\r\n" +
                "282163091263\t\tON\tCPC\tBROAD\thttp://www.example.com";

        FileSystemFacade fsFacadeStub = mock(FileSystemFacade.class);
        when(fsFacadeStub.readFile(any(String.class))).thenReturn(input);

        WebScraper webScraperMock = mock(WebScraper.class);
        when(webScraperMock.getText(any(String.class))).thenReturn(Optional.empty());

        //act
        Worker sut = new Worker(fsFacadeStub, webScraperMock, mock(TagExtractor.class));
        sut.fillKeywordValues(inputFilePath);

        //assert
        verify(webScraperMock).getText("http://www.leningrad-sbp.ru");
        verify(webScraperMock).getText("http://www.example.com");
    }

    @Test
    public void shouldCallTagExtractor_toExtractTagsFromText() {
        //arrange
        final String inputFilePath = "data.txt";
        final String input =
                "ID\tKeyword Value\tKeyword Status\tPricing Type\tKeyword Match Type\tDestination URL\n" +
                "282163091263\t\tON\tCPC\tBROAD\thttp://www.leningrad-sbp.ru";
        final String text = "Hadoop optimizes on Data Locality: Moving compute to data is cheaper than moving data to compute.";

        FileSystemFacade fsFacadeStub = mock(FileSystemFacade.class);
        when(fsFacadeStub.readFile(any(String.class))).thenReturn(input);

        WebScraper webScraperStub = mock(WebScraper.class);
        when(webScraperStub.getText(any(String.class))).thenReturn(Optional.of(text));

        TagExtractor tagExtractor = mock(TagExtractor.class);

        //act
        Worker sut = new Worker(fsFacadeStub, webScraperStub, tagExtractor);
        sut.fillKeywordValues(inputFilePath);

        //assert
        verify(tagExtractor).getTags(text);
    }


    @Test
    public void shouldCallFileSystemFacade_toWriteFilledKeywords() {
        //arrange
        final String inputFilePath = "data.txt";
        final String input =
                "ID\tKeyword Value\tKeyword Status\tPricing Type\tKeyword Match Type\tDestination URL\n" +
                "282163091263\t\tON\tCPC\tBROAD\thttp://www.leningrad-sbp.ru";
        final String text = "Hadoop optimizes on Data Locality: Moving compute to data is cheaper than moving data to compute.";

        final String expectedResult =
                "ID\tKeyword Value\tKeyword Status\tPricing Type\tKeyword Match Type\tDestination URL\n" +
                "282163091263\tbig,data\tON\tCPC\tBROAD\thttp://www.leningrad-sbp.ru";

        FileSystemFacade fsFacadeStub = mock(FileSystemFacade.class);
        when(fsFacadeStub.readFile(any(String.class))).thenReturn(input);

        WebScraper webScraperStub = mock(WebScraper.class);
        when(webScraperStub.getText(any(String.class))).thenReturn(Optional.of(text));

        TagExtractor tagExtractor = mock(TagExtractor.class);
        when(tagExtractor.getTags(any(String.class))).thenReturn(Arrays.asList("big","data"));

        //act
        Worker sut = new Worker(fsFacadeStub, webScraperStub, tagExtractor);
        sut.fillKeywordValues(inputFilePath);

        //assert
        verify(fsFacadeStub).writeFile(inputFilePath + ".filled", expectedResult);
    }


}
