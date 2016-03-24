package com.khaale.bigdatarampup.tagcloud.integrationtests;

import com.khaale.bigdatarampup.tagcloud.app.FileSystemFacade;
import com.khaale.bigdatarampup.tagcloud.app.TagExtractor;
import com.khaale.bigdatarampup.tagcloud.app.WebScraper;
import com.khaale.bigdatarampup.tagcloud.app.Worker;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(IntegrationTest.class)
public class WorkerTests {

    @Test
    @Ignore
    public void shouldFillKeywordValues() {

        //arrange
        String filePath = "/data/tagcloud/test/user.profile.tags.us.txt";

        //act
        Worker worker = new Worker(new FileSystemFacade(), new WebScraper(), new TagExtractor());
        worker.fillKeywordValues(filePath);
    }
}
