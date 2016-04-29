package stlhug.streaming;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import stlhug.domain.VitalRecord;
import stlhug.streaming.function.IndexSolrDocs;
import stlhug.streaming.function.MapMessageToVital;
import stlhug.streaming.function.MapVitalRecordToSolrDocument;

public class PatientVitalStreamBonus {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		String zkQuorum = args[1];
		String kafkaConsumerGroupId = args[2];
		String solrZkHost = args[3];
		String collection = args[4];
		int batchSize = Integer.parseInt(args[5]);

		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("PatientVitals");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));

		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("patientVitals", 1);
		JavaPairReceiverInputDStream<String, String> kafkaStream = KafkaUtils.createStream(jssc, zkQuorum,
				kafkaConsumerGroupId, map);

		JavaDStream<VitalRecord> vitals = kafkaStream.map(new MapMessageToVital());

		JavaDStream<SolrInputDocument> documents = vitals.map(new MapVitalRecordToSolrDocument());

		//TODO: Correct this example to write out the SolrDocuments to use the Streaming function
		documents.foreachRDD(new IndexSolrDocs(solrZkHost, collection, batchSize));
		//SolrSupport.indexDStreamOfDocs(solrZkHost, collection, batchSize, documents.dstream());

		jssc.start();
		jssc.awaitTermination();
	}

}
