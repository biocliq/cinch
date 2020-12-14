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
package com.zitlab.palmyra.xdbmd.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface MetaDataProvider {
	ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException;

	ResultSet getTables(String schemaPattern, String tableNamePattern)
			throws SQLException;
	
	ResultSet getColumns(String schemaPattern, String tableNamePattern)
			throws SQLException;

	ResultSet getSchemas() throws SQLException;

	ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException;

	ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique)
			throws SQLException;
	
	public ResultSet getUniqueIndexInfo(String catalog, String schema, String table)
			throws SQLException;
	
	
	public DatabaseMetaData getMetadata();
}
