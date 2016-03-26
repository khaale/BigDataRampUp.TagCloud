package com.khaale.bigdatarampup.tagcloud.yarn;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Aleksander_Khanteev on 3/25/2016.
 *
 */
public class ContainerParameters {

    private final String inputFileName;
    private final String outputFileName;
    private final int skip;
    private final int limit;

    public static List<ContainerParameters> get(String inputFileName, int totalLinesCount, int containersCount) {

        List<ContainerParameters> result = new ArrayList<>();
        int linesLeft = Math.max(totalLinesCount - 1, 0);
        int batchSize = (totalLinesCount - 1) / containersCount;

        for (int i = 0; i < containersCount; i++) {

            int skip = totalLinesCount - linesLeft;

            int limit = i == containersCount - 1 ? linesLeft : batchSize;

            result.add(new ContainerParameters(
                    inputFileName,
                    String.format("%s.out.%s-%s", inputFileName, skip, limit),
                    skip,
                    limit));

            linesLeft -= limit;
        }

        return result;
    }

    public ContainerParameters(String[] args) {
        this(
                args[0],
                args[1],
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3])
        );
    }

    public ContainerParameters(String inputFileName, String outputFileName, int skip, int limit) {

        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;

        this.skip = skip;
        this.limit = limit;
    }


    public int getSkip() {
        return skip;
    }

    public int getLimit() {
        return limit;
    }

    public String[] toArgs() {
        return new String[] { inputFileName, outputFileName, String.valueOf(skip), String.valueOf(limit) };
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }
}
