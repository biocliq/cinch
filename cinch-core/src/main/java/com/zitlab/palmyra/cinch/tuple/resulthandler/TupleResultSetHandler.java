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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.api2db.pojo.impl.TupleImpl;
import com.zitlab.palmyra.cinch.rshandler.ResultSetHandler;
import com.zitlab.palmyra.cinch.tuple.queryhelper.Column;
import com.zitlab.palmyra.cinch.tuple.queryhelper.Table;

/**
 * @author ksvraja
 *
 */
public class TupleResultSetHandler implements ResultSetHandler<Tuple> {

	private Map<String, Table> tableMap;
	private TupleType tupleType = null;
	private Table table;
	private int columnCount = 16;
	
	public void setTableCfg(TupleType tupleType, Map<String, Table> tableMap) {		
		this.tupleType = tupleType;
		this.tableMap = tableMap;
		String tupleTypeName = tupleType.getName();
		this.table = tableMap.get(tupleTypeName);
		tableMap.remove(tupleTypeName);
	}

	private TupleImpl getItem(String ciType) {
		TupleImpl tuple = new TupleImpl(columnCount);// TupleCache.get();
		tuple.setType(ciType);
		return tuple;
	}

	@Override
	public Tuple processRow(ResultSet rs) throws SQLException {
		String reference;
		String attReference;
		Map<String, Object> attributes;

		TupleImpl item = getItem(tupleType.getName());
		item.setTupleType(tupleType);
		attributes = item.getAttributes();
		
		for (Column column : table.getColumns()) {
			attributes.put(column.getAttribute(), column.getConverter().read(rs, column.getRsIndex()));
		}

		for (Entry<String, Table> entry : tableMap.entrySet()) {
			Table subTable = entry.getValue();
			Map<String, Object> subAttributes;
			ArrayList<Column> subCols = subTable.getColumns();
			if (subCols.size() > 0) {
				reference = subTable.getReference();
				attReference = reference.substring(reference.indexOf(".") + 1, reference.length());
				Tuple subItem = getItem(subTable.getCiType());
				subAttributes = subItem.getAttributes();

				for (Column col : subCols) {
					subAttributes.put(col.getAttribute(), col.getConverter().read(rs, col.getRsIndex()));
				}
				item.removeAttribute(attReference);
				item.setReference(attReference, subItem);
			}
		}
		return item;
	}

	@Override
	public void processMetaData(ResultSet rs) throws SQLException {
		columnCount = rs.getMetaData().getColumnCount();
		columnCount = columnCount << 1;
	}
}