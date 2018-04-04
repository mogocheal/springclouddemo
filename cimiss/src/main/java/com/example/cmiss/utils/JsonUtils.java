package com.example.cmiss.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;

public class JsonUtils {

	// private static final SerializeConfig config;
	// static {
	// config = new SerializeConfig();
	// config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
	// }

	private static ValueFilter filter = new ValueFilter() {
		@Override
		public Object process(Object obj, String s, Object v) {
			if (v == null)
			return "";
			return v;
		}
	};

	// private static final SerializerFeature[] features = {
	// SerializerFeature.WriteMapNullValue,
	// SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
	// SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
	// SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
	// SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
	// };

	public static String toJson(Object obj) {
		return JSON.toJSONString(obj, filter);
	}

	public static Object toObject(String json) {
		return JSON.parse(json);
	}

}
