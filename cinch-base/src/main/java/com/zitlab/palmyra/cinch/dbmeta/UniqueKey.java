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
package com.zitlab.palmyra.cinch.dbmeta;

import java.util.HashMap;
import java.util.Map;

public class UniqueKey {
	private Map<String, TupleAttribute> columns = new HashMap<String, TupleAttribute>();

	public Map<String, TupleAttribute> getColumns() {
		return columns;
	}

	public void addAll(Map<String, TupleAttribute> columns) {
		this.columns.putAll(columns);
	}
	
	public void add(String key, TupleAttribute attribute) {
		this.columns.put(key, attribute);
	}
	
	public int size() {
		return columns.size();
	}
	
	public boolean containsKey(String key) {
		return columns.containsKey(key);
	}

	public TupleAttribute get(String field) {
		return columns.get(field);
	}
}
