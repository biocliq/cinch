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

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zitlab.palmyra.cinch.dbmeta.ForeignKey;
import com.zitlab.palmyra.cinch.dbmeta.TupleAttribute;
import com.zitlab.palmyra.cinch.dbmeta.TupleType;
import com.zitlab.palmyra.cinch.dbmeta.UniqueKey;
import com.zitlab.palmyra.cinch.pojo.Tuple;
import com.zitlab.palmyra.cinch.pojo.TupleFilter;
import com.zitlab.palmyra.cinch.tuple.dao.QueryParams;
import com.zitlab.palmyra.sqlbuilder.condition.ComboCondition;
import com.zitlab.palmyra.sqlbuilder.condition.ComboCondition.Op;
import com.zitlab.palmyra.sqlbuilder.condition.Operator;
import com.zitlab.palmyra.sqlbuilder.dialect.Dialect;
import com.zitlab.palmyra.sqlbuilder.query.SelectQuery;

class UniqueQueryHelper extends AppendColumnHelper {
	private static Logger logger = LoggerFactory.getLogger(UniqueQueryHelper.class);

	private SelectQueryHelper helper;

	private ForeignKey lastSelectedForeignKey;
	private Tuple lastSelectedParent;

	public UniqueQueryHelper(SelectQueryHelper helper) {
		this.helper = helper;
	}

	public QueryParams getSelectQueryByUQKey(Tuple item, TupleFilter filter, Dialect dialect) {

		TupleType tupleType = item.getTupleType();
		ArrayList<Object> valueList = new ArrayList<Object>();

		UniqueKey uniqueKeyList = null;
		String key = null;
		String preferredKey = item.getPreferredKey();
		String tableName = tupleType.getTable();
		String reference = tupleType.getName();
		Table rootTable = new Table(tupleType.getSchema(), tableName, reference);
		SelectQuery<Table> query = new SelectQuery<Table>(rootTable, reference, dialect);
		//Table rootTable = query.getPrimaryTable();

		// Search by OR condition for multiple Unique Keys
		ComboCondition condition = new ComboCondition(Op.OR);

		if (null != preferredKey) {
			uniqueKeyList = tupleType.getUniqueKey(preferredKey);
			if (null != uniqueKeyList)
				addByUniqueKey(preferredKey, uniqueKeyList, tupleType, item, rootTable, query, condition, valueList);
		}

		if (null == uniqueKeyList || 0 == condition.count()) {
			for (Entry<String, UniqueKey> entry : tupleType.getUniqueKeyMap().entrySet()) {
				key = entry.getKey();
				if (!key.equals("primaryKey")) {
					uniqueKeyList = entry.getValue();
					addByUniqueKey(key, uniqueKeyList, tupleType, item, rootTable, query, condition, valueList);
				}
			}

			// Add the primary key criteria
			Object id = item.getId();
			if (null != id) {
				DataList subValueList = new DataList();
				ComboCondition pCond = new ComboCondition(Op.AND);
				addPrimaryCriteria(pCond, subValueList, rootTable, Operator.EQUAL_TO,
						tupleType.getPrimaryKeyAttributes(), id);
				condition.addCondition(pCond);
				valueList.addAll(subValueList);
			}
		}

		helper.setAddlFilters(query, filter);

		if (0 == condition.count())
			return null;

		// Add the Columns to be selected
		appendColumnsToSelect(query, tupleType, rootTable, filter);
		query.addCondition(condition);
		String queryString = query.getQuery();

		QueryParams params = new QueryParams();
		params.setParams(valueList);
		params.setQuery(queryString);
		params.setTableLookup(query.getTableLookup());
		if (logger.isTraceEnabled())
			logger.trace("{}", params);

		return params;
	}

