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

public class LongConverter implements Converter<Long> {

	private static final LongConverter instance = new LongConverter();

	private LongConverter() {

	}

	public static LongConverter instance() {
		return instance;
	}

	@Override
	public Long read(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getLong(columnIndex);
	}

	@Override
	public Long convert(Object obj) {
		if(obj instanceof Long) {
			return (Long) obj;
		}else {
			try {
				return Long.valueOf(obj.toString());
			} catch (Throwable e) {
				throw new ConverterException(" Error while converting to Long, provided value:" + obj, e);
			}
		}
	}
}
