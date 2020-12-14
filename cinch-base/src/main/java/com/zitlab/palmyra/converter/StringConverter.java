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
package com.zitlab.palmyra.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringConverter implements Converter<String>{

	private static final Converter<String> instance = new StringConverter();
	
	private StringConverter() {
		
	}
	
	public static Converter<String> instance() {
		return instance;
	}
	
	@Override
	public String read(ResultSet rs, int columnIndex) throws SQLException{
		return rs.getString(columnIndex);
	}

	@Override
	public String convert(Object obj) {
		if(obj instanceof String) {
			return (String) obj;
		}else {
			return obj.toString();
		}
	}

}
