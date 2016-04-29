spark-submit \
--master yarn-client \
--files log4j.properties \
--conf "spark.driver.extraClassPath=/etc/hbase/conf/:/usr/hdp/current/hbase-client/lib/htrace-core-3.1.0-incubating.jar:/usr/hdp/2.4.0.0-169/hadoop/client/hadoop-yarn-common.jar:/usr/hdp/current/hadoop-yarn-client/lib/jackson-mapper-asl-1.9.13.jar:/usr/hdp/current/hbase-client/lib/guava-12.0.1.jar:/usr/hdp/2.4.0.0-169/hadoop/hadoop-common.jar" \
--conf "spark.executor.extraJavaOptions=-Dsun.security.krb5.debug=true -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:NewSize=128m -XX:CMSInitiatingOccupancyFraction=70 -XX:-CMSConcurrentMTEnabled -Djava.net.preferIPv4Stack=true -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps" \
--executor-cores 1 \
--class stlhug.streaming.PatientVitalStream3 \
DemoSpark-0.0.1-SNAPSHOT-shaded.jar \
Demo \
sandbox.hortonworks.com:2181 \
vitals3
