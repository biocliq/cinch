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
package com.zitlab.palmyra.cinch.tuple.resulthandler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Base64;

import com.zitlab.palmyra.api2db.pojo.GenericItem;
import com.zitlab.palmyra.cinch.rshandler.ResultSetHandler;

public class GenericResultHandler implements ResultSetHandler<GenericItem>{
	
	private String[] columns;
	private int colTypes[];
	private int columnCount;
	
	
	public void processMetaData(ResultSet rs) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		columnCount = rsmd.getColumnCount();
		
		columns = new String[columnCount];
		colTypes = new int[columnCount];
		
		for (int i = 0; i < columnCount; i++) {
			columns[i] = rsmd.getColumnLabel(i + 1);
			colTypes[i] = rsmd.getColumnType(i + 1);
		}
	}
	
	
	@Override
	public GenericItem processRow(ResultSet rs) throws SQLException {
		GenericItem item = new GenericItem();
		
		int i = 0;
		for (int index = 1; index <= columnCount; index++) {
			String fieldName = columns[i];
			int colType = colTypes[i];
			i++;
			
			switch (colType) {
			case Types.VARCHAR:
				item.setAttribute(fieldName, rs.getString(index));
			case Types.CHAR:
				String str = rs.getString(index);
				if (null != str) {
					item.setAttribute(fieldName, str.trim());
				} else
					item.setAttribute(fieldName, null);
				break;
			case Types.DATE:
				Date date = rs.getDate(index);
				if (null != date) {
					item.setAttribute(fieldName, date);
				}
				break;
			case Types.TIME:
			case Types.TIMESTAMP:
			case Types.TIME_WITH_TIMEZONE:
			case Types.TIMESTAMP_WITH_TIMEZONE:
				Timestamp ts = rs.getTimestamp(index);
				if (null != ts) {
					item.setAttribute(fieldName, ts);
				}
				break;
			case Types.BINARY: {
				byte[] val = rs.getBytes(index);
				if (null != val) {
					item.setAttribute(fieldName, Base64.getEncoder().encodeToString(val));
				} else
					item.setAttribute(fieldName, null);
				break;
			}
			default:
				item.setAttribute(fieldName, rs.getObject(index));
			}
		}
		return item;
	}
}
