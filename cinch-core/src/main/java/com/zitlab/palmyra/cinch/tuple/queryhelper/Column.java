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

import com.zitlab.palmyra.cinch.converter.Converter;
import com.zitlab.palmyra.cinch.dbmeta.TupleAttribute;

public class Column extends com.zitlab.palmyra.sqlbuilder.query.Column{
	private Converter<?> converter;

	private String attribute;

	public Column() {
		
	}
		
	public Column(String name, String alias, int dataType, Converter<?> converter) {
		super(name, alias, dataType);
		this.attribute = alias;
		this.converter = converter;
	}
	
	public Column(TupleAttribute attrib) {
		super(attrib.getColumnName(), attrib.getAttribute(), attrib.getDataType());
		this.attribute = attrib.getAttribute();
		this.converter = attrib.getConverter();
	}
	
	public Converter<?> getConverter() {
		return converter;
	}

	public void setConverter(Converter<?> converter) {
		this.converter = converter;
	}

	public final String getAttribute() {
		return attribute;
	}
}
