package stlhug.client;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import stlhug.domain.VitalRecord;

public class PatientVitalSource {

	private Producer<String, String> kafkaProducer;
	private Random seed;
	
	private int patientId;
	
	private float baseHeartRate;
	private float baseSysBP;
	private float baseDiaBP; 
	private float baseTemp;
	
	private float ampHeartRate;
	private float ampBP;
	private float ampTemp;
	private String bootstrapServers;
	private long sleepInMillis;
	
	public PatientVitalSource(
			int patientId,
			float baseHeartRate, 
			float baseSysBP, 
			float baseDiaBP, 
			float baseTemp,
			float ampHeartRate,
			float ampBP,
			float ampTemp,
			String bootstrapServers,
			long sleepInMillis
			) {
		seed =  new Random();
		
		this.patientId = patientId;
		
		this.baseHeartRate = baseHeartRate;
		this.baseSysBP = baseSysBP;
		this.baseDiaBP = baseDiaBP;
		this.baseTemp = baseTemp;
		
		this.ampBP = ampBP;
		this.ampHeartRate = ampHeartRate;
		this.ampTemp = ampTemp;
		this.bootstrapServers = bootstrapServers;
		this.sleepInMillis = sleepInMillis;
	}
	
	public void initialize() {
		Properties props = new Properties();
		props.put("bootstrap.servers", bootstrapServers);
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		kafkaProducer = new KafkaProducer<String, String>( props );
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void run() {
		initialize();
		System.out.println("Running ...");
		for (;;) {
			ProducerRecord record = new ProducerRecord("patientVitals", createVitalsMessage());
			kafkaProducer.send(record);
			try {
				Thread.sleep(sleepInMillis);
			} catch (InterruptedException e) {
			}
		}
	}
	
	
	private String createVitalsMessage() {
		return new VitalRecord(patientId,new java.sql.Date(new Date().getTime()),getHR(),getSysBP(),getDiaBP(),getTemp()).toString();		
	}

	private double getTemp() {
		return baseTemp + (ampTemp * Math.sin(seed.nextDouble()*2*Math.PI));
	}

	private double getDiaBP() {
		return baseDiaBP + (ampBP * Math.sin(seed.nextDouble()*2*Math.PI));
	}

	private double getSysBP() {
		return baseSysBP + (ampBP * Math.sin(seed.nextDouble()*2*Math.PI));
	}

	private double getHR() {
		return baseHeartRate + (ampHeartRate * Math.sin(seed.nextDouble()*2*Math.PI));
	}

	public static void main(String[] args) {
		float baseHeartRate = Float.parseFloat(args[0]); 
		float baseSysBP = Float.parseFloat(args[1]);
		float baseDiaBP = Float.parseFloat(args[2]);
		float baseTemp = Float.parseFloat(args[3]);
		float ampHeartRate = Float.parseFloat(args[4]);
		float ampBP = Float.parseFloat(args[5]);
		float ampTemp = Float.parseFloat(args[6]);
		int patientId = Integer.parseInt(args[7]);
		String bootstrapServers = args[8];
		long sleepInMillis = Long.parseLong(args[9]);
		
		new PatientVitalSource(patientId, 
				baseHeartRate, 
				baseSysBP, 
				baseDiaBP, 
				baseTemp,
				ampHeartRate,
				ampBP,
				ampTemp,
				bootstrapServers, 
				sleepInMillis).run();
	}
	
	
}
