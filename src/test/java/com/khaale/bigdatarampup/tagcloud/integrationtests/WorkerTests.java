package com.khaale.bigdatarampup.tagcloud.integrationtests;

import com.khaale.bigdatarampup.tagcloud.app.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class WorkerTests {

    @Test
    @Ignore
    public void shouldFillKeywordValues() {

        //arrange
        String filePath = "/data/tagcloud/test/user.profile.tags.us.txt";

        //act
        Worker worker = new Worker(new FileSystemFacade(), new UrlContentExtractor(), new TagExtractor());
        worker.fillKeywordValues(filePath);
    }
}
