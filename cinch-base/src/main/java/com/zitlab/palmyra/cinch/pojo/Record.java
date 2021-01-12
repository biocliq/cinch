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

import java.math.BigDecimal;
import java.util.Map;

import com.zitlab.palmyra.util.Converter;

public interface Record {

	public default Object get(String key) {
		return this.getAttribute(key);
	}

	public Object getAttribute(String key);

	public default BigDecimal getAttributeAsDecimal(String name) {
		return Converter.asDecimal(getAttribute(name));
	}

	public default Double getAttributeAsDouble(String name) {
		return Converter.asDouble(getAttribute(name));		
	}

	public default Float getAttributeAsFloat(String name) {
		return Converter.asFloat(getAttribute(name));
	}

	public default Integer getAttributeAsInt(String name) {
		return Converter.asInt(getAttribute(name));		
	}

	public default Long getAttributeAsLong(String name) {
		return Converter.asLong(getAttribute(name));
		
	}

	public default String getAttributeAsString(String name) {
		return Converter.asString(getAttribute(name));
		
	}

	public Map<String, Object> getAttributes();

	public String getError();

	public String getType();

	public boolean hasAttribute(String name);

	public void reassign(String src, String tgt);

	public Object removeAttribute(String key);

	public default void set(String key, Object value) {
		this.setAttribute(key, value);
	}

	public void setAttribute(String key, Object value);

	public default void setAttributeIfNotEmpty(String attribute, Number value) {
		if (null != value && (0 != value.longValue()))
			setAttribute(attribute, value);
	}

	public default void setAttributeIfNotEmpty(String attribute, Object value) {
		if (null != value)
			setAttribute(attribute, value);
	}

	public void setAttributes(Map<String, Object> attributes);

	public void setError(String error);

	public void setType(String type);

	public void setActionCode(Integer action);

	public Integer getActionCode();
	
	public default boolean forDelete() {
		return this.getActionCode() == Action.DELETE;
	}
	
	public default boolean forCreate() {
		return this.getActionCode() == Action.CREATE;
	}
	
	public default boolean forUpdate() {
		return this.getActionCode() == Action.UPDATE;
	}
}
