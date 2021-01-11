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

import java.util.List;

import com.zitlab.palmyra.sqlbuilder.condition.Condition;
import com.zitlab.palmyra.sqlbuilder.condition.SqlQueryException;
import com.zitlab.palmyra.sqlbuilder.dialect.Dialect;

public class UpdateQuery<T extends Table<? extends Column>> extends Query<T>{
	
	public UpdateQuery(T table,String reference, Dialect dialect) {
		super(table, reference, dialect);
	}
	
	@Override
	public String getQuery() {
		if(0 == table.getColumns().size())
			return null;
		Counter counter = new Counter(0);
		StringBuilder sb = StringBuilderCache.get();
		sb.append("UPDATE ");
		table.appendName(sb);
		sb.append(table.getQueryAlias());
		sb.append(" SET ");
		appendColumns(sb, table,counter);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" WHERE ");
		addConditions(sb);
		return StringBuilderCache.release(sb);
	}
	
	
	private void addConditions(StringBuilder sb) {		
		int len = conditions.size();
		if(0 == len) {
			throw new SqlQueryException("No where clause conditions provided. Update query will not be generated");
		}
		int count = 1;
		for (Condition condition : conditions) {
			condition.append(sb);
			if(count < len) {
				sb.append(" AND ");
			}
			count++;
		}
	}

	@Override
	protected void appendColumns(StringBuilder sb, T table, Counter counter) {		
		List<? extends Column> columns = table.getColumns();
		
		columns.forEach((column) -> {
			column.setRsIndex(counter.increment());
			sb.append(column.getName()).append("=?,");
		});
	}
}
