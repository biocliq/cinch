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

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

public class TupleFilter extends SelectCriteria {

	private Expression expression;

	public void addCriteria(String column, String condition) {
		Tuple criteria = getCriteria();
		if (null != criteria) {
			criteria.setAttribute(column, condition);
		} else {
			criteria = new Tuple();
			setCriteria(criteria);
			criteria.setAttribute(column, condition);
		}
	}

	@Override
	public String getAddlCriteria() {
		if (null == expression)
			return null;
		else
			return expression.toString();
	}

	@Override
	public void setCriteria(String addlCriteria) {
		try {
			expression = CCJSqlParserUtil.parseCondExpression(addlCriteria);
		} catch (JSQLParserException e) {
			throw new RuntimeException("Invalid criteria " + addlCriteria, e);
		}
	}

	@Override
	public void addCriteria(String addlCriteria) {
		try {
			Expression expr = CCJSqlParserUtil.parseCondExpression(addlCriteria);
			if (null == expression) {
				expression = expr;
			} else
				expression = new AndExpression(expression, expr);
		} catch (JSQLParserException e) {
			throw new RuntimeException("Invalid criteria " + addlCriteria, e);
		}

	}

}
