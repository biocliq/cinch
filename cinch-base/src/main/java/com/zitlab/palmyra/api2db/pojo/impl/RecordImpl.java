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
package com.zitlab.palmyra.api2db.pojo.impl;

import java.util.HashMap;
import java.util.Map;

import com.zitlab.palmyra.api2db.pojo.Record;
import com.zitlab.palmyra.ds.ArrayMap;
import com.zitlab.palmyra.util.QueryTimer;

public abstract class RecordImpl implements Record{
	protected String type;

	protected String subType;

	protected Integer actionCode;

	protected String error;
		
	protected Map<String, Object> attributes;
	
	public RecordImpl() {
		this.attributes = new HashMap<String, Object>(32);
//		this.attributes = new ArrayMap<Object>(32);
	}

	public RecordImpl(int attrMapSize) {
//		this.attributes = new HashMap<String, Object>(attrMapSize);
		this.attributes = new ArrayMap<Object>(32);
	}
	
	public RecordImpl(String type) {
		this.type = type;
//		this.attributes = new HashMap<String, Object>(32);
		this.attributes = new ArrayMap<Object>(32);
	}

	public final String getType() {
		return type;
	}

	public final void setType(String type) {
		this.type = type;
	}
	
	public final Map<String, Object> getAttributes() {
		return attributes;
	}

	public final void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public void setAttribute(String key, Object value) {
		QueryTimer.start();
		attributes.put(key, value);
		QueryTimer.pause();
	}
	
	public final Object removeAttribute(String key) {
		return attributes.remove(key);
	}
	
	public void reassign(String src, String tgt) {
		Object obj = removeAttribute(src);
		setAttribute(tgt, obj);
	}

	
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}

	
	public final boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}

	
	public final String getSubType() {
		return subType;
	}

	
	public final void setSubType(String subType) {
		this.subType = subType;
	}

	
	public final String getError() {
		return error;
	}

	
	public final void setError(String error) {
		this.error = error;
	}	
}

