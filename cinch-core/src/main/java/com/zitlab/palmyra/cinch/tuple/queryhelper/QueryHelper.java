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

import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zitlab.palmyra.api2db.pdbc.pojo.TupleAttribute;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.sqlbuilder.condition.BetweenCondition;
import com.zitlab.palmyra.sqlbuilder.condition.BinaryCondition;
import com.zitlab.palmyra.sqlbuilder.condition.ComboCondition;
import com.zitlab.palmyra.sqlbuilder.condition.ComboCondition.Op;
import com.zitlab.palmyra.sqlbuilder.condition.Condition;
import com.zitlab.palmyra.sqlbuilder.condition.Operator;

public abstract class QueryHelper extends JoinHelper {

	protected static Logger logger = LoggerFactory.getLogger(QueryHelper.class);

	protected static Condition getCondition(Table table, TupleAttribute attribute, Object value, DataList valueList) {
		if (null == value)
			return BinaryCondition.isNull(table, attribute.getColumnName());

		String sVal = null;
		if (value instanceof String)
			sVal = (String) value;
		else
			sVal = value.toString();

		int len = sVal.length();
		if (0 == len || 1 == len) {
			valueList.add(value, attribute);
			return BinaryCondition.equals(table, attribute.getColumnName());
		}
		if (sVal.contains("|")) {
			String[] values = sVal.split("\\|");
			ComboCondition cnd = new ComboCondition(Op.OR);
			for (String val : values) {
				cnd.addCondition(getSingleCondition(table, attribute, val, valueList));
			}
			return cnd;
		} else
			return getSingleCondition(table, attribute, sVal, valueList);
	}

	private static Condition getSingleCondition(Table table, TupleAttribute attribute, String value,
			DataList valueList) {
		String sVal = value;
		int dataType = attribute.getDataType();
		int len = sVal.length();
		if (0 == len || 1 == len) {
			valueList.add(value, attribute);
			return BinaryCondition.equals(table, attribute.getColumnName());
		}

		char first = sVal.charAt(0);
		char second = sVal.charAt(1);

		switch (dataType) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
		case Types.CLOB:
			return getStringCondition(table, attribute, sVal, valueList, first, second);
		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
		case Types.NUMERIC:
		case Types.BIGINT:
		case Types.DOUBLE:
		case Types.REAL:
		case Types.FLOAT:
		case Types.DECIMAL:
			return getNumericCondition(table, attribute, sVal, valueList, first, second);
		case Types.DATE:
		case Types.TIME:
		case Types.TIMESTAMP:
			return getTimestampCondition(table, attribute, sVal, valueList, first, second, dataType);
		}

