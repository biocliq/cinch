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
package com.zitlab.palmyra.cinch.converter;

import com.zitlab.palmyra.cinch.exception.Validation;
import com.zitlab.palmyra.exception.CodedException;

public class ConverterException extends CodedException{

	private static final long serialVersionUID = 8772752616109842229L;
	
	public ConverterException(String message, Throwable e){
		super(Validation.CONVERSION_FAILURE.getCode(), message, e);
	}

	public ConverterException(String message){
		super(Validation.CONVERSION_FAILURE.getCode(), message);
	}

}
