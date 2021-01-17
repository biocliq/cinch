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
import java.time.LocalTime;

import com.zitlab.palmyra.util.DateTimeParser;

public class TimeConverter implements Converter<Time> {

	private static final Converter<Time> instance = new TimeConverter();

	private TimeConverter() {

	}

	public static Converter<Time> instance() {
		return instance;
	}

	@Override
	public Time read(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getTime(columnIndex);
	}

	@Override
	public Time convert(Object value) {
		if (value instanceof Time) {
			return (Time) value;
		} else if (value instanceof Long) {
			return new Time((Long) value);
		} else if (value instanceof java.util.Date)
			return new Time(((java.util.Date) value).getTime());

		String _value = value.toString().trim();
		int length = _value.length();

		if (length == DateTimeParser.defaultTimeFormat.length()) {
			return DateTimeParser.parseTime(_value);
		} else if (length > 12 & length < 15) {
			try {
				Long timestamp = Long.parseLong(_value);
				return new Time(timestamp);
			} catch (Throwable te) {

			}
		} else if (length > 21) {
			LocalTime localTime = LocalTime.from(DateTimeParser.getTemporalAccessorDateTime(_value));
			return Time.valueOf(localTime);
		}

		throw new ConverterException(value.getClass() + " cannot be cast to Time");
	}

}
