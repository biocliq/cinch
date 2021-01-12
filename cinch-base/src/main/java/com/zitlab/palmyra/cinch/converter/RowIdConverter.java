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
import java.sql.RowId;
import java.sql.SQLException;

public class RowIdConverter implements Converter<RowId>{

	private static final Converter<RowId> instance = new RowIdConverter();
	
	private RowIdConverter() {
		
	}
	
	public static Converter<RowId> instance() {
		return instance;
	}
	
	@Override
	public RowId read(ResultSet rs, int columnIndex) throws SQLException{
		return rs.getRowId(columnIndex);
	}

	@Override
	public RowId convert(Object obj) {
		if(obj instanceof RowId) {
			return (RowId) obj;
		}else {
			throw new ConverterException(obj.getClass() + " cannot be cast to RowId");
		}
	}

}
