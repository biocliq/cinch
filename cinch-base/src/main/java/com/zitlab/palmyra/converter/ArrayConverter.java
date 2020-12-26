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

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArrayConverter implements Converter<Array> {

	private static final ArrayConverter instance = new ArrayConverter();

	private ArrayConverter() {

	}

	public static ArrayConverter instance() {
		return instance;
	}

	@Override
	public Array read(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getArray(columnIndex);
	}

	@Override
	public Array convert(Object obj) {
		if (null != obj) {
			try {
				return (Array) obj;
			} catch (Throwable e) {
				throw new ConverterException(obj.getClass() + " cannot be cast to Array");
			}
		}
		return null;
	}
}
