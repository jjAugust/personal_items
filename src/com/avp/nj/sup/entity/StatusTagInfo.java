package com.avp.nj.sup.entity;

import java.util.HashMap;

public class StatusTagInfo {
    private String id;
    private HashMap<String, String> status;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public HashMap<String, String> getStatus() {
		return status;
	}
	public void setStatus(HashMap<String, String> status) {
		this.status = status;
	}
	public StatusTagInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StatusTagInfo(String id, HashMap<String, String> status) {
		super();
		this.id = id;
		this.status = status;
	}
    
    
}
