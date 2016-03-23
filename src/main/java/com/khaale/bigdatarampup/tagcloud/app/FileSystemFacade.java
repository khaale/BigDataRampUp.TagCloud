package com.khaale.bigdatarampup.tagcloud.app;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FileSystemFacade {

    private Configuration conf;

    public FileSystemFacade() {

        this.conf = new Configuration();
    }

    public String readFile(String inputFilePath) {
        try{
            FileSystem fs =  FileSystem.get(conf);
            Path path = new Path(inputFilePath);

            BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(path)));

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            return line;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFile(String s, String expectedResult) {

    }
}
