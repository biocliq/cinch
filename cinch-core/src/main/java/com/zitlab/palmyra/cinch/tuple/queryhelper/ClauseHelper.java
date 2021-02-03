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

import java.util.Map.Entry;

import com.zitlab.palmyra.cinch.dbmeta.TupleAttribute;
import com.zitlab.palmyra.cinch.dbmeta.TupleType;
import com.zitlab.palmyra.cinch.exception.DataValidationException;
import com.zitlab.palmyra.cinch.exception.FieldValidationException;
import com.zitlab.palmyra.cinch.exception.Validation;
import com.zitlab.palmyra.cinch.pojo.FieldList;
import com.zitlab.palmyra.cinch.pojo.QueryFilter;
import com.zitlab.palmyra.sqlbuilder.query.Query;
import com.zitlab.palmyra.sqlbuilder.query.SelectQuery;
import com.zitlab.palmyra.util.TextUtil;

public class ClauseHelper{

	protected final String getAlias(String attribute, TupleType root, Query<Table> query) {
		int index = attribute.indexOf('.');
		TupleAttribute attrib;
		if (index < 0) {
			attrib = root.getField(attribute);
			if (null != attrib)
				return query.getPrimaryTable().getQueryAlias() + "." + attrib.getColumnName();
			else
				throw new FieldValidationException(attribute, root.getName(),
						"Invalid attribute " + attribute + " for the ciType " + root.getName(),
						Validation.INVALID_ATTRIBUTE);
		} else {
			String ref = attribute.substring(0, index);
			String field = attribute.substring(index + 1);
			TupleType child = root.getForeignKeyTupleType(ref);
			if (null != child) {
				attrib = child.getField(field);
				if (null != attrib) {
					String ciReference = TextUtil.appendAll(root.getName(), ".", ref);
					String subTableAlias = getSubTable(query, child, ciReference) 
							//query.getSubTable(child.getSchema(), child.getTable(), ciReference, child.getName())
							.getQueryAlias();
					return TextUtil.appendAll(subTableAlias, ".", attrib.getColumnName());
				} else
					throw new FieldValidationException(attribute, root.getName(),
							"Invalid attribute " + attribute + " for the ciType " + root.getName(),
							Validation.INVALID_ATTRIBUTE);
			} else
				throw new FieldValidationException(attribute, root.getName(),
						"Invalid attribute " + attribute + " does not have foreign key relation",
						Validation.INVALID_ATTRIBUTE);
		}
	}

	protected final String getAlias(String ref, String attribute, TupleType root, Query<Table> query) {
		TupleAttribute attrib;
		TupleType child = root.getForeignKeyTupleType(ref);
		if (null != child) {
			attrib = child.getField(attribute);
			if (null != attrib) {
				String ciReference = TextUtil.appendAll(root.getName(), ".", ref);
				String subTableAlias = getSubTable(query, child, ciReference)
						//query.getSubTable(child.getSchema(), child.getTable(), ciReference, child.getName())
						.getQueryAlias();
				return TextUtil.appendAll(subTableAlias, ".", attrib.getColumnName());
			} else
				throw new FieldValidationException(attribute, root.getName(),
						"Invalid attribute " + attribute + " for the ciType " + root.getName(),
						Validation.INVALID_ATTRIBUTE);
		} else
			throw new DataValidationException(attribute,
					"Invalid attribute " + attribute + " does not have foreign key relation",
					Validation.INVALID_ATTRIBUTE_FOREIGNKEY);

	}

	public void addOrderClause(QueryFilter filter, TupleType tupleType, SelectQuery<Table> query) {
		FieldList orderMap = filter.getOrderBy();
		if (null != orderMap) {
			for (String orderClause : orderMap.getAttributes()) {
				String field = null;
				char prefix = orderClause.charAt(0);
				if ('+' == prefix || '-' == prefix) {
					field = orderClause.substring(1);
				} else {
					field = orderClause;
					prefix = '+';
				}

				String alias = getAlias(field, tupleType, query);
				query.addOrder(alias, '+' == prefix);
			}

			String key = null;
			FieldList fieldMap = null;

			for (Entry<String, FieldList> entry : orderMap.getAllReferences().entrySet()) {
				key = entry.getKey();
				fieldMap = entry.getValue();
				for (String orderClause : fieldMap.getAttributes()) {
					String field = null;
					char prefix = orderClause.charAt(0);
					if ('+' == prefix || '-' == prefix) {
						field = orderClause.substring(1);
					} else {
						field = orderClause;
						prefix = '+';
					}

					String alias = getAlias(key, field, tupleType, query);
					query.addOrder(alias, '+' == prefix);
				}
			}

		}
	}
	


	protected Table getSubTable(Query<Table> query, String schema, String table, String reference, String ciType) {		
		Table subTable = query.getSubTable(reference);
		if (null == subTable) {
			subTable = new Table(schema, table, ciType, reference);
			query.addSubTable(subTable, reference);
		}
		return subTable;
	}
	

	protected Table getSubTable(Query<Table> query, TupleType tupleType, String reference) {
		return getSubTable(query, tupleType.getSchema(), tupleType.getTable(), reference, tupleType.getName());
	}
}
