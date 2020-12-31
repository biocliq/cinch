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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.cinch.rshandler.ResultSetHandler;
import com.zitlab.palmyra.converter.Converter;

public class GenericTupleHandler implements ResultSetHandler<Tuple>{
	
	private Column[] columns;
	private int columnCount;
	
	public void processMetaData(ResultSet rs) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		columnCount = rsmd.getColumnCount();		
		columns = new Column[columnCount];
		Column column;
		int j=1;
		for (int i = 0; i < columnCount; i++) {
			column = new Column();
			column.column = rsmd.getColumnLabel(j);
			column.dataType = rsmd.getColumnType(j);
			column.converter = Converter.getConverter(column.dataType);
			columns[i] = column;
			j++;
		}
	}
	
	
	@Override
	public Tuple processRow(ResultSet rs) throws SQLException {		
		Tuple item = new Tuple();
		Column column;
		
		int i = 0;
		for (int index = 1; index <= columnCount; index++) {
			column = columns[i];
			i++;			
			item.setAttribute(column.column, column.converter.read(rs, index));
			
		}
		return item;
	}
	
	private class Column {
		String column;
		int dataType;
		Converter<?> converter;
	}
}
