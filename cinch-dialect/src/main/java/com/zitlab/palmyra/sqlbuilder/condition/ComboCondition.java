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

import java.util.ArrayList;
import java.util.List;

import com.zitlab.palmyra.sqlbuilder.dialect.Dialect;

public class ComboCondition extends Condition {
	private ArrayList<Condition> conditions = new ArrayList<Condition>();
	private Op operator = Op.AND;

	public ComboCondition(Op operator) {
		this.operator = operator;
	}

	public int count() {
		return conditions.size();
	}
	
	public ComboCondition addCondition(Condition condition) {
		conditions.add(condition);
		return this;
	}

	public enum Op {
		AND(" AND "), OR(" OR ");

		private final String operator;

		private Op(String operator) {
			this.operator = operator;
		}

		@Override
		public String toString() {
			return operator;
		}
	}

	@Override
	public void append(StringBuilder sb) {
		throw new RuntimeException("This method is not supported");
	}

	@Override
	public void append(StringBuilder sb, Dialect dialect) {
		List<Condition> conds = conditions;		
		int len = conds.size();
		boolean isParenthesis = len != 1;
		if (len > 0) {
			if(isParenthesis)
				sb.append(" (");
			else
				sb.append(" ");
			int count = 1;
			Condition condition;			
			for (int index = 0; index < len; index++) {
				condition = conds.get(index);
				condition.append(sb, dialect);
				if (count < len) {
					sb.append(operator);
				}
				count++;
			}
			if(isParenthesis)
				sb.append(")");			
		}
	}

}
