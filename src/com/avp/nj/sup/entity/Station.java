package com.avp.nj.sup.entity;

import java.util.List;

public class Station {
   String id;
   String name;
   String iconSkin;
   String index;
   List<Device> children;
   
   
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
public String getIconSkin() {
	return iconSkin;
}
public void setIconSkin(String iconSkin) {
	this.iconSkin = iconSkin;
}
public String getIndex() {
	return index;
}
public void setIndex(String index) {
	this.index = index;
}
public List<Device> getChildren() {
	return children;
}
public void setChildren(List<Device> children) {
	this.children = children;
}
public Station() {
	super();
	// TODO Auto-generated constructor stub
}
public Station(String id, String name, String iconSkin, String index,
		List<Device> children) {
	super();
	this.id = id;
	this.name = name;
	this.iconSkin = iconSkin;
	this.index = index;
	this.children = children;
}

   

   
}
