package stlhug.streaming.function;

import org.apache.solr.common.SolrInputDocument;
import org.apache.spark.api.java.function.Function;

import stlhug.domain.VitalRecord;

public class MapVitalRecordToSolrDocument implements Function<VitalRecord, SolrInputDocument> {

	private static final long serialVersionUID = 8189302045478735079L;

	@Override
	public SolrInputDocument call(VitalRecord record) throws Exception {
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", record.getSolrId());
		doc.setField("patientId", record.getPatientId());
		doc.setField("event_timestamp", record.getTimestamp());
		doc.setField("sysBP", record.getSysBP());
		doc.setField("diaBP", record.getDiaBP());
		doc.setField("heartRate", record.getHeartRate());
		doc.setField("temp", record.getTemp());
		return doc;
	}


}
