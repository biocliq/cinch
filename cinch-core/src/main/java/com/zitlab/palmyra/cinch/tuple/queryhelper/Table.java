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
package com.zitlab.palmyra.cinch.tuple.queryhelper;

import com.zitlab.palmyra.api2db.pdbc.pojo.TupleAttribute;

public class Table extends com.zitlab.palmyra.sqlbuilder.query.Table<Column>{

	private String ciType;
	
	private String attReference;
	
	Table(String schema, String tableName, String ciType, String reference) {
		super(schema, tableName, reference);
		this.ciType = ciType;
		attReference = reference.substring(reference.indexOf(".") + 1, reference.length());
	}
	
	Table(String schema, String tableName, String ciType) {
		super(schema, tableName, ciType);
		this.ciType = ciType;
		//this.attReference = ciType;
	}

	protected Column newColumn() {
		return new Column();
	}
	
	public void addColumn(TupleAttribute attrib) {
		Column column = new Column(attrib);
		addColumn(column);
	}

	public String getCiType() {
		return ciType;
	}
	
	public String getAttributeReference() {
		return attReference;
	}
}
