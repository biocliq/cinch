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
package com.zitlab.palmyra.cinch.schema.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.zitlab.palmyra.cinch.dbmeta.TupleType;

public class DefaultDatabaseMetaInfo implements DatabaseMetaInfo {

	DataSource ds;

	public DefaultDatabaseMetaInfo(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public Map<String, TupleType> getTupleTypes(List<String> schemas) throws SQLException {
		Connection con = ds.getConnection();
		DatabaseMetaData dbmd = con.getMetaData();
		MetaDataUtil metadataUtil = new MetaDataUtil(dbmd);
		Map<String, TupleType> tables = metadataUtil.getAllTablesAsMap(schemas);
		Map<String, TupleType> result = new HashMap<String, TupleType>();
		for (TupleType table : tables.values()) {
			result.put(table.getName(), table);
		}
		return result;
	}
	
}