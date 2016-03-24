package com.khaale.bigdatarampup.tagcloud.app;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FileSystemFacade {

    private Configuration conf;

    public FileSystemFacade() {

        this.conf = new Configuration();
        conf.addResource(new Path("/etc/hadoop/conf/core-site.xml"));
        conf.addResource(new Path("/etc/hadoop/conf/hdfs-site.xml"));
    }

    public String readFile(String path) {
        try{
            FileSystem fs =  FileSystem.get(conf);
            Path hdpPath = new Path(path);

            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(hdpPath)));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            return sb.toString();
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
}
