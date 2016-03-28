# BigDataRumpUp.TagCloud
Big Data Rump Up training, 2nd homework: TagCloud YARN app

## How to run

```bash
mvn package
hadoop fs -copyFromLocal -f ./target/tagcloud-1.0-SNAPSHOT-jar-with-dependencies.jar /apps/tagcloud/tagcloud.jar
hadoop jar ./target/tagcloud-1.0-SNAPSHOT-jar-with-dependencies.jar  com.khaale.bigdatarampup.tagcloud.yarn.Client /data/tagcloud/prod/user.profile.tags.us.txt 2 hdfs:///apps/tagcloud/tagcloud.jar
```

## How does it work

1. _Client_ submits _ApplicationMaster_ to YARN
2. _ApplicationMaster_ gets a number of records from input file and generates container parameters. For example, with 2 containers and 500-lines input file, the following parameters will be generated:
  - 1st container: skip 1 line (header), read 249 lines.
  - 2nd container: skip 250 lines, read 250 lines.
3. _ApplicationMaster_ launches _ContainerWorker_'s and waits for completion.
4. Each _ContainerWorker_ processes it's own piece of work and writes result as a separate file to HDFS
5. _ApplicationMaster_ collects container's files to the single output file.
