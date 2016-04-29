package stlhug.streaming.function;

import org.apache.spark.api.java.function.Function;

import scala.Tuple2;
import stlhug.domain.VitalRecord;

public class MapMessageToVital implements Function<Tuple2<String, String>, VitalRecord> {
	private static final long serialVersionUID = 4318594661691362812L;

	@Override
	public VitalRecord call(Tuple2<String, String> v1) throws Exception {
		return new VitalRecord(v1._2());
	}


}
