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

package com.zitlab.palmyra.cinch.tuple.dao;

import java.util.List;
import java.util.Map;

import com.zitlab.palmyra.cinch.pojo.NativeQuery;
import com.zitlab.palmyra.cinch.tuple.queryhelper.Table;
import com.zitlab.palmyra.util.StringBuilderCache;

public class QueryParams extends NativeQuery{
	private String countQuery;
	private Map<String, Table> tableLookup;
	
	public QueryParams(List<Object> dataList) {
		params = dataList;
	}
	public QueryParams() {
		
	}
	public String getCountQuery() {
		return countQuery;
	}
	public void setCountQuery(String countQuery) {
		this.countQuery = countQuery;
	}
	public Map<String, Table> getTableLookup() {
		return tableLookup;
	}
	public void setTableLookup(Map<String, Table> tableLookup) {
		this.tableLookup = tableLookup;
	}
	
	public String toString() {
		StringBuilder sb = StringBuilderCache.get();
		sb.append("query:").append(query);
		sb.append(" params: ").append(params);
		
		return StringBuilderCache.release(sb);
	}
} 
