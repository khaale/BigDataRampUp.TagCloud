package com.khaale.bigdatarampup.tagcloud.app;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Optional;

/**
 *  Provides facade for HDFS operations.
 */
public class FileSystemFacade {

    private final Configuration conf;

    public FileSystemFacade() {

        this.conf = new Configuration();
        conf.addResource(new Path("/etc/hadoop/conf/core-site.xml"));
        conf.addResource(new Path("/etc/hadoop/conf/hdfs-site.xml"));
    }

    public String readFile(String path) {
        return readFile(path, 0, Optional.empty());
    }

    public String readFile(String path, int skipLines, int limitLines) {
        return readFile(path, skipLines, Optional.of(limitLines));
    }

    public String readFile(String path, Integer skipLines, Optional<Integer> limitLines) {
        try{
            FileSystem fs =  FileSystem.get(conf);
            Path hdpPath = new Path(path);

            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(hdpPath)));

            StringBuilder sb = new StringBuilder();
            String line;
            int lineIndex = 0;
            while ((line = br.readLine()) != null && (!limitLines.isPresent() || (lineIndex < skipLines + limitLines.get()))) {

                if (lineIndex >= skipLines) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                }

                lineIndex++;
            }
            return sb.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getLinesCount(String path) {
        try{
            FileSystem fs =  FileSystem.get(conf);
            Path hdpPath = new Path(path);

            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(hdpPath)));

            int lineIndex = 0;
            while (br.readLine() != null) {

                lineIndex++;
            }
            return lineIndex;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFile(String path, String content) {
        try{
            FileSystem fs =  FileSystem.get(conf);
            Path hdpPath = new Path(path);

            if (fs.exists(hdpPath))
            {
                fs.delete(hdpPath, true);
            }

            FSDataOutputStream fin = fs.create(hdpPath);
            fin.write(content.getBytes("UTF-8"));
            fin.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void appendToFile(String path, String content) {
        try{
            FileSystem fs =  FileSystem.get(conf);
            Path hdpPath = new Path(path);

            byte[] buffer = content.getBytes("UTF-8");
            FSDataOutputStream fin = fs.append(hdpPath, buffer.length);
            fin.write(buffer);
            fin.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeFile(String path) {
        try{
            FileSystem fs =  FileSystem.get(conf);
            Path hdpPath = new Path(path);

            if (fs.exists(hdpPath))
            {
                fs.delete(hdpPath, true);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Collect given files to the single file.
     * @param filePathsToCollect input files
     * @param outputFilePath output file
     * @param header prepends header if specified.
     */
    public void collectFiles(Collection<String> filePathsToCollect, String outputFilePath, Optional<String> header) {
        try{
            FileSystem fs =  FileSystem.get(conf);
            Path out = new Path(outputFilePath);

            if (fs.exists(out))
            {
                fs.delete(out, true);
            }

            try(FSDataOutputStream outputStream = fs.create(out)) {

                if (header.isPresent()) {
                    outputStream.write(header.get().getBytes("UTF-8"));
                }
                for (String inPath : filePathsToCollect) {

                    try(FSDataInputStream inputStream = fs.open(new Path(inPath))) {
                        IOUtils.copyBytes(inputStream, outputStream, 4096, false);
                    }
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
