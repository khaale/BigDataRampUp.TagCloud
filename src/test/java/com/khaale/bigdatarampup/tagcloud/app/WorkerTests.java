package com.khaale.bigdatarampup.tagcloud.app;

import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Created by Aleksander on 24.03.2016.
 */
public class WorkerTests {

    @Test
    public void shouldCallFileSystemFacade_toReadLinesCount() {
        //arrange
        String inputFilePath = "data.txt";

        FileSystemFacade fsFacadeMock = mock(FileSystemFacade.class);

        //act
        Worker sut = new Worker(fsFacadeMock, mock(WebPageContentExtractor.class), mock(TagExtractor.class));
        sut.fillKeywordValues(inputFilePath);

        //assert
        verify(fsFacadeMock).getLinesCount(inputFilePath);
    }

    @Test
    public void shouldCallFileSystemFacade_toReadInputText() {
        //arrange
        String inputFilePath = "data.txt";

        FileSystemFacade fsFacadeMock = mock(FileSystemFacade.class);
        when(fsFacadeMock.getLinesCount(any(String.class))).thenReturn(10);

        //act
        Worker sut = new Worker(fsFacadeMock, mock(WebPageContentExtractor.class), mock(TagExtractor.class));
        sut.fillKeywordValues(inputFilePath);

        //assert
        verify(fsFacadeMock).readFile(inputFilePath, 1, 9);
    }

    @Test
    public void shouldCallWebScraper_toGetTextForEachLinkInInputText() {
        //arrange
        final String inputFilePath = "data.txt";
        final String input =
                "282163091263\t\tON\tCPC\tBROAD\thttp://www.leningrad-sbp.ru\r\n" +
                "282163091263\t\tON\tCPC\tBROAD\thttp://www.example.com";

        FileSystemFacade fsFacadeStub = mock(FileSystemFacade.class);
        when(fsFacadeStub.readFile(any(String.class), any(int.class), any(int.class))).thenReturn(input);

        WebPageContentExtractor contentExtractorMock = mock(WebPageContentExtractor.class);
        when(contentExtractorMock.getContent(any(String.class))).thenReturn(Optional.empty());

        //act
        Worker sut = new Worker(fsFacadeStub, contentExtractorMock, mock(TagExtractor.class));
        sut.fillKeywordValues(inputFilePath);

        //assert
        verify(contentExtractorMock).getContent("http://www.leningrad-sbp.ru");
        verify(contentExtractorMock).getContent("http://www.example.com");
    }

    @Test
    public void shouldCallTagExtractor_toExtractTagsFromText() {
        //arrange
        final String inputFilePath = "data.txt";
        final String input =
                "282163091263\t\tON\tCPC\tBROAD\thttp://www.leningrad-sbp.ru";
        final String text = "Hadoop optimizes on Data Locality: Moving compute to data is cheaper than moving data to compute.";

        FileSystemFacade fsFacadeStub = mock(FileSystemFacade.class);
        when(fsFacadeStub.readFile(any(String.class), any(int.class), any(int.class))).thenReturn(input);

        WebPageContentExtractor contentExtractorStub = mock(WebPageContentExtractor.class);
        when(contentExtractorStub.getContent(any(String.class))).thenReturn(Optional.of(text));

        TagExtractor tagExtractor = mock(TagExtractor.class);

        //act
        Worker sut = new Worker(fsFacadeStub, contentExtractorStub, tagExtractor);
        sut.fillKeywordValues(inputFilePath);

        //assert
        verify(tagExtractor).getTags(text);
    }


    @Test
    public void shouldCallFileSystemFacade_toAppendFilledKeywords() {
        //arrange
        final String inputFilePath = "data.txt";
        final String input =
                "282163091263\t\tON\tCPC\tBROAD\thttp://www.leningrad-sbp.ru";
        final String text = "Hadoop optimizes on Data Locality: Moving compute to data is cheaper than moving data to compute.";

        final String expectedResult =
                "282163091263\tbig,data\tON\tCPC\tBROAD\thttp://www.leningrad-sbp.ru" + System.lineSeparator();

        FileSystemFacade fsFacadeStub = mock(FileSystemFacade.class);
        when(fsFacadeStub.readFile(any(String.class), any(int.class), any(int.class))).thenReturn(input);

        ContentExtractor contentExtractorStub = mock(ContentExtractor.class);
        when(contentExtractorStub.getContent(any(String.class))).thenReturn(Optional.of(text));

        TagExtractor tagExtractor = mock(TagExtractor.class);
        when(tagExtractor.getTags(any(String.class))).thenReturn(Arrays.asList("big","data"));

        //act
        Worker sut = new Worker(fsFacadeStub, contentExtractorStub, tagExtractor);
        sut.fillKeywordValues(inputFilePath);

        //assert
        verify(fsFacadeStub).writeFile(inputFilePath + ".out", expectedResult);
    }


}
