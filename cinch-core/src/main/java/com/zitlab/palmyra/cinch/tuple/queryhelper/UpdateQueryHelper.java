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

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zitlab.palmyra.api2db.exception.FieldValidationException;
import com.zitlab.palmyra.api2db.exception.Validation;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleAttribute;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.cinch.api2db.audit.ChangeLogHelper;
import com.zitlab.palmyra.cinch.api2db.audit.ChangeLogger;
import com.zitlab.palmyra.cinch.tuple.dao.QueryParams;
import com.zitlab.palmyra.cinch.tuple.dao.TupleDao;
import com.zitlab.palmyra.sqlbuilder.dialect.Dialect;
import com.zitlab.palmyra.sqlbuilder.query.UpdateQuery;

public class UpdateQueryHelper extends QueryHelper {
	private static Logger logger = LoggerFactory.getLogger(UpdateQueryHelper.class);

	private Set<String> excludeFields;
	private ChangeLogger auditLogger;
	private boolean excludeCheckRequired = false;

	public UpdateQueryHelper(Set<String> excludeFields, ChangeLogger auditLogger) {
		this.excludeFields = excludeFields;
		this.auditLogger = auditLogger;
		this.excludeCheckRequired = (null != excludeFields );
	}

	public QueryParams getUpdateQueryByID(TupleType tupleType, Tuple item, Tuple dbItem, String currentUser,
			Dialect dialect, TupleDao dao) {

		if (null != item.getId()) {
			Table table = new Table(tupleType.getSchema(), tupleType.getTable(), tupleType.getName());
			UpdateQuery<Table> query = new UpdateQuery<Table>(table,tupleType.getName(),dialect);
			QueryParams result = setDataforUpdateQuery(tupleType, item, dbItem, query, currentUser, dialect, dao);
			return result;
		}
		return null;
	}

	private QueryParams setDataforUpdateQuery(TupleType tupleType, Tuple item, Tuple dbItem, UpdateQuery<Table> query,
			String currentUser, Dialect dialect, TupleDao dao) {
		ChangeLogHelper auditHelper = new ChangeLogHelper(dbItem, currentUser, new Date());

		PrimaryKeyHelper helper = new PrimaryKeyHelper(tupleType);

		DataList valueList = new DataList();
		Table table = query.getPrimaryTable();

		addAttributesForUpdate(tupleType, item, dbItem, query, dialect, dao, valueList, table, auditHelper);

		// References are already added in TupleService hence this is not required to be
		// executed.
		// addReferencesForUpdate(tupleType, item, dbItem, query, dialect, dao,
		// valueList, table, auditHelper);

		if (valueList.size() > 0) {
			TupleAttribute lastUpdBy = tupleType.getField("lastUpdBy");
			if (null != lastUpdBy) {
				table.addColumn(lastUpdBy);
				valueList.addString(currentUser);
			}
			TupleAttribute lastUpdOn = tupleType.getField("lastUpdOn");
			if (null != lastUpdOn) {
				table.addColumn(lastUpdOn);
				valueList.addDateTime(new java.sql.Timestamp(new Date().getTime()));
			}

			helper.addPrimaryKeyValues(item, query, valueList);

			String queryString = query.getQuery();

			if (null != queryString) {
				logger.debug("Generated query {}, {}", queryString, valueList);
				QueryParams params = new QueryParams();
				params.setQuery(queryString);
				params.setParams(valueList);
				return params;
			}
		}

		return null;
	}
	private void addAttributesForUpdate(TupleType tupleType, Tuple item, Tuple dbItem, UpdateQuery<Table> query,
			Dialect dialect, TupleDao dao, DataList valueList, Table table, ChangeLogHelper auditHelper) {
		Map<String, Object> attributes = item.getAttributes();

		String key;
		Object inValue;
		Object dbValue;
		TupleAttribute tupleAttribute;
		int dataType = 0;
		String fieldName = null;
		Map<String, TupleAttribute> fieldMap = tupleType.getAllFields();

		for (Entry<String, Object> entry : attributes.entrySet()) {
			key = entry.getKey();
			inValue = entry.getValue();
			dbValue = dbItem.getAttribute(key);
			tupleAttribute = fieldMap.get(key);
			if (null == tupleAttribute) {
				continue;
//				throw new FieldValidationException(key, tableName,
//						"Attribute `" + key + "` not mapped to any table in `" + tableName + "`",
//						Validation.INVALID_ATTRIBUTE);
			}

			dataType = tupleAttribute.getDataType();
			fieldName = tupleAttribute.getColumnName();

			if (null == inValue && tupleAttribute.isMandatory()) {
				throw new FieldValidationException(key, tupleType.getName(),
						"Mandatory parameter `" + key + "` is missing for the CI Type `" + tupleType.getName() + "`",
						Validation.MANDATORY);
			}

			if (valueList.isDifferent(tupleAttribute.getColumnName(), inValue, dbValue, tupleAttribute.getConverter())) {
				if (excludeCheckRequired)
					checkExclude(key, inValue, tupleType);
				table.addColumn(tupleAttribute);
				valueList.add(inValue, tupleAttribute);
				auditHelper.addLog(auditLogger, fieldName, dataType, inValue, dbValue);
			}
		}
	}

	private void checkExclude(String fieldName, Object value, TupleType type) {
		if (excludeFields.contains(fieldName)) {
			throw new FieldValidationException(fieldName, type.getName(),
					"Non Updatable field '" + fieldName + "' in the request with value '" + value + "'",
					Validation.NON_MODIFIABLE);
		}
	}
}
