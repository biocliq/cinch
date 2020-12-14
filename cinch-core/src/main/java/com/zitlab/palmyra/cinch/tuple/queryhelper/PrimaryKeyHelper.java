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

import java.util.List;
import java.util.Map;

import com.zitlab.palmyra.api2db.exception.FieldValidationException;
import com.zitlab.palmyra.api2db.exception.Validation;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleAttribute;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.sqlbuilder.condition.BinaryCondition;
import com.zitlab.palmyra.sqlbuilder.condition.Operator;
import com.zitlab.palmyra.sqlbuilder.query.Query;

public class PrimaryKeyHelper {
	TupleType type;

	public PrimaryKeyHelper(TupleType type) {
		this.type = type;
	}

	public void addPrimaryKeyCondition(Tuple tuple, Query<Table> query) {
		List<TupleAttribute> keys = type.getPrimaryKeyAttributes();

		if (1 == keys.size()) {
			TupleAttribute attrib = keys.get(0);
			query.addCondition(new BinaryCondition(Operator.EQUAL_TO, attrib.getColumnName()));
		} else {
			for (TupleAttribute attrib : keys) {
				query.addCondition(new BinaryCondition(Operator.EQUAL_TO, attrib.getColumnName()));
			}
		}
	}

//	public boolean addPrimaryKeyValues(Tuple tuple, Query query, QueryParams queryParams) {
//		List<TupleAttribute> keys = type.getPrimaryKeyAttribute();
//		if (null == keys)
//			return false;
//
//		if (1 == keys.size()) {
//			TupleAttribute attrib = keys.get(0);
//			query.addCondition(new BinaryCondition(Operator.EQUAL_TO, attrib.getColumnName()));
//			queryParams.addParams(tuple.getAttribute(attrib.getAttribute()), attrib);
//		} else {
//			for (TupleAttribute attrib : keys) {
//				query.addCondition(new BinaryCondition(Operator.EQUAL_TO, attrib.getColumnName()));
//				queryParams.addParams(tuple.getAttribute(attrib.getAttribute()), attrib);
//			}
//		}
//		return true;
//	}

	public boolean addPrimaryKeyValues(Tuple tuple, Query<Table> query, DataList queryParams) {
		List<TupleAttribute> keys = type.getPrimaryKeyAttributes();
		if (null == keys)
			return false;

		if (1 == keys.size()) {
			TupleAttribute attrib = keys.get(0);
			query.addCondition(new BinaryCondition(Operator.EQUAL_TO, attrib.getColumnName()));
			queryParams.add(tuple.getAttribute(attrib.getAttribute()), attrib);
		} else {
			for (TupleAttribute attrib : keys) {
				query.addCondition(new BinaryCondition(Operator.EQUAL_TO, attrib.getColumnName()));
				queryParams.add(tuple.getAttribute(attrib.getAttribute()), attrib);
			}
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	public void addPrimaryKeyValues(Object id, Query query, DataList queryParams, Table table) {
		List<TupleAttribute> keys = type.getPrimaryKeyAttributes();

		if (1 == keys.size()) {
			TupleAttribute attrib = keys.get(0);
			query.addCondition(new BinaryCondition(Operator.EQUAL_TO, table, attrib.getColumnName()));
			queryParams.add(id, attrib);
		} else {
			if (id instanceof Map) {
				Map attribs = (Map) id;
				for (TupleAttribute attrib : keys) {
					query.addCondition(new BinaryCondition(Operator.EQUAL_TO, table, attrib.getColumnName()));
					queryParams.add(attribs.get(attrib.getAttribute()), attrib);
				}
			} else {
				throw new FieldValidationException("primaryKey", type.getName(),
						"Unsupported id value for multiple-primary key of type-" + type.getName(),
						Validation.INVALID_ATTRIBUTE_PRIMARYKEY);
			}
		}
	}

	public static void removePrimaryKeyifNull(Tuple tuple, TupleType tupleType) {
		List<TupleAttribute> keys = tupleType.getPrimaryKeyAttributes();
		Object value;
		for (TupleAttribute key : keys) {
			value = tuple.getAttribute(key.getAttribute());
			if (null == value) {
				tuple.removeAttribute(key.getAttribute());
			}
		}
	}
}
