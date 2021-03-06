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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zitlab.palmyra.cinch.dbmeta.ForeignKey;
import com.zitlab.palmyra.cinch.dbmeta.TupleAttribute;
import com.zitlab.palmyra.cinch.dbmeta.TupleType;
import com.zitlab.palmyra.cinch.exception.FieldValidationException;
import com.zitlab.palmyra.cinch.exception.Validation;
import com.zitlab.palmyra.cinch.pojo.FieldList;
import com.zitlab.palmyra.cinch.pojo.QueryFilter;
import com.zitlab.palmyra.sqlbuilder.condition.BinaryCondition;
import com.zitlab.palmyra.sqlbuilder.condition.ColumnCondition;
import com.zitlab.palmyra.sqlbuilder.condition.ComboCondition;
import com.zitlab.palmyra.sqlbuilder.condition.Condition;
import com.zitlab.palmyra.sqlbuilder.condition.NullCondition;
import com.zitlab.palmyra.sqlbuilder.condition.Operator;
import com.zitlab.palmyra.sqlbuilder.query.SelectQuery;
import com.zitlab.palmyra.util.TextUtil;

public class AppendColumnHelper extends QueryHelper {
//	private static final Logger logger = LoggerFactory.getLogger(AppendColumnHelper.class);

	protected void addQueryCriteria(String ref, ComboCondition condition, TupleType tupleType, SelectQuery<Table> query,
			Table table, DataList valueList) {
		if (null == condition.getConditions())
			return;

		Map<String, TupleAttribute> atts = tupleType.getAllFields();
		setColumn(ref, query, condition, atts, table, valueList, tupleType);
		query.addCondition(condition);
		// setParentColumn(ref,query,condition,atts,table,valueList,tupleType);

	}

	protected void setParentColumn(String ref, SelectQuery<Table> query, ColumnCondition condition,
			Map<String, TupleAttribute> atts, Table table, DataList valueList, TupleType tupleType) {
		TupleType refType = tupleType;
		String attrib = condition.getAttribute();
		String[] values = attrib.split("\\.");
		String relatedCi = null;
		Table subTable = null;
		for (int i = 0; i + 1 < values.length; i++) {
			ForeignKey fkey = refType.getForeignKeyByKeyName(values[i]);
			relatedCi = values[i + 1];
			String ctRef = TextUtil.appendAll(ref, ".", relatedCi);

			if (fkey != null) {
				refType = fkey.getTargetType();
				subTable = getSubTable(query, refType, ctRef);
				addInnerJoin(query, table, tupleType, subTable, refType, fkey);

			} else {
				throw new FieldValidationException(relatedCi, tupleType.getName(),
						"Parent reference " + relatedCi + " is not found for ci type " + tupleType.getName(),
						Validation.MISSING_REFERENCE);
			}

		}
		TupleAttribute column = refType.getField(relatedCi);
		if (null != column) {
			condition.setColumn(subTable, column.getColumnName());
			condition.appendValue(valueList);
		} else {
			throw new FieldValidationException(relatedCi, tupleType.getName(),
					"Parent reference " + relatedCi + " is not found for ci type " + tupleType.getName(),
					Validation.MISSING_FIELD);
		}
	}

	protected void setColumn(String ref, SelectQuery<Table> query, ComboCondition condition,
			Map<String, TupleAttribute> atts, Table table, DataList valueList, TupleType tupleType) {
		for (Condition cond : condition.getConditions()) {
			if (cond instanceof ColumnCondition) {
				ColumnCondition ccond = (ColumnCondition) cond;
				String attribute = ccond.getAttribute();
				TupleAttribute attrib = atts.get(attribute);

				if (null != attrib) {
					ccond.setColumn(table, attrib.getColumnName());
					ccond.appendValue(valueList);
				} else if (attribute.contains(".")) {
					setParentColumn(ref, query, ccond, atts, table, valueList, tupleType);
				} else {
					throw new FieldValidationException(attribute, tupleType.getName(),
							"Field reference " + attribute + " is not found for ci type " + tupleType.getName(),
							Validation.MISSING_FIELD);
				}
			} else if (cond instanceof ComboCondition) {
				setColumn(ref, query, (ComboCondition) cond, atts, table, valueList, tupleType);
			}

		}
	}
//	protected void addQueryCriteriaByField(ComboCondition condition, SelectQuery<Table> query, Table table,TupleType tupleType) {
//		 
//				Map<String, TupleAttribute> fields = tupleType.getAllFields();
//				for(Condition cond: condition.getConditions()) {
//					ColumnCondition ccond=(ColumnCondition)cond;
//					Object value=ccond.getColumn();
//					TupleAttribute field = fields.get(value);
//
//					if (null != field) {
//						if (!field.isFormula())
//							addCriteria(query, condition, table, field, value);
//				}
//	}
//	private void addCriteria(SelectQuery<Table> query, ComboCondition condition, Table table, TupleAttribute field,
//			Object value) {
//		// TODO Auto-generated method stub
//		
//	}

