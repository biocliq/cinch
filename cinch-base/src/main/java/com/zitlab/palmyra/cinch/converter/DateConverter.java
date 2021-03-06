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
import java.sql.Time;
import java.util.Date;

import com.zitlab.palmyra.util.DateTimeParser;

public class DateConverter implements Converter<Date> {

	private static final Converter<Date> instance = new DateConverter();

	private DateConverter() {

	}

	public static Converter<Date> instance() {
		return instance;
	}

	@Override
	public Date read(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getDate(columnIndex);
	}

	@Override
	public Date convert(Object value) {
		if (null == value)
			return null;

		if (value instanceof Date) {
			return (Date) value;
		} else if (value instanceof Long)
			return new Date((long) value);
		else if (value instanceof java.sql.Date)
			return new Time(((java.sql.Date) value).getTime());

		String _value = value.toString().trim();
		int length = _value.length();

		if (length > 21) {
			return DateTimeParser.parseDateTime(_value);
		}else if (length > 12 & length < 15) {
			try {
				Long timestamp = Long.parseLong(_value);
				return new Date(timestamp);
			} catch (Throwable te) {

			}
		}
		throw new ConverterException(value.getClass() + ":" + value + " cannot be converted to Date");
	}

}
