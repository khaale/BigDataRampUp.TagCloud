hadoop fs -copyFromLocal -f ./target/tagcloud-1.0-SNAPSHOT-jar-with-dependencies.jar /apps/tagcloud/tagcloud.jar
hadoop jar ./target/tagcloud-1.0-SNAPSHOT-jar-with-dependencies.jar  com.khaale.bigdatarampup.tagcloud.yarn.Client /data/tagcloud/prod/user.profile.tags.us.txt 2 hdfs:///apps/tagcloud/tagcloud.jar
