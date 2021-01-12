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

package com.zitlab.palmyra.cinch.pojo;

import java.util.ArrayList;
import java.util.List;

public class NativeQuery implements QueryOptions{
	protected String query;
	protected String countQuery;
	protected List<Object> params = new ArrayList<Object>();
	private int expectedResultSetSize = 100;
	private int limit;
	private int offset;
	
	public NativeQuery(List<Object> dataList) {
		params = dataList;
	}
	public NativeQuery() {
		
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public List<Object> getParams() {
		return params;
	}
	public void setParams(List<Object> params) {
		this.params = params;
	}
	public void addParams(Object param) {
		this.params.add(param);
	}
	
	public void addParams(Object... params) {
		for(Object param:params)
			this.params.add(param);
	}
	
	public void clearParam() {
		this.params.clear();
	}
	public String getCountQuery() {
		return countQuery;
	}
	public void setCountQuery(String countQuery) {
		this.countQuery = countQuery;
	}
	public void setExpectedResultSetSize(int expectedResultSetSize) {
		if(expectedResultSetSize > 0)
			this.expectedResultSetSize = expectedResultSetSize;
	}
	public int getExpectedResultSetSize() {
		return expectedResultSetSize;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
} 
