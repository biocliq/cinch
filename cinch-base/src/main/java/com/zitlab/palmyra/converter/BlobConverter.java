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

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobConverter implements Converter<Blob>{

	private static final Converter<Blob> instance = new BlobConverter();
	
	private BlobConverter() {
		
	}
	
	public static Converter<Blob> instance() {
		return instance;
	}
	
	@Override
	public Blob read(ResultSet rs, int columnIndex) throws SQLException{
		return rs.getBlob(columnIndex);
	}

	@Override
	public Blob convert(Object obj) {
		if(obj instanceof Blob) {
			return (Blob) obj;
		}else {
			throw new ConverterException(obj.getClass() + " cannot be cast to Blob");
		}
	}

}
