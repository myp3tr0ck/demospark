package stlhug.streaming.function;

import org.apache.solr.common.SolrInputDocument;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;

import com.lucidworks.spark.util.SolrSupport;

public class IndexSolrDocs implements Function<JavaRDD<SolrInputDocument>, Void> {
	
	private static final long serialVersionUID = -3590215952164185048L;
	
	private String solrZkHost;
	private String collection; 
	private int batchSize;
	
	public IndexSolrDocs(String solrZkHost, String collection, int batchSize) {
		this.solrZkHost = solrZkHost;
		this.collection = collection;
		this.batchSize = batchSize;
		
	}

	@Override
	public Void call(JavaRDD<SolrInputDocument> rdd) throws Exception {
		SolrSupport.indexDocs(solrZkHost, collection, batchSize, rdd.rdd());
		return null; 
	}

}
