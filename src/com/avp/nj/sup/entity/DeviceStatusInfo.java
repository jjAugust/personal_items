package com.avp.nj.sup.entity;

public class DeviceStatusInfo {

	private String deviceSubId;
	private String stationName;
	private String deviceName;
	private String TAG;
	private String tagval;
	private String tagname;
	private String time;
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getTAG() {
		return TAG;
	}
	public void settAG(String TAG) {
		this.TAG = TAG;
	}
	public String getTagval() {
		return tagval;
	}
	public void setTagval(String tagval) {
		this.tagval = tagval;
	}
	public String getTagname() {
		return tagname;
	}
	public void setTagname(String tagname) {
		this.tagname = tagname;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDeviceSubId() {
		return deviceSubId;
	}
	public void setDeviceSubId(String deviceSubId) {
		this.deviceSubId = deviceSubId;
	}
	public DeviceStatusInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DeviceStatusInfo(String deviceSubId, String stationName,
			String deviceName, String TAG, String tagval, String tagname,
			String time) {
		super();
		this.deviceSubId = deviceSubId;
		this.stationName = stationName;
		this.deviceName = deviceName;
		this.TAG = TAG;
		this.tagval = tagval;
		this.tagname = tagname;
		this.time = time;
	}
	
	
	
	
	
}
