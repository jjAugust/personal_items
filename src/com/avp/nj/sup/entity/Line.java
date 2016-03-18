package com.avp.nj.sup.entity;

import java.util.List;

public class Line {
	String name;
	String iconSkin;
	List<Station> children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconSkin() {
		return iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}

	public List<Station> getChildren() {
		return children;
	}

	public void setChildren(List<Station> children) {
		this.children = children;
	}

}
