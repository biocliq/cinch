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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.zitlab.palmyra.cinch.pojo.FieldList;

public class FieldList {
	private List<String> attributes = new ArrayList<String>();
	private HashMap<String, FieldList> reference = new LinkedHashMap<String, FieldList>();

	public FieldList() {
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public int size() {
		return attributes.size();
	}

	public void setAttributes(List<String> attributes) {
		for (String att : attributes) {
			this.addField(att);
		}
	}

	public void addParentField(String refernce, String field) {
		FieldList fieldList = reference.get(refernce);
		if (null == fieldList) {
			fieldList = new FieldList();
			reference.put(refernce, fieldList);
		}
		fieldList.addParentField(field);
	}

	public void addField(String field) {
		if(!this.attributes.contains(field))
			this.attributes.add(field);
		
	}

	
	public HashMap<String, FieldList> getAllReferences() {
		return reference;
	}

	
	public void setReference(String key, FieldList list) {
		reference.put(key, list);		
	}

	
	public FieldList getReference(String key) {
		return reference.get(key);
	}

	
	public void addParentField(String field) {
		int index = field.indexOf('.');
		if (index < 0) {
			this.addField(field);
		} else {
			String ref = field.substring(0, index);
			String _field = field.substring(index + 1);
			FieldList fieldList = reference.get(ref);
			if (null == fieldList) {
				fieldList = new FieldList();
				reference.put(ref, fieldList);
			}
			fieldList.addParentField(_field);
		}		
	}
}