	private void addByUniqueKey(String key, UniqueKey uniqueKeyList, TupleType tupleType, Tuple item,
			Table rootTable, SelectQuery<Table> query, ComboCondition condition, ArrayList<Object> valueList) {

		// Within the Unique keys, search by AND for all the unique key columns
		ComboCondition subCondition = new ComboCondition(Op.AND);

		DataList subValueList = new DataList();
		String ds = null;
		Map<String, Object> attributes = item.getAttributes();
		int fkeyCount;

		// For each unique key combination
		for (Entry<String, TupleAttribute> uniqueKeyAttributes : uniqueKeyList.getColumns().entrySet()) {
			// Add the unique key attribute
			ds = uniqueKeyAttributes.getKey();
			logger.trace("Processing the key {} with the field {}", key, ds);
			TupleAttribute tupleAttribute = uniqueKeyAttributes.getValue();

			// If the unique key attribute is present in incoming object it will be added
			// here.
			Object value = attributes.get(ds);
			if (null != value) {
				addCriteria(subCondition, subValueList, rootTable, Operator.EQUAL_TO, tupleAttribute, value);
				continue;
			}

			fkeyCount = tupleAttribute.getForeignKeyCount();

			// If the unique key attribute is part of a single column foreignkey
			if (fkeyCount > 0) {

				Field pValue = getParentValueByAttribute(item, tupleType, tupleAttribute);
				if (null != pValue && null != pValue.value) {
					addCriteria(subCondition, subValueList, rootTable, Operator.EQUAL_TO, tupleAttribute, pValue.value);
					continue;
				}

				if (1 == fkeyCount) {
					boolean added = addSubTableUniqueCondition(ds, query, item, subCondition, subValueList, rootTable,
							ds);
					if (added)
						continue;
				}

			}
			if (!(tupleAttribute.isMandatory() || tupleAttribute.isAutoIncrement()) && uniqueKeyList.size() > 1) {
				addCriteria(subCondition, subValueList, rootTable, Operator.EQUAL_TO, tupleAttribute, null);
			} else
				break;
		}

		if (uniqueKeyList.size() == subCondition.count()) {
			condition.addCondition(subCondition);
			valueList.addAll(subValueList);
		}

	}

	protected boolean addSubTableUniqueCondition(String key, SelectQuery<Table> query, Tuple item, ComboCondition subCondition,
			DataList valueList, Table rootTable, String ctRef) {
		TupleType tupleType = item.getTupleType();
		Tuple refItem = item.getParent(key);
		ForeignKey fKey = item.getTupleType().getForeignKey(key);
		if (null == fKey || null == refItem)
			return false;

		TupleType subTableType = fKey.getTargetType();
		Object id = refItem.getId();
		Table subTable = getSubTable(query, subTableType, ctRef);//query.getSubTable(subTableType.getSchema(), subTableType.getTable(), ctRef, key);
//		if (fKey.isSingleColumn()) {
		if (null != id) {
			ComboCondition pCond = new ComboCondition(Op.AND);
			addPrimaryCriteria(pCond, valueList, subTable, Operator.EQUAL_TO, subTableType.getPrimaryKeyAttributes(),
					id);
			addInnerJoin(query, rootTable, tupleType, subTable, subTableType, fKey);
			subCondition.addCondition(pCond);
			return true;
		} else {
			// Add unique key condition;
			ctRef = ctRef + "." + key;
			ComboCondition subTableCondition = new ComboCondition(Op.AND);
			Object subValue;
			String attribName;

			// TODO this section shall be revisited based on the later requirements.
			for (String uqkey : subTableType.getUQKeyList()) {
				subTableCondition = new ComboCondition(Op.AND);
				UniqueKey subKeyMap = subTableType.getUniqueKey(uqkey);
				for (TupleAttribute subTableAttribute : subKeyMap.getColumns().values()) {
					attribName = subTableAttribute.getAttribute();
					subValue = refItem.getAttribute(attribName);
					if (null != subValue) {
						addCriteria(subTableCondition, valueList, subTable, Operator.EQUAL_TO, subTableAttribute,
								subValue);
					} else
						break;
				}

				if (subTableCondition.count() == subKeyMap.size()) {
					subCondition.addCondition(subTableCondition);
					addInnerJoin(query, rootTable, tupleType, subTable, subTableType, fKey);
					return true;
				}
			}
		}
//		}
		return false;
	}

	private Field getParentValueByAttribute(Tuple item, TupleType tupleType, TupleAttribute attribute) {
		String key;
		ForeignKey fKey = lastSelectedForeignKey;
		Tuple parent = lastSelectedParent;
		TupleAttribute target;

		if (null != parent) {
			target = fKey.getTargetBySrcAttribute(attribute);
			if (null != target) {
				Field result = new Field();
				result.attribute = target.getAttribute();
				result.value = parent.getAttribute(result.attribute);
				return result;
			}
		}

		for (Entry<String, Tuple> entry : item.getParent().entrySet()) {
			key = entry.getKey();
			parent = entry.getValue();
			fKey = tupleType.getForeignKey(key);
			target = fKey.getTargetBySrcAttribute(attribute);
			if (null != target) {
				lastSelectedForeignKey = fKey;
				lastSelectedParent = parent;
				Field result = new Field();
				result.attribute = target.getAttribute();
				result.value = parent.getAttribute(result.attribute);
				if (null == result.value && parent.hasAttribute(result.attribute))
					return result;
				return null;
			}
		}
		lastSelectedParent = null;
		lastSelectedForeignKey = null;
		return null;
	}

	class Field {
		String attribute;
		Object value;
	}
}
