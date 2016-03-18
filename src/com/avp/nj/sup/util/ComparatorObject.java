package com.avp.nj.sup.util;

import java.util.Comparator;

import com.avp.nj.sup.entity.Device;
import com.avp.nj.sup.entity.DeviceEventVO;
import com.avp.nj.sup.entity.DeviceStatusInfo;
import com.avp.nj.sup.entity.Station;

public class ComparatorObject implements Comparator<Object> {

	private String sidx;
	private String sord;

	public ComparatorObject(String sidx, String sord) {
		super();
		this.sidx = sidx;
		this.sord = sord;
	}

	@Override
	public int compare(Object o1, Object o2) {
		String field_one = "";
		String field_two = "";
		if (o1 instanceof Station) {
			Station station_01 = (Station) o1;
			Station station_02 = (Station) o2;
			if(sidx.equals("id"))
			{
				field_one = station_01.getId() + "";
				field_two = station_02.getId() + "";
			}
			else if(sidx.equals("index"))
			{
				field_one = station_01.getIndex() + "";
				field_two = station_02.getIndex() + "";
			}	
		
		}
		if(o1 instanceof Device)
		{
			Device device_01 = (Device) o1;
			Device device_02 = (Device) o2;
			field_one = device_01.getId() + "";
			field_two = device_02.getId() + "";
		}
		if(o1 instanceof DeviceEventVO)
		{
			if(sidx.equals("id"))
			{
				DeviceEventVO deviceEventVO_01 = (DeviceEventVO) o1;
				DeviceEventVO deviceEventVO_02 = (DeviceEventVO) o2;
				field_one = deviceEventVO_01.getId() + "";
				field_two = deviceEventVO_02.getId() + "";
			}
			else if(sidx.equals("time"))
			{
				DeviceEventVO deviceEventVO_01 = (DeviceEventVO) o1;
				DeviceEventVO deviceEventVO_02 = (DeviceEventVO) o2;
				field_one = deviceEventVO_01.getTime() + "";
				field_two = deviceEventVO_02.getTime() + "";
			}
			
		}
		if(o1 instanceof DeviceStatusInfo)
		{
			if(sidx.equals("time"))
			{
				DeviceStatusInfo DeviceStatusInfo_01 = (DeviceStatusInfo) o1;
				DeviceStatusInfo DeviceStatusInfo_02 = (DeviceStatusInfo) o2;
				field_one = DeviceStatusInfo_01.getTime() + "";
				field_two = DeviceStatusInfo_02.getTime() + "";
			}
		}
		if(o1 instanceof Integer){
			field_one = ((Integer)o1).toString();
			field_two = ((Integer)o2).toString();
		}
		
		int compare_one = Integer.parseInt((String) field_one);
		int compare_two = Integer.parseInt((String) field_two);
		int flag = 0;
		if (compare_one > compare_two) {
			flag = 1;
		} else if (compare_one < compare_two) {
			flag = -1;
		}
		if (sord.equals("desc")) {
			flag *= -1;
		}

		return flag;
	}

}
