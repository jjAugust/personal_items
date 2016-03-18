package com.avp.nj.sup.util;

public class SupUtil {

	public static int getDeviceType(int id){
		String bcdcode = String.format("%08x", id);
		int typecode = Integer.parseInt(bcdcode.substring(0,2));
		typecode %= 17;
		return typecode;
	}
	
	public static int getStationSubId(int id){
		String bcdcode = String.format("%08x", id);
		int typecode = Integer.parseInt(bcdcode.substring(0,2));
		int stationcode = Integer.parseInt(bcdcode.substring(4,6));
		stationcode += 100*(typecode/17);
		return stationcode;
	}
	
	public static int getDeviceSubId(int id){
		String bcdcode = String.format("%08x", id);
		return Integer.parseInt(bcdcode.substring(6, 8));
	}
	
	public static int getLineId(int id){
		String bcdcode = String.format("%08x", id);
		return Integer.parseInt(bcdcode.substring(2,4));
	}
}
