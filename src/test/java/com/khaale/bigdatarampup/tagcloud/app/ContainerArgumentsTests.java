package com.khaale.bigdatarampup.tagcloud.app;

import com.khaale.bigdatarampup.tagcloud.yarn.ContainerParameters;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Aleksander_Khanteev on 3/25/2016.
 */
public class ContainerArgumentsTests {

    @Test
    public void get_shouldReturnEmpty_whenLinesCountIs0() {
        //arrange
        int linesCount = 0;
        int containersCount = 3;

        //act
        List<ContainerParameters> actual = ContainerParameters.get("", linesCount, containersCount);

        //assert
        assertEquals(actual.size(), 3);
        assertEquals(0, actual.get(0).getSkip());
        assertEquals(0, actual.get(0).getLimit());
    }

    @Test
    public void get_shouldReturnEmpty_whenLinesCountIs1() {
        //arrange
        int linesCount = 0;
        int containersCount = 3;

        //act
        List<ContainerParameters> actual = ContainerParameters.get("", linesCount, containersCount);

        //assert
        assertEquals(actual.size(), 3);
        assertEquals(0, actual.get(0).getSkip());
        assertEquals(0, actual.get(0).getLimit());
    }

    @Test
    public void get_shouldReturn1_whenContainersCountIs1() {
        //arrange
        int linesCount = 10;
        int containersCount = 1;

        //act
        List<ContainerParameters> actual = ContainerParameters.get("", linesCount, containersCount);

        //assert
        assertEquals(1, actual.size());
        assertEquals(1, actual.get(0).getSkip());
        assertEquals(9, actual.get(0).getLimit());
    }

    @Test
    public void get_shouldReturn2_whenContainersCountIs2() {
        //arrange
        int linesCount = 6;
        int containersCount = 2;

        //act
        List<ContainerParameters> actual = ContainerParameters.get("", linesCount, containersCount);

        //assert
        assertEquals(2, actual.size());
        assertEquals(1, actual.get(0).getSkip());
        assertEquals(2, actual.get(0).getLimit());
        assertEquals(3, actual.get(1).getSkip());
        assertEquals(3, actual.get(1).getLimit());
    }

    @Test
    public void get_shouldReturn2_whenContainersCountIs_2() {
        //arrange
        int linesCount = 2;
        int containersCount = 2;

        //act
        List<ContainerParameters> actual = ContainerParameters.get("", linesCount, containersCount);

        //assert
        assertEquals(2, actual.size());
        assertEquals(1, actual.get(0).getSkip());
        assertEquals(0, actual.get(0).getLimit());
        assertEquals(1, actual.get(1).getSkip());
        assertEquals(1, actual.get(1).getLimit());
    }
}
