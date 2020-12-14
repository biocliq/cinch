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
package com.zitlab.palmyra.xdbmd.jdbc.plugin;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;

import com.zitlab.palmyra.xdbmd.jdbc.GenericMetaDataProvider;

public class HsqlMetaDataProvider extends GenericMetaDataProvider {

	public HsqlMetaDataProvider(DatabaseMetaData dbmd) {
		super(dbmd);
	}
	
	@Override
	protected Predicate<String> getUrlPredicate() {
		return connectionUrl -> (connectionUrl.startsWith("jdbc:hsql:"));
	}
	public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
		String _schemaPattern = (null == schema)  ? "%" : schema;
		String _tablePattern = (null == table) ? "%" : table;
		return query(HsqlQuery.GET_IMPORTED_KEYS, _schemaPattern,_tablePattern);
	}
}
