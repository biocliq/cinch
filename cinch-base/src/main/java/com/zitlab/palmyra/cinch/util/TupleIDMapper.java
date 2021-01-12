package com.zitlab.palmyra.cinch.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zitlab.palmyra.cinch.dbmeta.TupleAttribute;
import com.zitlab.palmyra.cinch.dbmeta.TupleType;
import com.zitlab.palmyra.cinch.pojo.Tuple;

public class TupleIDMapper {
	public static void setId(Tuple source, Tuple target, TupleType type) {
		List<TupleAttribute> attributes = type.getPrimaryKeyAttributes();
		String attribName;
		for (TupleAttribute attrib : attributes) {
			attribName = attrib.getAttribute();
			target.setAttribute(attribName, source.getAttribute(attribName));
		}
	}

	public static Object getId(Tuple tuple, TupleType type) {
		if (1 == type.getPrimaryKeyColumnCount()) {
			return tuple.getAttribute(type.getPrimaryKeyAttributes().get(0).getAttribute());
		} else {
			Map<String, Object> id = new HashMap<String, Object>();
			Object value;
			String key;
			List<TupleAttribute> attribs = type.getPrimaryKeyAttributes();
			int index = 0;
			int size = attribs.size();
			TupleAttribute attribute;
			
			//for (TupleAttribute attribute : type.getPrimaryKeyAttribute()) {
			for(index =0; index < size; index++) {
				attribute = attribs.get(index);
				key = attribute.getAttribute();
				value = tuple.getAttribute(key);
				if (null == value)
					return null;
				id.put(key, value);
			}
			if (id.size() > 0)
				return id;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static void setId(Tuple tuple, TupleType type, Object value) {
		if (1 == type.getPrimaryKeyColumnCount()) {
			tuple.setAttribute(type.getPrimaryKeyAttributes().get(0).getAttribute(), value);
		}else {
			Map<String, Object> id = (Map<String, Object>)value;
			String key;
			Object _val;
			List<TupleAttribute> attribs = type.getPrimaryKeyAttributes();
			int index = 0;
			int size = attribs.size();
			TupleAttribute attribute;
			
			//for (TupleAttribute attribute : type.getPrimaryKeyAttribute()) {
			for(index =0; index < size; index++) {
				attribute = attribs.get(index);
				key = attribute.getAttribute();
				_val = id.get(key);
				if(null != _val)
					tuple.setAttribute(key, _val);
				else {
					if(id.containsKey(key))
						tuple.setAttribute(key, null);
				}
			}
		}

	}

	public static void setId(Tuple tuple, TupleType type, Map<String, Object> value) {
		for (Entry<String, Object> entry : value.entrySet()) {
			tuple.setAttribute(entry.getKey(), entry.getValue());
		}
	}

	public static boolean equals(Tuple source, Tuple target, TupleType type) {
		if (null != source) {
			if (null != target) {

			} else
				return false;
		}
		return null == target;
	}

	public static boolean equalId(Object source, Object target, TupleType type) {
		if (type.isSinglePrimaryKey())
			return compare(source, target);
		else
			return compare(source, target, type);
	}

	@SuppressWarnings("unchecked")
	private static boolean compare(Object source, Object target, TupleType type) {
		Map<String, Object> src = (Map<String, Object>) source;
		Map<String, Object> tgt = (Map<String, Object>) target;
		String key;
		List<TupleAttribute> attribs = type.getPrimaryKeyAttributes();
		int index = 0;
		int size = attribs.size();
		TupleAttribute attribute;
		
		for(index =0; index < size; index++) {
			attribute = attribs.get(index);
			key = attribute.getAttribute();
			if (!compare(src.get(key), tgt.get(key)))
				return false;
		}
		return true;
	}

	private static boolean compare(Object source, Object target) {
		if (null != source) {
			if (null != target) {
				return source.equals(target);
			} else
				return false;
		}
		return null == target;
	}
}
