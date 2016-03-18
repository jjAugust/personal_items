package com.avp.nj.sup.entity;

import java.util.Map;

public class EodStatus {

	private String id;
	private Map<String, String> status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, String> getStatus() {
		return status;
	}

	public void setStatus(Map<String, String> status) {
		this.status = status;
	}

	public EodStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EodStatus(String id, Map<String, String> status) {
		super();
		this.id = id;
		this.status = status;
	}

}
