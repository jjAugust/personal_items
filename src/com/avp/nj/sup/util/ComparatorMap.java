package com.avp.nj.sup.util;

import java.util.Comparator;
import java.util.HashMap;

public class ComparatorMap implements Comparator<HashMap<String, Object>>{

	private String sidx;
	private String sord;
	
	public ComparatorMap(String sidx,String sord) {
		super();
		this.sidx = sidx;
		this.sord = sord;
	}

	@Override
	public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
	    String field_one=o1.get(sidx)+"";
	    String field_two=o2.get(sidx)+"";
	    int compare_one;
	    int compare_two;
	    if(field_one.startsWith("0") || field_two.startsWith("0"))
	    {
	    	compare_one=Integer.parseInt(field_one.substring(1,field_one.length()));
	    	compare_two =Integer.parseInt(field_two.substring(1,field_one.length()));
	    }
	    else
	    {
	    	
	    	compare_one =Integer.parseInt( (String) o1.get(sidx));
			compare_two = Integer.parseInt((String) o2.get(sidx)) ;
	    }
		 
		int flag=0;
		if(compare_one>compare_two)
		{
			flag=1;
		}
		else if(compare_one<compare_two)
		{
			flag=-1;
		}
		if(sord.equals("desc"))
		{
			flag*=-1;
		}
		
		return flag;
	}

}

