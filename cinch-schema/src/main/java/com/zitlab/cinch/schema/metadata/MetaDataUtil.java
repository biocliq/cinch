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

package com.zitlab.cinch.schema.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zitlab.palmyra.api2db.pdbc.pojo.ForeignKey;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleAttribute;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.util.TextUtil;
import com.zitlab.palmyra.xdbmd.jdbc.MetaDataProvider;
import com.zitlab.palmyra.xdbmd.jdbc.MetaDataProviderFactory;

public class MetaDataUtil {

	private Logger logger = LoggerFactory.getLogger(MetaDataUtil.class);
	private static final Pattern ORCL_NEXTVAL_PATTERN = Pattern.compile(".*([A-Z])($)*.+[1-9\"]\\.nextval");
	private MetaDataProvider provider;

	public MetaDataUtil(DatabaseMetaData dbmd) {
		this.provider = MetaDataProviderFactory.getMetaDataProvider(dbmd);
	}

	public Map<String, TupleType> getAllTablesAsMap(String schema) {
		ResultSet rs = null;

		Map<String, TupleType> tables = new HashMap<String, TupleType>();
		try {
			rs = provider.getTables(schema, "%");// dbmd.getTables(null, schema, "%", types);
			while (rs.next()) {
				String tableName = rs.getString(3).trim();
				String ciName = TextUtil.camelCase(tableName, true);
				TupleType table = new TupleType(tableName,ciName, true);
				table.setSchema(schema);
				tables.put(getKey(schema, table.getTable()), table);
			}
			getColumns(schema, tables);
			getPrimaryKeys(schema, tables);
			getForeginKeys(schema, tables);
			getUniqueIdx(schema, tables);
		} catch (SQLException e) {
			logger.error("Error while retriving table Information", e);
			throw new RuntimeException(e);
		}

		return tables;
	}

