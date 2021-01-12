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
package com.zitlab.palmyra.cinch.exception;

public enum Validation {	
	MANDATORY ("2000"), 
	
	NON_MODIFIABLE("3000"),
	NON_INSERTABLE("3001"),
	
	INVALID_FORMAT("4000"),
	INVALID_DATA("4001"),
	EMPTY_DATA("4002"),
	INVALID_FORMAT_BINARY("4050"),
	
	INVALID_ATTRIBUTE("4100"),
	INVALID_ATTRIBUTE_FOREIGNKEY("4101"),
	INVALID_ATTRIBUTE_PRIMARYKEY("4102"),
	MISSING_FIELD("4110"),
	MISSING_REFERENCE("4111"),
	MISSING_SEQUENCE("4120"),
	
	INVALID_CITYPE("4150"),
	INVALID_APPLICATION("4160"),
	
	METHOD_NOT_IMPLEMENTED("4190"),
	INVALID_ACTION("4191"),
	
	MORETHAN_ONE_RECORD_FOUND("5001"),
	NO_RECORD_FOUND("5002"),
	NO_PARENT_RECORD_FOUND("5003"),
	
	OTHERS("9000"),
	CONVERSION_FAILURE("9100"),
	UNKNOWN("9900");
	
	private String code;
	
	private Validation(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
