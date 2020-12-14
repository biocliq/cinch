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

package com.zitlab.palmyra.cinch.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.sql2o.ResultSetHandler;

import com.zitlab.palmyra.api2db.pojo.GenericItem;

public class senericResultSetHandler implements ResultSetHandler<GenericItem> {
	public final SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

	@Override
	public GenericItem handle(ResultSet rs) throws SQLException {
		GenericItem item = new GenericItem();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		
		
		for (int i = 1; i <= columnCount; i++) {
			String columns = rsmd.getColumnLabel(i);
			Integer colType = rsmd.getColumnType(i);

			switch (colType) {
			case Types.DATE:
				Date date = rs.getDate(i);
				item.setAttribute(columns, date);
				break;
			case Types.TIMESTAMP:
			case Types.TIMESTAMP_WITH_TIMEZONE:
				Timestamp time = rs.getTimestamp(i);
				if (null != time)
					item.setAttribute(columns, TimeStampFormat.format(time));
				else
					item.setAttribute(columns, null);
				break;
			case Types.INTEGER:
			case Types.BIGINT:
			case Types.SMALLINT:
				item.setAttribute(columns, rs.getInt(i));
				break;
			case Types.CHAR:
			case Types.VARCHAR:
				item.setAttribute(columns, rs.getString(i));
				break;
			default:
				item.setAttribute(columns, rs.getString(i));
			}
		}
		return item;
	}
}