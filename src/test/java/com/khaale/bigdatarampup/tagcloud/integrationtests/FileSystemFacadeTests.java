package com.khaale.bigdatarampup.tagcloud.integrationtests;

import com.khaale.bigdatarampup.tagcloud.app.FileSystemFacade;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

/**
 * Created by Aleksander_Khanteev on 3/24/2016.
 */
@Category(IntegrationTest.class)
public class FileSystemFacadeTests {

    @Test
    public void readFile_shouldReadFromLocalFileSystem() {

        //arrange
        String hadoopFilePath = "/data/tagcloud/test/user.profile.tags.us.txt";
        //String hadoopFilePath = "c:\\Work\\Projects\\BigDataRampUp\\BigDataRampUp.TagCloud\\src\\test\\resources\\user.profile.tags.us.txt";

        //act
        FileSystemFacade sut = new FileSystemFacade();
        String fileContent = sut.readFile(hadoopFilePath);

        //assert
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
}
