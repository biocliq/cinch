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
package com.zitlab.palmyra.sqlbuilder.query;

/**
 * @author ksvraja
 *
 */
public class Column {
	String name;
	String alias;
	boolean isColumn = true;
	private int dataType;
	private int rsIndex;
	
	public Column() {
		
	}
	
	public Column(String name, int dataType) {
		this.name = name;
		this.alias = name;
		this.dataType = dataType;
	}
	
	public Column(String name, String alias, int dataType) {
		this.name = name;
		this.alias = alias;
		this.dataType = dataType;
	}
	
	public final String getName() {
		return name;
	}
	public final String getAlias() {
		return alias;
	}
	public final boolean isColumn() {
		return isColumn;
	}
	public final int getDataType() {
		return dataType;
	}	
	public final int getRsIndex() {
		return rsIndex;
	}
	public final void setRsIndex(int rsIndex) {
		this.rsIndex = rsIndex;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}	
}
