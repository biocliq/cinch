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
import java.util.Map;

import com.zitlab.palmyra.sqlbuilder.condition.Condition;
import com.zitlab.palmyra.sqlbuilder.dialect.Dialect;

public abstract class Query<T extends Table<? extends Column>>{
	protected T table;
	private Dialect dialect;
	protected ArrayList<Condition> conditions = new ArrayList<Condition>(4);
	
	private HashMap<String, T> tableLookup = new HashMap<String, T>();

	public Dialect getDialect() {
		return dialect;
	}
	
	public abstract String getQuery();

	public Query(T table,String reference, Dialect dialect) {
		String _alias = appendString("p", tableLookup.size());
		this.table = table;
		tableLookup.put(reference, this.table);
		table.setAlias(_alias);
		this.dialect = dialect;
	}

	public T getSubTable(String reference) {
		return tableLookup.get(reference);
	}
		
	public void addSubTable(T subTable, String reference) {
		String _alias = appendString("p", tableLookup.size());
		subTable.setAlias(_alias);
		tableLookup.put(reference, subTable);
	}
	
	public final boolean hasAlias(String ciReference) {
		return tableLookup.containsKey(ciReference);
	}

	public final T getPrimaryTable() {
		return this.table;
	}

	public final Map<String, T> getTableLookup() {
		return this.tableLookup;
	}

	protected int appendColumns(StringBuilder sb, T table, int offset) {
		return table.append(sb, offset);
	}
	
	protected int appendColumns(StringBuilder sb, T table, int offset, int limit) {
		return table.append(sb, offset, limit);
	}
		
	public void addCondition(Condition condition) {
		this.conditions.add(condition);
	}
	
	protected String appendString(Object ... values) {
		StringBuilder sb = StringBuilderCache.get();
		sb.setLength(0);
		int index = 0;
		int size = values.length;
		for (index = 0; index < size; index++) {
			sb.append(values[index]);
		}
		return StringBuilderCache.release(sb);
	}
}
