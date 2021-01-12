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

package com.zitlab.palmyra.cinch.core.audit;

import java.util.Date;

public class ChangeLog {
	private long id;
	private Object parentId;
	private String parentCit;
	private String cit;
	private Object ciId;
	private String fieldName;
	private String oldValue;
	private String newValue;
	private int dataType;
	private Date createdOn;
	private String createdBy;
	
	public ChangeLog(){
		
	}
	
	public ChangeLog(String cit, Object ciid,  String parentCit, Object parentId, String user, Date createdOn){
		this.cit = cit;
		this.ciId = ciid;
		this.parentCit = parentCit;
		this.parentId = parentId;
		this.createdBy = user;
		this.createdOn = createdOn;		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Object getParentId() {
		return parentId;
	}
	public void setParentId(Object parentId) {
		this.parentId = parentId;
	}
	
	public String getParentCit() {
		return parentCit;
	}

	public void setParentCit(String parentCit) {
		this.parentCit = parentCit;
	}

	public String getCit() {
		return cit;
	}

	public void setCit(String cit) {
		this.cit = cit;
	}

	public Object getCiId() {
		return ciId;
	}
	public void setCiId(Object ciId) {
		this.ciId = ciId;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}	
}
