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
package com.zitlab.palmyra.api2db.exception;

/**
 * @author ksvraja
 *
 */
public class GenericValidation extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private int errorCode;
	
	public GenericValidation(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
	
	public GenericValidation(Validation reason,  String message, Throwable e) {
        super(message, e);
        this.errorCode = reason.getCode();
    }
	
	public GenericValidation(Validation reason, String message) {
        super(message);
        this.errorCode = reason.getCode();
    }

	public int getErrorCode() {
		return errorCode;
	}
}