	/*
	 * protected void addQueryCriteriaByField(Tuple criteria, TupleType tupleType,
	 * SelectQuery<Table> query, Table table, DataList valueList) { Map<String,
	 * Object> attributes = criteria.getAttributes(); Map<String, TupleAttribute>
	 * fields = tupleType.getAllFields();
	 * 
	 * for (Entry<String, Object> attribute : attributes.entrySet()) { Object value
	 * = attribute.getValue(); TupleAttribute field =
	 * fields.get(attribute.getKey());
	 * 
	 * if (null != field) { if (!field.isFormula()) addCriteria(query,
	 * conditions,valueList, table, field, value);
	 * 
	 * else // TODO this should be handled after constructing the actual query
	 * logger.
	 * info("Formula column based where clause is currently not implemented: type:{}, attribute:{}"
	 * , tupleType.getName(), field.getAttribute()); } else { throw new
	 * FieldValidationException(attribute.getKey(), tupleType.getName(), "Field " +
	 * attribute.getKey() + " is not found the type " + tupleType.getName(),
	 * Validation.MISSING_FIELD); } } }
	 */
	protected final void addCriteria(String ref, SelectQuery<Table> query, DataList valueList, Table table,
			TupleAttribute attribute, Operator operator, Object value) {
		if (null == value) {
			query.addCondition(new NullCondition(table, attribute.getColumnName()));
		} else {
			query.addCondition(new BinaryCondition(operator, table, attribute.getColumnName(), value));
			// valueList.add(value, attribute);
		}
	}

	@SuppressWarnings("unchecked")
	protected final void addPrimaryCriteria(ComboCondition cond, DataList valueList, Table table, Operator operator,
			List<TupleAttribute> attributes, Object value) {
		if (1 == attributes.size()) {
			TupleAttribute attribute = attributes.get(0);
			if (null == value) {
				cond.addCondition(new NullCondition(table, attribute.getColumnName()));
			} else {
				cond.addCondition(new BinaryCondition(operator, table, attribute.getColumnName(), value));
				// valueList.add(value, attribute);
			}
		} else {
			Map<String, Object> valueMap = (Map<String, Object>) value;
			Object _val = null;
			int size = attributes.size();
			TupleAttribute attribute;

			for (int index = 0; index < size; index++) {
				attribute = attributes.get(index);
				_val = valueMap.get(attribute.getAttribute());
				if (null == _val) {
					cond.addCondition(new NullCondition(table, attribute.getColumnName()));
				} else {
					cond.addCondition(new BinaryCondition(operator, table, attribute.getColumnName(), value));
					// valueList.add(_val, attribute);
				}
			}
		}
	}

	protected final void addCriteria(ComboCondition cond, DataList valueList, Table table, Operator operator,
			TupleAttribute attribute, Object value) {

		if (null == value) {
			cond.addCondition(new NullCondition(table, attribute.getColumnName()));
		} else {
			cond.addCondition(new BinaryCondition(operator, table, attribute.getColumnName(), value));
			// valueList.add(value, attribute);
		}
	}

	protected final void addCriteria(SelectQuery<Table> query, DataList valueList, Table table,
			TupleAttribute attribute, Object value) {
		query.addCondition(getCondition(table, attribute, value, valueList));

	}

