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
package com.zitlab.palmyra.sqlbuilder.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.zitlab.palmyra.sqlbuilder.condition.BinaryCondition;
import com.zitlab.palmyra.sqlbuilder.condition.ComboCondition;
import com.zitlab.palmyra.sqlbuilder.condition.ComboCondition.Op;
import com.zitlab.palmyra.sqlbuilder.condition.Condition;
import com.zitlab.palmyra.sqlbuilder.condition.JoinCondition;
import com.zitlab.palmyra.sqlbuilder.condition.JoinType;
import com.zitlab.palmyra.sqlbuilder.dialect.Dialect;

public class SelectQuery<T extends Table<? extends Column>> extends Query<T> {

	private HashMap<String, Join<T>> joinMap = new LinkedHashMap<String, Join<T>>(4);
	private ArrayList<String> groupBy;
	private ArrayList<OrderClause> orders = new ArrayList<OrderClause>();
	private LimitClause limitClause;

	private String addlCritera;
	private String addlJoin;

	public SelectQuery(T table, String reference, Dialect dialect) {
		super(table, reference, dialect);
	}

	private boolean addJoin(String key, T table, JoinType type, Condition condition) {
		Join<T> join = joinMap.get(key);
		if (null == join) {
			join = new Join<T>(table, type, condition);
			joinMap.put(key, join);
			return true;
		} else if (join.joinType != JoinType.INNER_JOIN) {
			join.joinType = type;
		}
		return false;
	}

	public void addOrder(String field, boolean asc) {
		OrderClause order = new OrderClause(field, asc);
		orders.add(order);
	}

	public void setLimit(int limit, int offset) {
		if (limit > 0) {
			if (offset > -1)
				limitClause = new LimitClause(limit, offset);
			else
				limitClause = new LimitClause(limit, 0);
		} else {
			limitClause = null;
		}

	}

	public void addGroupBy(String field) {
		if (null == groupBy)
			groupBy = new ArrayList<String>();
		groupBy.add(field);
	}

	@Override
	public String getQuery() {
		StringBuilder sb = StringBuilderCache.get();
		sb.append("SELECT ");
		appendColumns(sb);
		// sb.append(" FROM ").append(table.getNamewithSchema()).append(" AS
		// ").append(table.getQueryAlias());
		sb.append(" FROM ");
		table.appendName(sb).append(table.getQueryAlias());
		addJoins(sb);
		if (null != addlJoin) {
			sb.append(" ").append(addlJoin).append(" ");
		}
		if (conditions.size() > 0) {
			addConditions(sb);
		}
		if (null != addlCritera) {
			sb.append(" ").append(addlCritera).append(" ");
		}
		if (orders.size() > 0) {
			addOrdering(sb);
		}
		if (null != limitClause) {
			getDialect().append(sb, limitClause);
		}
		// TODO add group by conditions
		return StringBuilderCache.release(sb);
	}

	public String getCountQuery(Dialect dialect) {
		StringBuilder sb = StringBuilderCache.get();
		sb.append("SELECT COUNT(1) FROM (SELECT ");
		appendColumnforCountQuery(sb);
		// sb.append(" FROM ").append(table.getNamewithSchema()).append(" AS
		// ").append(table.getQueryAlias());
		sb.append(" FROM ");
		table.appendName(sb).append(table.getQueryAlias());
		addJoins(sb);
		if (null != addlJoin) {
			sb.append(" ").append(addlJoin).append(" ");
		}
		if (conditions.size() > 0) {
			addConditions(sb);
		}
		if (null != addlCritera) {
			sb.append(" ").append(addlCritera).append(" ");
		}
		// TODO add group by conditions
		sb.append(") fztmpyra");
		return StringBuilderCache.release(sb);
	}

	private void addJoins(StringBuilder sb) {
		T table = null;
		for (Join<T> join : this.joinMap.values()) {
			sb.append(" ").append(join.joinType).append(" ");
			table = join.table;
			table.appendName(sb).append(table.getQueryAlias()).append(" ON ");
			join.condition.append(sb, getDialect());
		}
	}

