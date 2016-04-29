The instructions below reference the Hortonworks 2.4 Sandbox. If you are using a Cloudera or
Map-R distribution, you will need to make allowances for the different layout.

1) Download the Hortonworks Sandbox from http://hortonworks.com/hdp/downloads/

1.1) Start the Sandbox. Log in using your favorite SSH client.

2) Compile the project using Maven. Upload the resulting jar to /root/spark.

3) Set up Kafka queue "patientVitals"
cd /usr/hdp/current/kafka-broker
bin/kafka-topics.sh --create --zookeeper sandbox.hortonworks.com:2181 --replication-factor 1 --partitions 1 --topic patientVitals

4) Start up Kafka console consumer
bin/kafka-console-consumer.sh --zookeeper sandbox.hortonworks.com:2181 --topic patientVitals --from-beginning

5) In a new session, start patient feeds
/root/spark/patient/steadyStan.sh

6) Watch feeds in console consumer window

7) Run PatientVitalStream1
/root/spark/patientVitalStream1.sh

8) View Spark Streaming UI
http://sandbox.hortonworks.com:4040/streaming/

9) Edit the Spark Defaults configuration file (/usr/hdp/current/spark-client/conf/spark-default.conf). 
Add the line below to the list of configurations:
spark.sql.hive.thriftServer.singleSession true

10) Run PatientVitalStream2
/root/spark/patientVitalStream2.sh

11) View Spark Streaming UI
http://sandbox.hortonworks.com:4040/streaming/

12) Start Thrift server
/usr/hdp/current/spark-client/sbin/start-thriftserver.sh
/usr/hdp/current/spark-client/bin/beeline
!connect jdbc:hive2://localhost:10001
select * from cumulativeVitals
 
13) Run PatientVitalStream3
/root/spark/patientVitalStream3.sh

14) View Spark Streaming UI
http://sandbox.hortonworks.com:4040/streaming/

15) Start Thrift server
/usr/hdp/current/spark-client/sbin/start-thriftserver.sh
/usr/hdp/current/spark-client/bin/beeline
!connect jdbc:hive2://localhost:10001
select * from cumulativeVitals;
select * from windowVitals;
  
 