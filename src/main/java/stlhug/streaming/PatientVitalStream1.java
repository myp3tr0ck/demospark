package stlhug.streaming;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import stlhug.domain.VitalRecord;
import stlhug.streaming.function.MapMessageToVital;

public class PatientVitalStream1 {

	public static void main(String[] args) {
		
		String zkQuorum = args[1];
		String kafkaConsumerGroupId = args[2];
		
		SparkConf conf = new SparkConf()
				.setMaster("local[2]")
				.setAppName("PatientVitals");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));

		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("patientVitals", 1);
		JavaPairReceiverInputDStream<String, String> kafkaStream = 
		     KafkaUtils.createStream(jssc,
		     zkQuorum, kafkaConsumerGroupId, map );
		
		JavaDStream<VitalRecord> vitals = kafkaStream.map(new MapMessageToVital());
		vitals.print();
		
		jssc.start();
		jssc.awaitTermination();
	}

}
