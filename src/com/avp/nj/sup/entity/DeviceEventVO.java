package com.avp.nj.sup.entity;

public class DeviceEventVO {
   private Integer id;
   private String time;
   private String level;
   private String deviceName;
   private String tagName;
   private String value;
   private String valDesc;
   private String stationNameAndId;
   private String deviceId;
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public String getLevel() {
	return level;
}
public void setLevel(String level) {
	this.level = level;
}
public String getDeviceName() {
	return deviceName;
}
public void setDeviceName(String deviceName) {
	this.deviceName = deviceName;
}
public String getTagName() {
	return tagName;
}
public void setTagName(String tagName) {
	this.tagName = tagName;
}
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}
public String getValDesc() {
	return valDesc;
}
public void setValDesc(String valDesc) {
	this.valDesc = valDesc;
}
public String getStationNameAndId() {
	return stationNameAndId;
}
public void setStationNameAndId(String stationNameAndId) {
	this.stationNameAndId = stationNameAndId;
}
public String getDeviceId() {
	return deviceId;
}
public void setDeviceId(String deviceId) {
	this.deviceId = deviceId;
}
public DeviceEventVO() {
	super();
	// TODO Auto-generated constructor stub
}
public DeviceEventVO(Integer id, String time, String deviceName,
		String tagName, String value, String valDesc, String stationNameAndId,
		String deviceId) {
	super();
	this.id = id;
	this.time = time;

	this.deviceName = deviceName;
	this.tagName = tagName;
	this.value = value;
	this.valDesc = valDesc;
	this.stationNameAndId = stationNameAndId;
	this.deviceId = deviceId;
}
   
   
   
}
