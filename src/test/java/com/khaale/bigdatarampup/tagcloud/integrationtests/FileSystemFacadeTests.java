package com.khaale.bigdatarampup.tagcloud.integrationtests;

import com.khaale.bigdatarampup.tagcloud.app.FileSystemFacade;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

/**
 * Created by Aleksander_Khanteev on 3/24/2016.
 */
public class FileSystemFacadeTests {

    @Test
    public void readFile_shouldReadFromLocalFileSystem() {

        //arrange
        String hadoopFilePath = "/data/tagcloud/test/user.profile.tags.us.txt";
        //String hadoopFilePath = "c:\\Work\\Projects\\BigDataRampUp\\BigDataRampUp.TagCloud\\src\\test\\resources\\user.profile.tags.us.txt";

        //act
        FileSystemFacade sut = new FileSystemFacade();
        String fileContent = sut.readFile(hadoopFilePath, 0 , 10);

        //assert
        System.out.println(fileContent);
        assertNotNull(fileContent);
        assertNotEquals(fileContent.length(), 0);
    }

    @Test
    public void readFile_shouldWriteToLocalFileSystem() {

        //arrange
        String hadoopFilePath =  "/data/tagcloud/test/FileSystemFacadeTests.txt";
        //String hadoopFilePath = "c:\\Work\\Projects\\BigDataRampUp\\BigDataRampUp.TagCloud\\src\\test\\resources\\user.profile.tags.us.txt.filled";
        String content = "test";

        //act
        FileSystemFacade sut = new FileSystemFacade();
        sut.writeFile(hadoopFilePath, content);
    }

    @Test
    public void appendToFile_shouldAppend() {
        //arrange
        String hadoopFilePath =  "/data/tagcloud/test/append_test.txt";
        //String hadoopFilePath = "c:\\Work\\Projects\\BigDataRampUp\\BigDataRampUp.TagCloud\\src\\test\\resources\\user.profile.tags.us.txt.filled";

        //act
        FileSystemFacade sut = new FileSystemFacade();

        sut.removeFile(hadoopFilePath);
        sut.writeFile(hadoopFilePath, "header" + System.lineSeparator());
        sut.appendToFile(hadoopFilePath, "line1" + System.lineSeparator());
        sut.appendToFile(hadoopFilePath, "line2" + System.lineSeparator());

        int linesCount = sut.getLinesCount(hadoopFilePath);

        assertEquals(3, linesCount);
    }
}
