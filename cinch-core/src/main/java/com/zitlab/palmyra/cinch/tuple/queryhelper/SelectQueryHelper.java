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

import com.zitlab.palmyra.cinch.dbmeta.TupleType;
import com.zitlab.palmyra.cinch.pojo.QueryFilter;
import com.zitlab.palmyra.cinch.pojo.Tuple;
import com.zitlab.palmyra.cinch.schema.Schema;
import com.zitlab.palmyra.cinch.schema.SchemaFactory;
import com.zitlab.palmyra.cinch.tuple.dao.QueryParams;
import com.zitlab.palmyra.sqlbuilder.condition.ComboCondition;
import com.zitlab.palmyra.sqlbuilder.condition.CustomCondition;
import com.zitlab.palmyra.sqlbuilder.dialect.Dialect;
import com.zitlab.palmyra.sqlbuilder.query.SelectQuery;

public class SelectQueryHelper extends AppendColumnHelper {
	private SchemaFactory configFactory;

	public SelectQueryHelper(SchemaFactory configFactory ) {
		this.configFactory = configFactory;
	}
	
	public Schema getConfig() {
		return configFactory.getConfig();
	}
	
	public Dialect getDialect() {
		return configFactory.getConfig().getDialect();
	}

	public QueryParams getSelectQueryById(TupleType type, Object id, QueryFilter filter) {
		PrimaryKeyHelper helper = new PrimaryKeyHelper(type);
	//	String tableName = type.getTable();
		String reference = type.getName();
		Table rootTable = new Table(type.getSchema(),type.getTable(), type.getName(), reference);
		SelectQuery<Table> query = new SelectQuery<Table>(rootTable, reference, getDialect());
		//Table rootTable = query.getPrimaryTable();
		DataList list = new DataList();
		if (null != filter) {
			Tuple item = filter.getCriteria();

			if (null != item) {
				// TODO experminal added for inclusion of parent references
				addQueryCriteria(type.getName(), item, type, query, rootTable, list);
			}
		}
		setAddlFilters(query, filter);
		if (null != type.getCriteria()) {
			query.addCondition(new CustomCondition(type.getCriteria()));
		}
		appendColumnsToSelect(query, type, rootTable, filter);
		helper.addPrimaryKeyValues(id, query, list, rootTable);

		String queryString = query.getQuery();
		QueryParams params = new QueryParams(list);
		params.setQuery(queryString);
		if (logger.isTraceEnabled())
			logger.trace("{}", params);
		params.setTableLookup(query.getTableLookup());
		return params;
	}

	public QueryParams getSearchQuery(TupleType tupleType, QueryFilter filter) {

		ComboCondition conditions = filter.getConditions();
		DataList valueList = new DataList();
		Tuple item=filter.getCriteria();
		String tableName = tupleType.getTable();
		String reference = tupleType.getName();
		Table rootTable = new Table(tupleType.getSchema(),tableName, reference);
		SelectQuery<Table> query = new SelectQuery<Table>(rootTable, reference, getDialect());
		
		//valueList.addAll(filter.getDataList());
		addQueryCriteria(tupleType.getName(), conditions, tupleType, query, rootTable, valueList);
		setAddlFilters(query, filter);
		appendColumnsToSelect(query, tupleType, rootTable, filter);
		addOrderClause(filter, tupleType, query);
		
		String criteria = filter.getAddlCriteria();
		if (null != criteria)
			query.addCondition(CriteriaHelper.getCondition(criteria, query, tupleType));

		query.setLimit(filter.getLimit(), filter.getOffset());
		QueryParams params = new QueryParams(valueList);
		params.setTableLookup(query.getTableLookup());

		params.setQuery(query.getQuery());
		if (filter.isTotal()) {
			params.setCountQuery(query.getCountQuery(getDialect()));
		}

		if (logger.isTraceEnabled())
			logger.trace("{}", params);

		return params;
	}
	

	public void setAddlFilters(SelectQuery<Table> query, QueryFilter filter) {
		if (null == filter)
			return;
		String _join = filter.getAddlJoin();
		if (null != _join) {
			String join = AddlClauseHelper.convertJoinClause(_join, query, getConfig());
			if (null != join)
				query.setAddlJoin(join);
		}
	}

	public QueryParams getSelectQueryByUQKey(Tuple item, QueryFilter filter) {
		UniqueQueryHelper queryHelper = new UniqueQueryHelper(this);
		return queryHelper.getSelectQueryByUQKey(item, filter, getDialect());
	}
}
