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
package com.zitlab.palmyra.api2db.schema;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;

public interface SchemaFactory {
		
	public void remove(String schema);
	
	public Schema getConfig();
	
	public Schema getConfig(String appContext);
	
	public void addSchema(Schema schema);
	
	public TupleType getTableCfg(String type);

	public void reload();
	
	public boolean isMulti();
	
	public void reset();

	public void load(String appContext, DataSource ds, List<String> schemas);
	
	public default void load(String appContext, DataSource ds, String... schemas) {
		List<String> schemaList = new ArrayList<String>();
		for(String schema: schemas) {
			schemaList.add(schema);
		}
		load(appContext, ds, schemaList);
	}
}
