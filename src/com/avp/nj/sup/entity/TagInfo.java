package com.avp.nj.sup.entity;

import java.util.Map;

public class TagInfo {
	String timestamp;
	Integer ack;
	String value;
	Map<String, String> valDesc;
	String tagname;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getAck() {
		return ack;
	}

	public void setAck(Integer ack) {
		this.ack = ack;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Map<String, String> getValDesc() {
		return valDesc;
	}

	public void setValDesc(Map<String, String> valDesc) {
		this.valDesc = valDesc;
	}

	public TagInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public TagInfo(String timestamp, Integer ack, String value,
			Map<String, String> valDesc) {
		super();
		this.timestamp = timestamp;
		this.ack = ack;
		this.value = value;
		this.valDesc = valDesc;
	}

}
