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
import java.util.List;
import java.util.Map;

import org.simpleflatmapper.util.CheckedConsumer;

import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.api2db.pojo.impl.TupleImpl;
import com.zitlab.palmyra.cinch.rshandler.RowCallbackHandler;
import com.zitlab.palmyra.cinch.tuple.queryhelper.Column;
import com.zitlab.palmyra.cinch.tuple.queryhelper.Table;

/**
 * @author ksvraja
 *
 */
public class TupleRowCallbackHandler implements RowCallbackHandler {
	private Map<String, Table> tableMap;	
	private CheckedConsumer<Tuple> consumer;
	private Table table;
	private TupleType tupleType;

	public void setTableCfg(TupleType tupleType, Map<String, Table> tableMap) {
		this.tupleType = tupleType;
		this.tableMap = tableMap;
		String tupleTypeName = tupleType.getName();		
		this.table = tableMap.get(tupleTypeName);
		tableMap.remove(tupleTypeName);
	}
	
	public TupleRowCallbackHandler(CheckedConsumer<Tuple> rp, TupleType tt) {
		consumer = rp;
		tupleType = tt;
	}

	private TupleImpl getItem(String ciType) {
		TupleImpl tuple = new TupleImpl();// TupleCache.get();
		tuple.setType(ciType);
		return tuple;
	}
	
	@Override
	public void processRow(ResultSet rs) throws SQLException {
		TupleImpl tuple = getItem(tupleType.getName());
		tuple.setTupleType(tupleType);
		Map<String, Object> attributes = tuple.getAttributes();
		List<Column> columns = table.getColumns();
		
		for (Column col : columns) {
			attributes.put(col.getAttribute(), col.getConverter().read(rs, col.getRsIndex()));
		}

		for (Table subTable : tableMap.values()) {
			Map<String, Object> subAttributes;
			List<Column> subCols = subTable.getColumns();
			String reference;
			String attReference;
			if (subCols.size() > 0) {
				reference = subTable.getReference();
				attReference = reference.substring(reference.indexOf(".") + 1, reference.length());
				Tuple subItem = getItem(subTable.getCiType());
				subAttributes = subItem.getAttributes();
				for(Column col: subCols) {
					subAttributes.put(col.getAttribute(), col.getConverter().read(rs, col.getRsIndex()));
				}
				tuple.removeAttribute(attReference);
				tuple.setReference(attReference, subItem);
			}
		}
		
		try {
			consumer.accept(tuple);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void processMetaData(ResultSet rs) throws SQLException {
		// Simple definition, No implementation required
	}
}