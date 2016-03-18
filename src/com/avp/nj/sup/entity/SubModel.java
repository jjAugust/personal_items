package com.avp.nj.sup.entity;

public class SubModel {
	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SubModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SubModel(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

}
