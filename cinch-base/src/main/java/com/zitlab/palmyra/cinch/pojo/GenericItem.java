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

import java.util.LinkedHashMap;
import java.util.Map;

public class GenericItem {
	private Map<String, Object> attributes = new LinkedHashMap<String, Object>();
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Map<String, Object> getAttributes() {
		return this.attributes;
	}
	
	public void setAttribute(String name, Object value) {
		this.attributes.put(name, value);
	}
}
