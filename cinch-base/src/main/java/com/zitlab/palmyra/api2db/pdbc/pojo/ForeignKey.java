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
package com.zitlab.palmyra.api2db.pdbc.pojo;

import java.util.ArrayList;
import java.util.List;

public class ForeignKey {
	private TupleType targetType;
	private String targetTable;
	private String targetSchema;
	private String alias;
	
	private List<TupleAttribute> source = new ArrayList<TupleAttribute>();
	private List<TupleAttribute> target = new ArrayList<TupleAttribute>();
	
	public TupleType getTargetType() {
		return targetType;
	}
	public void setTargetType(TupleType targetType) {
		this.targetType = targetType;
	}
	public String getTargetTable() {
		return targetTable;
	}
	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}
	public List<TupleAttribute> getSource() {
		return source;
	}
	public void setSource(List<TupleAttribute> source) {
		this.source = source;
	}
	public List<TupleAttribute> getTarget() {
		return target;
	}
	public void setTarget(List<TupleAttribute> target) {
		this.target = target;
	}
	
	public TupleAttribute remove(String columnName) {
		List<String> sourceCols = getSourceColumns();
		int index = sourceCols.indexOf(columnName);
		if(index >= 0) {
			target.remove(index);
			return source.remove(index);			
		}
		return null;
	}
	
	public TupleAttribute getTarget(String columnName) {
		List<String> sourceCols = getSourceColumns();
		int index = sourceCols.indexOf(columnName);
		if(index >= 0) {
			return target.get(index);
		}
		return null;
	}
	
	public List<String> getSourceColumns(){
		List<String> result = new ArrayList<String>();
		for(TupleAttribute attrib : source) {
			result.add(attrib.getColumnName());
		}
		return result;
	}
	
	public List<String> getTargetColumns(){
		List<String> result = new ArrayList<String>();
		for(TupleAttribute attrib : target) {
			result.add(attrib.getColumnName());
		}
		return result;
	}
	
	public List<String> getSourceAttributes(){
		List<String> result = new ArrayList<String>();
		for(TupleAttribute attrib : source) {
			result.add(attrib.getAttribute());
		}
		return result;
	}
	
	public List<String> getTargetAttributes(){
		List<String> result = new ArrayList<String>();
		for(TupleAttribute attrib : target) {
			result.add(attrib.getAttribute());
		}
		return result;
	}
	
	public boolean isExist(String colName, String tgtColName) {
		TupleAttribute tgt = getTarget(colName);
		if(null != tgt) {
			return tgt.getColumnName().equalsIgnoreCase(tgtColName);
		}
		return false;
	}
	public String getTargetSchema() {
		return targetSchema;
	}
	public void setTargetSchema(String targetSchema) {
		this.targetSchema = targetSchema;
	}
	
	public boolean isSingleColumn() {
		return source.size() == 1;
	}
	
	public boolean isMultiColumn() {
		return source.size() > 1;
	}
	
	public void addMapping(String srcTable, TupleAttribute src, TupleAttribute tgt) {
		this.source.add(src);
		this.target.add(tgt);
		src.setForeignKey(srcTable, this, tgt);
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	// This will work faster after the schema is populated from the database.
	public TupleAttribute getTargetBySrcAttribute(TupleAttribute attribute) {
		int index = source.indexOf(attribute);
		if(index > -1) {
			return target.get(index);
		}
		
		int size = source.size();
		TupleAttribute srcAttrib;
		for(index = 0; index < size; index++) {
			srcAttrib = source.get(index);
			if(srcAttrib.getAttribute().equals(attribute.getAttribute())) {
				return target.get(index);
			}
		}
		return null;
	}
}
