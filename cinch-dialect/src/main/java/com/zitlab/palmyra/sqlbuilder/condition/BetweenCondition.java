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
package com.zitlab.palmyra.sqlbuilder.condition;

import java.util.List;

import com.zitlab.palmyra.sqlbuilder.query.Column;
import com.zitlab.palmyra.sqlbuilder.query.Table;

public class BetweenCondition extends ColumnCondition{
	
	private Object start;
	private Object end; 
	private boolean _negate = false;

	public BetweenCondition(String attribute, Object start, Object end) {
		super(attribute);
		this.start = start;
		this.end = end;
	}
	
	public BetweenCondition(String attribute, boolean negate, Object start, Object end) {
		super(attribute);
		this._negate = negate;
		this.start = start;
		this.end = end;
	}
		
	public BetweenCondition(Table<? extends Column> table, String column, Object start, Object end) {
		super(null, table, column);
		this.start = start;
		this.end = end;
	}
	
	public BetweenCondition(Table<? extends Column> table, String column, boolean negate, Object start, Object end) {
		super(null, table, column);
		this.start = start;
		this.end = end;
	}
	
	@Override
	public void append(StringBuilder sb) {
		 sb.append(getColumn())
	      .append(_negate ? " NOT BETWEEN " : " BETWEEN ")
	      .append("? AND ?");
	}

	@Override
	public void appendValue(List<Object> valueList) {
		valueList.add(start);
		valueList.add(end);
	}	
}
