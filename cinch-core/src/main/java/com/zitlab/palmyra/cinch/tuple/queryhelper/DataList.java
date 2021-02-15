/*******************************************************************************
 * Copyright 2020 BioCliq Technologies
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.zitlab.palmyra.cinch.tuple.queryhelper;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.zitlab.palmyra.cinch.converter.Converter;
import com.zitlab.palmyra.cinch.dbmeta.TupleAttribute;

public class DataList extends ArrayList<Object> {
	private static final long serialVersionUID = 1L;
	public boolean addAll(ArrayList<String> valueList) {
		return super.addAll(valueList);
	}
//	public boolean add(Object value) {
//		throw new UnsupportedOperationException("This method should not be used");
//	}

	public boolean addString(String value) {
		return super.add(value);
	}

	public boolean add(Object value, TupleAttribute attrib) {
		return super.add(getValid(value, attrib));
	}
	
	public boolean addLikeValue(String value) {	
		return super.add(value);
	}

	public Object getValid(Object value, TupleAttribute attrib) {
		if (null == value) {
			return null;
		}
		return attrib.getConverter().convert(value);
	}

	public boolean isDifferent(String column, Object src, Object target, Converter<?> converter) {
		if (null != src && null != target) {
			if (src.equals(target))
				return false;
			else {
				Object _src = converter.convert(src);
				Object _tgt = converter.convert(target);
				/*
				 * Valid values may return as NULL  for empty string  hence do the null check again.
				 */
				if (null != _src) {
					return null == _tgt ?
							true : 
								!_src.equals(_tgt);
				}else {
					return null != _tgt;
				}
			}
		} else if (null == src) {
			return null != target;
		}
		return true;
	}

	public void addDate(java.sql.Date date) {
		super.add(date);
	}

	public void addDateTime(Timestamp timestamp) {
		super.add(timestamp);
	}
	
	public static String getType(int dataType) {
		switch (dataType) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGNVARCHAR:
			return "string";

		case Types.DECIMAL:
			return "decimal";
		case Types.FLOAT:
			return "float";
		case Types.INTEGER:
		case Types.NUMERIC:
			return "int";
		case Types.BIGINT:
			return "long";
		case Types.BIT:
			return "bit";
		case Types.BOOLEAN:
			return "boolean";
		case Types.TIMESTAMP:
			return "timestamp";
		case Types.TIMESTAMP_WITH_TIMEZONE:
			return "timestampz";
		case Types.TIME:
			return "time";
		case Types.DATE:
			return "date";
		default:
			return "string";
		}
	}
}
