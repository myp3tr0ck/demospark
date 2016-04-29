package stlhug.streaming;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.hive.HiveContext;
import org.apache.spark.sql.hive.thriftserver.HiveThriftServer2;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import stlhug.domain.VitalRecord;
import stlhug.streaming.function.ConvertStreamToDataFrame;
import stlhug.streaming.function.MapMessageToVital;

public class PatientVitalStream2 {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		String zkQuorum = args[1];
		String kafkaConsumerGroupId = args[2];

		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("PatientVitals");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
		

		HiveContext sqlContext = new HiveContext(jssc.sparkContext().sc());
		sqlContext.setConf("hive.server2.thrift.port", "10001");
		
		
		initializeTable(jssc, sqlContext, "cumulativeVitals");
		sqlContext.sql("select * from cumulativeVitals").show();

		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("patientVitals", 1);
		JavaPairReceiverInputDStream<String, String> kafkaStream = KafkaUtils.createStream(jssc, zkQuorum,
				kafkaConsumerGroupId, map);

		JavaDStream<VitalRecord> vitals = kafkaStream.map(new MapMessageToVital());

		vitals.foreach(new ConvertStreamToDataFrame("cumulativeVitals"));
		
		HiveThriftServer2.startWithContext(sqlContext);
		jssc.start();
		jssc.awaitTermination();
	}

	private static void initializeTable(JavaStreamingContext jssc, HiveContext sqlContext, String tableName) {
		List<VitalRecord> records = new ArrayList<VitalRecord>();
		VitalRecord record = new VitalRecord(0, new Date(new java.util.Date().getTime()), 0,0,0,0);
		records.add(record);
		JavaRDD<VitalRecord> recordRDD = jssc.sparkContext().parallelize(records);
		DataFrame recordDF = sqlContext.createDataFrame(recordRDD, VitalRecord.class);
		recordDF.registerTempTable(tableName);	
	}

}
