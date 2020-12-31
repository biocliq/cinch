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

import java.util.List;

/**
 * @author ksvraja
 *
 */

public abstract class SelectCriteria {
	private Tuple criteria;
	private FieldList fields;
	private FieldList orderBy ;
	private FieldList groupBy;
	private int offset = 0;
	private int limit = 15;
	private boolean total = false;

	private boolean includeReference = false;
	
	private String addlJoin;	

	public SelectCriteria() {
	}

	public Tuple getCriteria() {
		return criteria;
	}

	public void setCriteria(Tuple criteria) {
		this.criteria = criteria;
	}

	public FieldList getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(FieldList orderBy) {
		this.orderBy = orderBy;
	}

	public FieldList getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(FieldList groupBy) {
		this.groupBy = groupBy;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setStart(int start) {
		this.offset = start;
	}

	public void setEnd(int end) {
		this.setLimit(end - offset);
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public FieldList getFields() {
		return fields;
	}

	public void setFields(FieldList fields) {
		this.fields = fields;
	}

	public void setFieldsAsString(List<String> fieldList) {
		if (null == this.fields)
			this.fields = new FieldList();
		if (null != fieldList)
			this.fields.setAttributes(fieldList);
	}

	public boolean isTotal() {
		return total;
	}

	public void setTotal(boolean total) {
		this.total = total;
	}

	public abstract String getAddlCriteria();

	public abstract void setCriteria(String addlCriteria);
	
	public abstract void addCriteria(String addlCriteria);

	public String getAddlJoin() {
		return addlJoin;
	}

	public void setAddlJoin(String addlJoin) {
		this.addlJoin = addlJoin;
	}

	public void addOrderDesc(String field) {
		if(null == orderBy)
			orderBy = new FieldList();
		orderBy.getAttributes().add("-" + field);
	}

	public void addOrderAsc(String field) {
		if(null == orderBy)
			orderBy = new FieldList();
		orderBy.getAttributes().add("+" + field);
	}

	public boolean isIncludeReference() {
		return includeReference;
	}

	public void setIncludeReference(boolean includeReference) {
		this.includeReference = includeReference;
	}
}
