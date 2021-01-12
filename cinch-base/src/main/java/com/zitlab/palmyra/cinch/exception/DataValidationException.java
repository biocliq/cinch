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

import com.zitlab.palmyra.cinch.pojo.Record;
import com.zitlab.palmyra.exception.CodedException;

/**
 * @author ksvraja
 *
 */
public class DataValidationException extends CodedException{
	private static final long serialVersionUID = 1L;

	private String fieldName = null;
	private Record record = null;
	
	public DataValidationException(String fieldName, Validation reason) {
		super(reason.getCode(), get(fieldName, reason));
		this.fieldName = fieldName;
    }
	
	public DataValidationException(String fieldName, String message) {
		super(Validation.OTHERS.getCode());
		this.fieldName = fieldName;		
	}
	
	public DataValidationException(String fieldName, String message, Validation reason) {
		super(reason.getCode(), message);
		this.fieldName = fieldName;
	}
			
	public String getFieldName() {
		return fieldName;
	}

	protected void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	private static String get(String field, Validation reason) {
		switch (reason) {
		case MANDATORY:
			return field + " is mandatory";
		case NON_MODIFIABLE:
			return field + " is not modifiable";
		case NON_INSERTABLE:
			return field + " is not insertable";
		case INVALID_FORMAT:
			return "Invalid format for the field " + field;
		default:
			return field + " is invalid";
		}
	}

	public Record getRecord() {
		return record;
	}
}
