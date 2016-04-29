package stlhug.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class VitalRecord implements Serializable {

	private static final long serialVersionUID = -5709964804126453227L;

	private double heartRate;
	private double sysBP;
	private double diaBP;
	private double temp;
	private int patientId;
	private Date timestamp;
	
	public VitalRecord() {
		
	}
	
	public VitalRecord(int patientId, Date timestamp, double d, double e, double f, double g) {
		this.patientId = patientId;
		this.timestamp =timestamp;
		this.heartRate = d;
		this.sysBP = e;
		this.diaBP = f;
		this.temp = g;
	}
	
	public VitalRecord(String serialized) {
		String[] items = serialized.split("\\|");
		patientId = Integer.parseInt(items[0]);
		try {
			timestamp = new Date(new SimpleDateFormat().parse(items[1]).getTime());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		heartRate = Float.parseFloat(items[2]);
		sysBP = Float.parseFloat(items[3]);
		diaBP = Float.parseFloat(items[4]);
		temp = Float.parseFloat(items[5]);
	}
	
	public double getHeartRate() {
		return heartRate;
	}
	public void setHeartRate(double heartRate) {
		this.heartRate = heartRate;
	}
	public double getSysBP() {
		return sysBP;
	}
	public void setSysBP(double sysBP) {
		this.sysBP = sysBP;
	}
	public double getDiaBP() {
		return diaBP;
	}
	public void setDiaBP(double diaBP) {
		this.diaBP = diaBP;
	}
	public double getTemp() {
		return temp;
	}
	public void setTemp(double temp) {
		this.temp = temp;
	}
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String toString() {
		return new StringBuilder().append(patientId).append("|")
				.append(new SimpleDateFormat().format(timestamp)).append("|")
				.append(getHeartRate()).append("|")
				.append(getSysBP()).append("|")
				.append(getDiaBP()).append("|")
				.append(getTemp()).toString();		
	}

	public String getSolrId() {
		return patientId + "," + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(timestamp);
	}
}
