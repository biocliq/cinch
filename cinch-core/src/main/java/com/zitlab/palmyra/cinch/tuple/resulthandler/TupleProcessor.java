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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.cinch.rshandler.TupleFactory;
import com.zitlab.palmyra.cinch.tuple.queryhelper.Column;
import com.zitlab.palmyra.cinch.tuple.queryhelper.Table;

/**
 * @author ksvraja
 *
 */
abstract class TupleProcessor {
	private Map<String, Table> parentTableMap;
	private Table table;
	private TupleType tupleType;
	private int primaryTableSize = 16;
	private TupleFactory factory;
	
	public void setTableCfg(TupleType tupleType, Map<String, Table> tableMap, TupleFactory factory) {
		this.tupleType = tupleType;
		this.factory = factory;
		String tupleTypeName = tupleType.getName();
		this.table = tableMap.get(tupleTypeName);
		primaryTableSize = this.table.getColumns().size();
		this.parentTableMap = new HashMap<String, Table>(tableMap.size() * 2);
		Table table;
		String key;

		for (Entry<String, Table> entry : tableMap.entrySet()) {
			table = entry.getValue();
			key = entry.getKey();
			if (table.getColumns().size() > 0 && !key.equals(tupleTypeName)) {
				parentTableMap.put(key, table);
			}
		}
	}

	private Tuple getItem(String ciType, int count) {
		Tuple tuple = factory.instance(count);
		tuple.setType(ciType);
		return tuple;
	}

	public Tuple process(ResultSet rs) throws SQLException {
		Tuple tuple = getItem(tupleType.getName(), primaryTableSize);
		tuple.setTupleType(tupleType);
		Map<String, Object> attributes = tuple.getAttributes();
		List<Column> columns = table.getColumns();
		//String reference;
		String attReference;
		Tuple subItem;
		Map<String, Object> subAttributes;
		List<Column> subCols;
		
		for (Column col : columns) {
			attributes.put(col.getAttribute(), col.getConverter().read(rs, col.getRsIndex()));
		}

		for (Table parentTable : parentTableMap.values()) {
			subCols = parentTable.getColumns();
			//reference = subTable.getReference();
			attReference = parentTable.getAttributeReference(); //reference.substring(reference.indexOf(".") + 1, reference.length());
			subItem = getItem(parentTable.getCiType(), subCols.size());
			subAttributes = subItem.getAttributes();
			for (Column col : subCols) {
				subAttributes.put(col.getAttribute(), col.getConverter().read(rs, col.getRsIndex()));
			}
			tuple.removeAttribute(attReference);
			tuple.setParent(attReference, subItem);
		}
		return tuple;
	}
}