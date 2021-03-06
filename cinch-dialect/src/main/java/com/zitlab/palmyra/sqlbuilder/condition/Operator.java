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

public enum Operator {
	LESS_THAN(" < "), LESS_THAN_OR_EQUAL_TO(" <= "), GREATER_THAN(" > "), GREATER_THAN_OR_EQUAL_TO(" >= "),
	EQUAL_TO(" = "), NOT_EQUAL_TO(" <> "), LIKE(" LIKE "), NOT_LIKE(" NOT LIKE ");

	private final String operator;

	private Operator(String operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return operator;
	}
}
