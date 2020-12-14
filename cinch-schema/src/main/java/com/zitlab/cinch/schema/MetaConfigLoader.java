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
package com.zitlab.cinch.schema;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zitlab.palmyra.sqlbuilder.dialect.Dialect;
import com.zitlab.palmyra.sqlbuilder.dialect.DialectFactory;

public class MetaConfigLoader {
	public static void load(Config config, DataSource ds) throws SQLException{
		DatabaseMetaData dbmd = ds.getConnection().getMetaData();
		config.setDbName(dbmd.getDatabaseProductName());
		config.setDbVersion(dbmd.getDatabaseProductVersion());
		config.setMajorVersion(dbmd.getDatabaseMajorVersion());
		config.setMinorVersion(dbmd.getDatabaseMinorVersion());
		config.setDialect(getDialect(config.getDbName(), config.getDbVersion()));
	}
	
	public static Dialect getDialect(String database, String version) {
		return DialectFactory.getDialect(database);
	}
}
