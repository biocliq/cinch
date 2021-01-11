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
import com.zitlab.palmyra.sqlbuilder.dialect.Dialect;

public class InsertQuery<T extends Table<? extends Column>> extends Query<T>{

	public InsertQuery(T table,String reference, Dialect dialect) {
		super(table, reference, dialect);
	}
	
	@Override
	public String getQuery() {
		StringBuilder sb = StringBuilderCache.get();
		Counter counter = new Counter(0);
		sb.append("INSERT INTO ");
		table.appendName(sb);
		sb.append(" (");
		appendColumns(sb, table, counter);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") VALUES (");
		int length = table.getColumns().size();
		
		for(int i = 0 ; i < length; i++) {
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");		
		return StringBuilderCache.release(sb);		
	}

	@Override
	protected void appendColumns(StringBuilder sb, T table, Counter counter) {
		List<? extends Column> columns = table.getColumns();
		columns.forEach((column) -> {
			column.setRsIndex(counter.increment());
			sb.append(column.getName()).append(",");
		});
	}
	
	public void addCondition(Condition cond) {
		throw new UnsupportedOperationException("Insert Query is not supported with Conditions");
	}
}
