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
import java.sql.Types;

public interface Converter<T> {

	public T read(ResultSet rs, int columnIndex) throws SQLException;

	public T convert(Object obj);

	public static Converter<?> getConverter(int dataType) {
		switch (dataType) {
		case Types.CHAR:
		case Types.VARCHAR:
			return StringConverter.instance();
		case Types.LONGNVARCHAR:
		case Types.NCHAR:
			return NStringConverter.instance();
		case Types.DECIMAL:
			return BigDecimalConverter.instance();
		case Types.FLOAT:
		case Types.REAL:
		case Types.NUMERIC:
			return FloatConverter.instance();
		case Types.SMALLINT:
			return ShortConverter.instance();
		case Types.INTEGER:
			return IntConverter.instance();
		case Types.BIGINT:
			return LongConverter.instance();
		case Types.BIT:
			return BitConverter.instance();
		case Types.BOOLEAN:
			return BooleanConverter.instance();
		case Types.TIMESTAMP:
		case Types.TIMESTAMP_WITH_TIMEZONE:
			return TimestampConverter.instance();
		case Types.DATE:
			return DateConverter.instance();
		case Types.TIME:
		case Types.TIME_WITH_TIMEZONE:
			return TimeConverter.instance();
		case Types.ARRAY:
			return ArrayConverter.instance();
		default:
			return ObjectConverter.instance();
		}
	}
}
