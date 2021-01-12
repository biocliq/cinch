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

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntConverter implements Converter<Integer>{

	private static final Converter<Integer> instance = new IntConverter();
	
	private IntConverter() {
		
	}
	
	public static Converter<Integer> instance() {
		return instance;
	}
	
	@Override
	public Integer read(ResultSet rs, int columnIndex) throws SQLException{
		return rs.getInt(columnIndex);
	}

	@Override
	public Integer convert(Object obj) {
		if(obj instanceof Integer) {
			return (Integer) obj;
		}else {
			try {
				return Integer.valueOf(obj.toString());
			} catch (Throwable e) {
				throw new ConverterException(" Error while converting to Int, provided value:" + obj, e);
			}
		}
	}

}