	private void getPrimaryKeys(String schema, Map<String, TupleType> tables) {
		ResultSet rs = null;
		TupleType tupleType = null;
		TupleAttribute attribute = null;
		String key = null;
		String table = null;

		try {
			rs = provider.getPrimaryKeys(null, schema, "%");
			while (rs.next()) {
				table = rs.getString("TABLE_NAME");
				tupleType = tables.get(getKey(schema, table));
				if (null != tupleType) {
					key = rs.getString("COLUMN_NAME");
					attribute = tupleType.getFieldByColumn(key);
					if (null != attribute) {
						attribute.setPrimaryKey(true);
						tupleType.addField(attribute);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	private void printMetadata(ResultSet rs) throws SQLException {
//		ResultSetMetaData rsmd = rs.getMetaData();
//		int cnt = rsmd.getColumnCount();
//		for (int i = 1; i <= cnt; i++) {
//			System.out.println(rsmd.getColumnLabel(i));
//		}
//	}

	private void getForeginKeys(String schema, Map<String, TupleType> tables) {
		ResultSet rs = null;
		ForeignKey fkey = null;
		TupleType srcTupleType = null;
		TupleType tgtTupleType = null;
		TupleAttribute sourceAttrib = null;
		TupleAttribute targetAttrib = null;
		String fKeyName = null;
		String tgtTableName = null;
		String srcTableName = null;
		String tgtSchema = null;
		Map<String, ForeignKey> keyMap = null;

		try {
			rs = provider.getImportedKeys(null, schema, "%");
		
			while (rs.next()) {
				fKeyName = rs.getString("FK_NAME");
				srcTableName = rs.getString("FKTABLE_NAME");
				tgtTableName = rs.getString("PKTABLE_NAME");

				srcTupleType = tables.get(getKey(schema, srcTableName));
				if (null != srcTupleType)
					sourceAttrib = srcTupleType.getFieldByColumn(rs.getString("FKCOLUMN_NAME"));

				if (null != sourceAttrib) {
					keyMap = srcTupleType.getForeignKeyMap();
					fkey = keyMap.get(fKeyName);
					tgtTableName = rs.getString("PKTABLE_NAME");

					int index = tgtTableName.indexOf("_statecode_");
					if (index > 0)
						tgtTableName = tgtTableName.substring(0, index);
					tgtSchema = rs.getString("PKTABLE_SCHEM");

					tgtTupleType = tables.get(getKey(tgtSchema, tgtTableName));
					;
					if (null == fkey) {
						fkey = new ForeignKey();
						fkey.setTargetSchema(tgtSchema);
						if (null != tgtTupleType) {
							fkey.setTargetTable(tgtTupleType.getTable());
							fkey.setTargetType(tgtTupleType);
							targetAttrib = tgtTupleType.getFieldByColumn(rs.getString("PKCOLUMN_NAME"));
							fkey.addMapping(srcTableName, sourceAttrib, targetAttrib);
							keyMap.put(fKeyName, fkey);
						} else {
							logger.error("getForeignKeys : Foreignkey table {} not found for the key {}",
									getKey(tgtSchema, tgtTableName), fKeyName);
						}
					} else {
						targetAttrib = tgtTupleType.getFieldByColumn(rs.getString("PKCOLUMN_NAME"));
						fkey.addMapping(srcTableName, sourceAttrib, targetAttrib);
					}
				}
			}
			for(TupleType table: tables.values()) {
				table.refactorForeignKey();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getKey(String schema, String tgtTable) {
		if (null == schema)
			return tgtTable;
		else
			return schema + tgtTable;
	}

	private void getColumns(String schema, Map<String, TupleType> tables) throws SQLException {
		ResultSet rs = null;
		TupleAttribute column = null;
		String tableName = null;
		String columnName = null;
		DatabaseMetaData dbmd = provider.getMetadata();
		String dbName = dbmd.getDatabaseProductName().toLowerCase();
		try {
			// String[] types = { "TABLE" };
			rs = provider.getColumns(schema, "%");
			// rs = dbmd.getColumns(null, schema, "%", "%");
			while (rs.next()) {
				column = new TupleAttribute();
				tableName = rs.getString(3);
				columnName = rs.getString(4);
				column.setColumnName(columnName);

				column.setLength(rs.getInt("COLUMN_SIZE"));
				short dataType = rs.getShort(5);
				if (Types.BIT == dataType) {
					if (dbName.equals("postgresql"))
						column.setDataType(Types.BOOLEAN);
					else
						column.setDataType(Types.BIT);
				} else
					column.setDataType(dataType);

				column.setMandatory(rs.getInt("NULLABLE") == 0);

				switch (columnName) {
				case "created_by":
				case "created_on":
				case "last_upd_by":
				case "last_upd_on":
					column.setMandatory(false);
					break;

				default:
					break;
				}
				verifyAutoincrement(column, rs);
				column.setDisplayOrder(rs.getInt("ORDINAL_POSITION"));
				column.setAttribute(TextUtil.camelCase(column.getColumnName()));
				TupleType table = tables.get(getKey(schema, tableName));
				if (null != table)
					table.addField(column);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void verifyAutoincrement(TupleAttribute column, ResultSet rs) throws SQLException {
		String auto = rs.getString("IS_AUTOINCREMENT");
		if (null != auto) {
			auto = auto.toLowerCase();
			if (auto.equals("yes")) {
				column.setAutoIncrement(true);
				column.setMandatory(false);
				return;
			}
		}
		String defaultVal = rs.getString("COLUMN_DEF");
		if (null != defaultVal) {
			Matcher matcher = ORCL_NEXTVAL_PATTERN.matcher(defaultVal);
			if (matcher.matches()) {
				column.setAutoIncrement(true);
				column.setMandatory(false);
			}
		}
	}

	private void getUniqueIdx(String schema, Map<String, TupleType> tables) {

		ResultSet rs = null;
		TupleType table = null;
		TupleAttribute column = null;

		try {
			Iterator<TupleType> it = tables.values().iterator();
			while (it.hasNext()) {
				table = it.next();
				rs = provider.getIndexInfo(null, table.getSchema(), table.getTable(), true);
				while (rs.next()) {
					String indexName = rs.getString("INDEX_NAME");
					if (null == indexName || indexName.equalsIgnoreCase("PRIMARY"))
						continue;
					String columnName = rs.getString("COLUMN_NAME");
					column = table.getFieldByColumn(columnName);
					if (null != column)
						table.addUniqueKey(indexName, column);
				}
				table.removeDuplicateUQKey();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