	private void addOrdering(StringBuilder sb) {
		sb.append(" ORDER BY ");
		List<OrderClause> ords = orders;

		int size = ords.size();

		int count = 1;
		OrderClause order;
		Dialect dialect = getDialect();
		for (int index = 0; index < size; index++) {
			order = ords.get(index);
			// if required change for dialect based ordering.
			dialect.append(sb, order);
			if (count < size) {
				sb.append(',');
			}
			count++;
		}

	}

	private void addConditions(StringBuilder sb) {
		sb.append(" WHERE ");
		List<Condition> conds = conditions;
		int count = 1;
		int size = conds.size();
		Condition condition;

		for (int index = 0; index < size; index++) {
			condition = conds.get(index);
			condition.append(sb, getDialect());
			if (count < size) {
				sb.append(" AND ");
			}
			count++;
		}
	}


	private void appendColumns(StringBuilder sb) {	
		Counter counter = new Counter(0);
		this.appendColumns(sb, this.table, counter);
		
		joinMap.forEach((key, join) -> {
			this.appendColumns(sb, join.table, counter);
		});
		sb.deleteCharAt(sb.length() - 1);
	}
	
	private void appendColumnforCountQuery(StringBuilder sb) {
		int rsIndex = 1;
		rsIndex = this.appendColumns(sb, this.table, rsIndex, 1);
		sb.deleteCharAt(sb.length() - 1);
	}

	public void addLeftOuterJoin(T root, String rootColumn, T subTable,
			String subTableColumn) {
		String left = appendString(root.getQueryAlias(), '.', rootColumn);
		String right = appendString(subTable.getQueryAlias(), '.', subTableColumn);
		String key = appendString(left + right);
		addJoin(key, subTable, JoinType.LEFT_OUTER_JOIN, BinaryCondition.equals(left, right));
	}

	public void addLeftOuterJoin(T root, List<String> rootColumns,
			T subTable, List<String> subTableColumns) {
		int colCount = rootColumns.size();
		if (colCount != subTableColumns.size())
			throw new RuntimeException("addLeftOuterJoin - The columns count does not match");
		String rootAlias = root.getQueryAlias();
		String subAlias = subTable.getQueryAlias();
		String key = appendString(rootAlias, subAlias);
		ComboCondition cond = new ComboCondition(Op.AND);
		if (addJoin(key, subTable, JoinType.LEFT_OUTER_JOIN, cond)) {
			String left, right;
			for (int i = 0; i < colCount; i++) {
				left = appendString(root.getQueryAlias(), '.', rootColumns.get(i));
				right = appendString(subTable.getQueryAlias(), '.', subTableColumns.get(i));
				cond.addCondition(BinaryCondition.equals(left, right));
			}
		}
	}

	public void addInnerJoin(T root, String rootColumn, T subTable, String subTableColumn) {
		String left = appendString(root.getQueryAlias(), '.', rootColumn);
		String right = appendString(subTable.getQueryAlias(), '.', subTableColumn);
		String key = appendString(left + right);
		addJoin(key, subTable, JoinType.INNER_JOIN, BinaryCondition.equals(left, right));
	}

	public void addInnerJoin(T root, List<String> rootColumns, T subTable, List<String> subTableColumns) {
		int colCount = rootColumns.size();
		if (colCount != subTableColumns.size())
			throw new RuntimeException("addLeftOuterJoin - The columns count does not match");
		String rootAlias = root.getQueryAlias();
		String subAlias = subTable.getQueryAlias();
		String key = appendString(rootAlias, subAlias);
		ComboCondition cond = new ComboCondition(Op.AND);

		String left, right;
		for (int i = 0; i < colCount; i++) {
			left = appendString(root.getQueryAlias(), '.', rootColumns.get(i));
			right = appendString(subTable.getQueryAlias(), '.', subTableColumns.get(i));
			cond.addCondition(new JoinCondition(left, right));
		}
		addJoin(key, subTable, JoinType.INNER_JOIN, cond);
	}

	public String getAddlCritera() {
		return addlCritera;
	}

	public void setAddlCritera(String addlCritera) {
		this.addlCritera = addlCritera;
	}

	public String getAddlJoin() {
		return addlJoin;
	}

	public void setAddlJoin(String addlJoin) {
		this.addlJoin = addlJoin;
	}

}
