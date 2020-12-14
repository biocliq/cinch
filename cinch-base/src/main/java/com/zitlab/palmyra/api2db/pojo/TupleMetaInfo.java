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
package com.zitlab.palmyra.api2db.pojo;

import java.util.Map;

public class TupleMetaInfo {
	
	private String type;
	private Integer action;
	private String label;
	private String error;

	private String labelFormat;

	private Map<String, Object> modAttributes;
		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	
	public String getLabelFormat() {
		return labelFormat;
	}
	public void setLabelFormat(String labelFormat) {
		this.labelFormat = labelFormat;
	}
	public Map<String, Object> getModAttributes() {
		return modAttributes;
	}
	public void setModAttributes(Map<String, Object> modAttributes) {
		this.modAttributes = modAttributes;
	}	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}	
	
	public boolean forCreate() {
		return null == this.action ? false : this.action == Action.CREATE;
	}

	public boolean forDelete() {
		return null == this.action ? false : this.action == Action.DELETE;
	}

	public boolean forUpdate() {
		return null == this.action ? false : this.action == Action.UPDATE;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
}