		valueList.add(value, attribute);
		return new BinaryCondition(Operator.EQUAL_TO, table, attribute.getColumnName());
	}

	/**
	 * @param table
	 * @param column
	 * @param sVal
	 * @param valueList
	 * @return
	 */
	private static Condition getBetweenCondition(Table table, TupleAttribute attribute, String sVal,
			DataList valueList) {
		int idx = sVal.indexOf("...");
		String start = sVal.substring(0, idx);
		String end = sVal.substring(idx + 3, sVal.length());
		valueList.add(start, attribute);
		valueList.add(end, attribute);
		return new BetweenCondition(table, attribute.getColumnName());
	}

	private static Condition getLikeCondition(Table table, TupleAttribute attribute, String sVal, DataList valueList) {
		valueList.addLikeValue(convert(sVal));
		return new BinaryCondition(Operator.LIKE, table, attribute.getColumnName());
	}
	
	private static Condition getNotLikeCondition(Table table, TupleAttribute attribute, String sVal,
			DataList valueList) {
		valueList.addLikeValue(convert(sVal));
		return new BinaryCondition(Operator.NOT_LIKE, table, attribute.getColumnName());
	}

	private static String convert(String sVal) {
		String value = null;
		boolean multiMatch = sVal.indexOf('*') > -1;
		boolean singleMatch = sVal.indexOf('?') > -1;
		
		if (multiMatch || singleMatch) {
			if (multiMatch)
				value = replaceAll(sVal, '*', '%');
			if (singleMatch)
				value = replaceAll(sVal, '?', '_');
		}
		
		return value;
	}
	
	private static Condition getNumericCondition(Table table, TupleAttribute attribute, String sVal, DataList valueList,
			char first, char second) {
		boolean equal = '=' == second;
		Operator criteria;
		int index = 1;

		switch (first) {
		case '=':
			criteria = Operator.EQUAL_TO;
			index = 0;
			break;
		case '>':
			if (equal) {
				index = 2;
				criteria = Operator.GREATER_THAN_OR_EQUAL_TO;
			} else
				criteria = Operator.GREATER_THAN;
			break;
		case '<':
			if (equal) {
				index = 2;
				criteria = Operator.LESS_THAN_OR_EQUAL_TO;
			} else
				criteria = Operator.LESS_THAN;
			break;
		case '!':
			if (equal) {
				index = 2;
				if (sVal.contains("*"))
					return getNotLikeCondition(table, attribute, sVal.substring(2), valueList);
			}
			criteria = Operator.NOT_EQUAL_TO;
			break;
		default:
			if (sVal.contains("...")) {
				return getBetweenCondition(table, attribute, sVal, valueList);
			}
			if (sVal.contains("*")) {
				return getLikeCondition(table, attribute, sVal, valueList);
			}
			criteria = Operator.EQUAL_TO;
			index = 0;
		}

		if (0 != index) {
			sVal = sVal.substring(index);
		}
		valueList.add(sVal, attribute);
		return new BinaryCondition(criteria, table, attribute.getColumnName());

	}

	private static Condition getStringCondition(Table table, TupleAttribute attribute, String sVal, DataList valueList,
			char first, char second) {
		boolean equal = '=' == second;
		boolean multiMatch = sVal.indexOf('*') > -1;
		boolean singleMatch = sVal.indexOf('?') > -1;

		String value = null;
		Operator criteria;
		int index = 1;
		boolean nonNegate = true;
		if ('!' != first) {
			index = 0;
		} else {
			if (equal)
				index = 2;
			nonNegate = false;
			sVal = sVal.substring(index);
		}

		if (multiMatch || singleMatch) {
			if (multiMatch)
				value = replaceAll(sVal, '*', '%');
			if (singleMatch)
				value = replaceAll(sVal, '?', '_');

			valueList.addString(value);
			criteria = (nonNegate) ? Operator.LIKE : Operator.NOT_LIKE;
		} else {
			valueList.addString(sVal);
			criteria = (nonNegate) ? Operator.EQUAL_TO : Operator.NOT_EQUAL_TO;
		}
		return new BinaryCondition(criteria, table, attribute.getColumnName());
	}

	public static String replaceAll(String oldVal, char oldChar, char newChar) {
		char[] value = oldVal.toCharArray();
		if (oldChar != newChar) {
			int len = value.length;
			int i = -1;
			char[] val = value; /* avoid getfield opcode */
			char buf[] = new char[len];

			while (++i < len) {
				if (val[i] == oldChar) {
					buf[i] = newChar;
				} else
					buf[i] = val[i];
			}
			return new String(buf);
		}
		return oldVal;
	}

	private static Condition getTimestampCondition(Table table, TupleAttribute attribute, String sVal,
			DataList valueList, char first, char second, int dataType) {
		boolean equal = '=' == second;
		Operator criteria;
		int index = 1;

		switch (first) {
		case '=':
			criteria = Operator.EQUAL_TO;
			index = 0;
			break;
		case '>':
			if (equal) {
				index = 2;
				criteria = Operator.GREATER_THAN_OR_EQUAL_TO;
			} else
				criteria = Operator.GREATER_THAN;
			break;
		case '<':
			if (equal) {
				index = 2;
				criteria = Operator.LESS_THAN_OR_EQUAL_TO;
			} else
				criteria = Operator.LESS_THAN;
			break;
		case '!':
			if (equal)
				index = 2;
			criteria = Operator.NOT_EQUAL_TO;
			break;
		default:
			if (sVal.contains("...")) {
				return getBetweenCondition(table, attribute, sVal, valueList);
			}
			criteria = Operator.EQUAL_TO;
			index = 0;
		}

		if (0 != index) {
			sVal = sVal.substring(index);
		}
		valueList.add(sVal, attribute);
		return new BinaryCondition(criteria, table, attribute.getColumnName());
	}

	protected Object getRefField(Tuple refItem, String refField) {
		Object inValue = refItem.getAttribute(refField);
		if (null == inValue) {
			if (!refItem.hasAttribute(refField)) {
				Tuple dbRefItem = refItem.getDbTuple();
				if (null != dbRefItem)
					inValue = dbRefItem.getAttribute(refField);
			}
		}
		return inValue;
	}
}