	protected void appendColumnsToSelect(SelectQuery<Table> query, TupleType tupleType, Table rootTable,
			QueryFilter filter) {
		List<String> fields = null;
		FieldList fieldList = null;
		HashMap<String, FieldList> referenceMap = null;
		boolean includeReference = (null != filter && filter.isIncludeReference());
		if (null != filter) {
			fieldList = filter.getFields();
			if (null != fieldList) {
				fields = fieldList.getAttributes();
				if (null != fields && 0 < fields.size()) {
					appendColumns(query, tupleType, rootTable, fieldList, tupleType.getName());
					return;
				}
				referenceMap = fieldList.getAllReferences();
				if (null != referenceMap && 0 < referenceMap.size()) {
					appendColumns(query, tupleType, rootTable, fieldList, tupleType.getName());
					return;
				}
			}
		}

		appendAllColumns(tupleType.getName(), query, tupleType, rootTable, includeReference);
	}

	private void appendAllColumns(String ref, SelectQuery<Table> query, TupleType tupleType, Table rootTable,
			boolean includeReference) {
		TupleType subType = null;
		Table subTable = null;
		String alias = null;
		StringBuilder sb = new StringBuilder(32);
		ForeignKey fKey = null;
		TupleAttribute field;
		TupleAttribute subField;
		for (Entry<String, TupleAttribute> entry : tupleType.getAllFields().entrySet()) {
			field = entry.getValue();
			rootTable.addColumn(field);
		}
		if (includeReference) {
			for (Entry<String, ForeignKey> entry : tupleType.getForeignKeyMap().entrySet()) {
				sb.setLength(0);
				TextUtil.appendAll(sb, ref, ".", entry.getKey());
				alias = sb.toString();

				fKey = entry.getValue();

				subType = fKey.getTargetType();
				subTable = getSubTable(query, tupleType, alias);
				// query.getSubTable(subType.getSchema(), subType.getTable(), alias,
				// subType.getName());

				for (Entry<String, TupleAttribute> subEntry : subType.getAllFields().entrySet()) {
					subField = subEntry.getValue();
					subTable.addColumn(subField);
				}
				addLeftOuterJoin(query, rootTable, tupleType, subTable, subType, fKey);

			}
		}
	}

	private void appendColumns(SelectQuery<Table> query, TupleType tupleType, Table rootTable, FieldList fieldList,
			String reference) {
		List<String> fields = fieldList.getAttributes();
		Map<String, FieldList> referenceMap = fieldList.getAllReferences();
		Map<String, TupleAttribute> tupleAttrMap = tupleType.getAllFields();
		ForeignKey fKey;
		String key;
		StringBuilder sb = new StringBuilder(32);
		TupleType subType = null;
		TupleAttribute attribute = null;
		Table subTable = null;

		if (null != fields) {
			for (String fieldName : fields) {
				attribute = tupleAttrMap.get(fieldName);
				if (null != attribute) {
					rootTable.addColumn(attribute);
				} else
					throw new FieldValidationException(fieldName, tupleType.getName(),
							"Attribute " + fieldName + " is not found in the type " + tupleType.getName(),
							Validation.MISSING_FIELD);
			}
		}
		if (null != referenceMap) {
			for (Entry<String, FieldList> entry : referenceMap.entrySet()) {
				key = entry.getKey();
				fKey = tupleType.getForeignKey(key);
				if (null != fKey) {
					subType = fKey.getTargetType();
					sb.setLength(0);
					TextUtil.appendAll(sb, reference, ".", key);
					String nextRef = sb.toString();
					subTable = getSubTable(query, subType, nextRef);
					// query.getSubTable(subType.getSchema(), subType.getTable(), nextRef,
					// subType.getName());
					FieldList list = entry.getValue();
					addLeftOuterJoin(query, rootTable, tupleType, subTable, subType, fKey);
					if (list.size() > 0) {
						appendColumns(query, subType, subTable, list, nextRef);
					} else {
						appendAllColumns(nextRef, query, subType, subTable, false);
					}
				} else
					throw new FieldValidationException(key, tupleType.getName(),
							"Parent reference " + key + " is not found in the type " + tupleType.getName(),
							Validation.MISSING_REFERENCE);
			}
		}
	}
}
