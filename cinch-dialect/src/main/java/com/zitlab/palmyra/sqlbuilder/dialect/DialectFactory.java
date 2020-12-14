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
package com.zitlab.palmyra.sqlbuilder.dialect;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DialectFactory {
	private static final Dialect mariadbDialect 	= new MariadbDialect();
	private static final Dialect mssqlDialect		= new MsSqlDialect();
	private static final Dialect postgresqlDialect 	= new PostgresqlDialect();
	private static final Dialect oracleDialect 		= new OracleDialect();
	
	public static Dialect getDialect(String dbType) {
		switch (dbType) {
			case "postgresql":
				return postgresqlDialect;
			case "mssql":
				return mssqlDialect;
			case "mariadb":
			case "mysql":
				return mariadbDialect;
			case "oracle":
				return oracleDialect;
			default:
				return mariadbDialect;
		}
	}
	
	public static Dialect getDialect(DataSource ds) throws SQLException{
		Connection conn = ds.getConnection();
		String dbType = conn.getMetaData().getDatabaseProductName();
		return getDialect(dbType);
	}
}
