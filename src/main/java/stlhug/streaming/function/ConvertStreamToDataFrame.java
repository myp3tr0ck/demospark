package stlhug.streaming.function;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;

import stlhug.domain.VitalRecord;

public class ConvertStreamToDataFrame implements Function<JavaRDD<VitalRecord>, Void> {

	private static final long serialVersionUID = -6436230469985833480L;

	private String tableName;

	public ConvertStreamToDataFrame(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public Void call(JavaRDD<VitalRecord> vitalRdd) throws Exception {
		if (!vitalRdd.isEmpty()) {
			SQLContext sqlContext = SQLContext.getOrCreate(vitalRdd.context());

			DataFrame vitalsDataFrame = sqlContext.createDataFrame(vitalRdd, VitalRecord.class);

			vitalsDataFrame.write().mode(SaveMode.Append).saveAsTable(tableName);

			DataFrame vitalAverageDataFrame = sqlContext
					.sql("select patientId, avg(heartRate) from " + tableName + " group by patientId");
			vitalAverageDataFrame.show();
		}
		return null;
	}
}



// Register as table
// sqlContext.registerDataFrameAsTable(vitalsDataFrame, tableName);

