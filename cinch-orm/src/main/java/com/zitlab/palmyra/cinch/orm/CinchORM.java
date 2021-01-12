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
package com.zitlab.palmyra.cinch.orm;

import javax.sql.DataSource;

import com.zitlab.palmyra.cinch.schema.DefaultSchemaFactory;
import com.zitlab.palmyra.cinch.schema.SchemaFactory;

public class CinchORM {
	private DataSource ds;
	private SchemaFactory configFactory;
	
	public CinchORM(DataSource ds) {
		this.ds = ds;
		configFactory = new DefaultSchemaFactory();
	}
	
	public CinchORM loadSchemas(String... schemas) {
		SchemaFactory factory = getConfigFactory();
		factory.load("default", ds, schemas);		
		return this;
	}
	
	public SchemaFactory getConfigFactory() {
		return configFactory;
	}
	
	public <T> QueryBuilder query(Class<T> clazz) {
		return new QueryBuilder(clazz, configFactory, ds);		
	}
}
