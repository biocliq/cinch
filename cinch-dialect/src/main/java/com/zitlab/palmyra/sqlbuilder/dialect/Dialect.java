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
package com.zitlab.palmyra.sqlbuilder.dialect;

import com.zitlab.palmyra.sqlbuilder.condition.NestableObject;
import com.zitlab.palmyra.sqlbuilder.query.LimitClause;
import com.zitlab.palmyra.sqlbuilder.query.OrderClause;

public abstract class Dialect {			

	public Object getBIT(String attribute, String field, Object value, int dataType) {
		String _value;
		if (value instanceof Boolean) {
			Boolean _val = (Boolean) value;
			return _val ? 1 : 0;
		} else {
			_value = value.toString().trim();
			switch (_value.toLowerCase()) {
			case "true":
			case "1":
				return 1;
			case "false":
			case "0":
				return 0;
			default:
			//	logger.debug("Invalid value for BIT attribute:" + field + " value:"  + _value);
				return null;
			}
		}
	}
	
	public void append(StringBuilder sb, LimitClause lc) {
		sb.append(" LIMIT ").append(lc.getLimit());
		int offset = lc.getOffset();
		if(offset > 0)
			sb.append(" OFFSET ").append(offset);
	}
	
	public void append(StringBuilder sb, int limit, int offset) {
		if(limit > 0)
			sb.append(" LIMIT ").append(limit);		
		if(offset > 0)
			sb.append(" OFFSET ").append(offset);
	}
	
	public void append(StringBuilder sb, OrderClause oc) {
		oc.append(sb);
	}
	
	protected void openParanthesis(StringBuilder sb, NestableObject nestable) {
		if(nestable.isParenthesis())
			sb.append("(");
	}
	
	protected void closeParanthesis(StringBuilder sb, NestableObject nestable) {
		if(nestable.isParenthesis())
			sb.append(")");
	}
}
