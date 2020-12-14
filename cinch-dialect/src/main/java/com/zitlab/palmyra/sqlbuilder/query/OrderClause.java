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

import com.zitlab.palmyra.sqlbuilder.dialect.Dialect;

public class OrderClause extends SqlObject{
	public final boolean ASC = true;
	public final boolean DESC = false;
	String name;
	boolean asc;
	
	public OrderClause (String alias, boolean asc) {
		this.name = alias;
		this.asc = asc;
	}

	@Override
	public void append(StringBuilder sb) {
		sb.append(name);
		sb.append((asc) ? " ASC" : " DESC");
	}
	
	public void append(StringBuilder sb, Dialect dialect) {
		dialect.append(sb, this);
	}
}
