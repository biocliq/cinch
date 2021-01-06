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

public class Table<T extends Column> {
	public static final String TABLE_ALIAS = "$alias";
	private String schema;
	private String name;  // table name
	private String queryAlias;
	private String reference;
	
	private ArrayList<T> columns = new ArrayList<T>();
	
	public Table(String schema, String name, String reference) {
		this.name = name;
		this.schema = schema;
		this.reference = reference;
	}
	
	public final void addColumn(T column) {
		columns.add(column);
	}

	public final String getQueryAlias() {
		return queryAlias;
	}

	public StringBuilder appendName(StringBuilder sb) {
		if(null != schema) {
			sb.append(schema).append('.');
		}
		return sb.append(name).append(" ");		
	}
	
	public final String getName() {
		return name;
	}

	public final ArrayList<T> getColumns() {
		return columns;
	}

	public final int append(StringBuilder sb, int offset) {
		ArrayList<T> cols = columns;
		int size = cols.size();
		
		for (int index =0; index < size; index++) {
			T col = cols.get(index);
			col.setRsIndex(offset++);
			if (col.isColumn) {
				sb.append(queryAlias).append('.');
			}
			sb.append(col.name).append(' ');
			sb.append(queryAlias).append('_');
			sb.append(col.alias);
			sb.append(',');
		}
		return offset;
	}
	
	public final int append(StringBuilder sb, int offset, int limit) {
		ArrayList<T> cols = columns;
		int size = cols.size();
		
		limit = limit > size ? size : limit;
		
		for (int index =0; index < limit; index++) {
			T col = cols.get(index);
			col.setRsIndex(offset++);
			if (col.isColumn) {
				sb.append(queryAlias).append('.');
			}
			sb.append(col.name).append(' ');
//			sb.append(queryAlias).append('_');
			sb.append(col.alias);
			sb.append(',');
		}
		return offset;
	}

	public final String getSchema() {
		return schema;
	}

	public String getReference() {
		return reference;
	}

	public final void setSchema(String schema) {
		this.schema = schema;
	}
		
	public void setAlias(String _alias) {
		this.queryAlias = _alias;
	}
}
