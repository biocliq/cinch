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
package com.zitlab.palmyra.cinch.schema;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.zitlab.palmyra.cinch.dbmeta.TupleType;
import com.zitlab.palmyra.cinch.exception.TupleTypeNotFoundException;
import com.zitlab.palmyra.cinch.schema.metadata.DatabaseMetaInfo;
import com.zitlab.palmyra.cinch.schema.metadata.DefaultDatabaseMetaInfo;
import com.zitlab.palmyra.sqlbuilder.dialect.DialectFactory;

public class DefaultSchemaFactory implements SchemaFactory {
	private static Schema config;

	public DefaultSchemaFactory() {
	}

	public void remove(String appConfig) {
		config = null;
	}

	public Schema getConfig() {
		return config;
	}

	public Schema getConfig(String appContext) {
		return config;
	}

	public TupleType getTableCfg(String type) {
		TupleType result = null;
		if (null != config) {
			result = config.getTableCfg(type);
			if (null != result)
				return result;
			else
				throw new TupleTypeNotFoundException(
						"type " + type + " is not available in the Schema " + config.getName());
		} else
			throw new ConfigNotLoadedException("Schema not yet initialized");
	}

	public void addSchema(Schema schma) {
		config = schma;
	}

	@Override
	public boolean isMulti() {
		return false;
	}

	@Override
	public void reload() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void reset() {
		config = new Schema();
	}

	@Override
	public void load(String appContext, DataSource ds, List<String> schemas) {
		try {
			DatabaseMetaInfo metaInfo = new DefaultDatabaseMetaInfo(ds);
			Map<String, TupleType> tupleMap = metaInfo.getTupleTypes(schemas);
			config = new Schema();
			config.setTableCfg(tupleMap);
			config.setDialect(DialectFactory.getDialect(ds));
			
		} catch (SQLException e) {
			throw new RuntimeException("error while loading " + appContext, e);
		}
	}
}
