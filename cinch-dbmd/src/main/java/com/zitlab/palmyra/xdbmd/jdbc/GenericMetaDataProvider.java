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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;

public abstract class GenericMetaDataProvider implements MetaDataProvider {

	protected DatabaseMetaData databaseMetaData;

	public GenericMetaDataProvider(DatabaseMetaData dbmd) {
		this.databaseMetaData = dbmd;
	}

	@Override
	public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
		return databaseMetaData.getPrimaryKeys(catalog, schema, table);
	}

	@Override
	public ResultSet getTables(String schemaPattern, String tableNamePattern)
			throws SQLException {
		String[] types = { "TABLE", "VIEW" };
		return databaseMetaData.getTables(null, schemaPattern, tableNamePattern, types);
	}
	
	public ResultSet getColumns(String schemaPattern, String tableNamePattern)
			throws SQLException{
		return databaseMetaData.getColumns(null,  schemaPattern, tableNamePattern, "%");
	}

	@Override
	public ResultSet getSchemas() throws SQLException {
		return databaseMetaData.getSchemas();
	}

	@Override
	public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
		return databaseMetaData.getImportedKeys(catalog, schema, table);
	}

	@Override
	public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique) throws SQLException {
		return databaseMetaData.getIndexInfo(catalog, schema, table, unique, true);
	}

	@Override
	public ResultSet getUniqueIndexInfo(String catalog, String schema, String table) throws SQLException {
		return databaseMetaData.getIndexInfo(catalog, schema, table, true, true);
	}

	protected ResultSet query(String selectQuery, Object... params) throws SQLException {
		PreparedStatement ps = databaseMetaData.getConnection().prepareStatement(selectQuery);
		int size = params.length;
		for(int i =0; i < size; i++) {
			ps.setObject(i+1, params[i]);
		}
		return ps.executeQuery();
	}
	
	public boolean supports() throws SQLException{
		return getUrlPredicate().test(databaseMetaData.getURL());
	}

	protected abstract Predicate<String> getUrlPredicate();
	
	public DatabaseMetaData getMetadata() {
		return databaseMetaData;
	}
}
