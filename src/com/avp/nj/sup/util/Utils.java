package com.avp.nj.sup.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;

public class Utils {
	public static String json_encode(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			return null;
		}    
	}

	public static Object json_decode(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, Object.class);
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> tags_to_obj(String[] tagNames, List<?> valueList) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (tagNames == null || valueList == null
				|| tagNames.length != valueList.size()) {
			return map;
		}

		for (int i = 0; i < tagNames.length; i++) {
			String key = tagNames[i];
			String valueAll = (String) (valueList.get(i));
			if (valueAll == null) {
				continue;
			}
			String[] valueArr = valueAll.split(";");
			if (valueArr.length == 3) {
				String timestamp = valueArr[0];
				String value = valueArr[1];
				String ack = valueArr[2];
				Map<String, Object> valuedescall = null;

				if (Common.hashTagValueInfo.containsKey(key + value) == false) {
					valuedescall = new HashMap<String, Object>();
					valuedescall.put("valDesc", "");
					valuedescall.put("level", 0);
					valuedescall.put("levelDesc", "");
				} else {
					Object obj = Common.hashTagValueInfo.get(key + value);
					valuedescall = (Map<String, Object>) Utils.json_decode((String)obj);
				}

				HashMap<String, Object> info = new HashMap<String, Object>();
				info.put("tagname", Common.hashTagInfo.get(key));
				info.put("timestamp", timestamp);
				info.put("value", value);
				info.put("valDesc", valuedescall);
				info.put("ack", ack);
				map.put(key, info);
			} else {
				LogTracer.logError("JSON"+"Error in data parse:" + key + ":" + valueAll);
			}
		}
		
		return map;
	}
}
