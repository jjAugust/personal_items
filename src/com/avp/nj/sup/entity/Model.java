package com.avp.nj.sup.entity;

import java.util.List;

public class Model {
	String id;
	String name;
	List<SubModel> modes;

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

	public List<SubModel> getModes() {
		return modes;
	}

	public void setModes(List<SubModel> modes) {
		this.modes = modes;
	}

	public Model() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Model(String id, String name, List<SubModel> modes) {
		super();
		this.id = id;
		this.name = name;
		this.modes = modes;
	}

}
