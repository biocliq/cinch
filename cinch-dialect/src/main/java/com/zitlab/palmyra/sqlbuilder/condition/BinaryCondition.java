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

public class BinaryCondition extends ColumnCondition {
	private Operator operator;	
	private Object value = "?";

	
	public BinaryCondition(Operator operator, Table<? extends Column> table, String left, Object value) {
		super(null, table, left);
		this.operator= operator;
		this.value = value;
	}

	public BinaryCondition(Operator operator, String attribute, Object value) {
		super(attribute);
		this.operator = operator;
		this.value = value;
	}

	public Operator getOperator() {
		return operator;
	}

	public String toString() {
		return getColumn() + operator.toString() + value;
	}

	@Override
	public void append(StringBuilder sb) {
		if (parenthesis)
			sb.append("(");
		sb.append(getColumn()).append(operator).append("?");
		if (parenthesis)
			sb.append(")");
	}

	public static BinaryCondition lessThan(String column, Object value) {
		return new BinaryCondition(Operator.LESS_THAN, column, value);
	}

	public static BinaryCondition lessThanOrEq(String column, Object value) {
		return new BinaryCondition(Operator.LESS_THAN_OR_EQUAL_TO, column, value);
	}

	public static BinaryCondition greaterThan(String column, Object value) {
		return new BinaryCondition(Operator.GREATER_THAN, column, value);
	}

	public static BinaryCondition greaterThanOrEq(String column, Object value) {
		return new BinaryCondition(Operator.GREATER_THAN_OR_EQUAL_TO, column, value);
	}

	public static BinaryCondition equals(String column, Object value) {
		return new BinaryCondition(Operator.EQUAL_TO, column, value);
	}

	public static BinaryCondition notEquals(String column, Object value) {
		return new BinaryCondition(Operator.NOT_EQUAL_TO, column, value);
	}

	public static BinaryCondition like(String column, Object value) {
		return new BinaryCondition(Operator.LIKE, column, value);
	}

	public static BinaryCondition notLike(String column, Object value) {
		return new BinaryCondition(Operator.NOT_LIKE, column, value);
	}
	
	public static NullCondition isNull(String column) {
		return new NullCondition(column);
	}

	public static NotNullCondition isNotNull( String column) {
		return new NotNullCondition(column);
	}
	
	public static NullCondition isNull(Table<? extends Column> table, String column) {
		return new NullCondition(table, column);
	}

	public static NotNullCondition isNotNull(Table<? extends Column> table, String column) {
		return new NotNullCondition(table, column);
	}

	@Override
	public void appendValue(List<Object> valueList) {
		valueList.add(value);		
	}

}
