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
package com.zitlab.palmyra.cinch;

import javax.sql.DataSource;

import com.zitlab.cinch.schema.Config;
import com.zitlab.cinch.schema.ConfigFactory;
import com.zitlab.cinch.schema.DefaultConfigFactory;

public class CinchORM {
	private DataSource ds;
	private ConfigFactory configFactory;
	private Config config;
	
	public CinchORM(DataSource ds) {
		this.ds = ds;
		configFactory = new DefaultConfigFactory();
	}
	
	public CinchORM loadSchemas(String... schemas) {
		ConfigFactory factory = getConfigFactory();
		factory.load("default", ds, schemas);
		config = factory.getConfig();
		return this;
	}
	
	public ConfigFactory getConfigFactory() {
		return configFactory;
	}
	
	public <T> QueryBuilder query(Class<T> clazz) {
		return new QueryBuilder(clazz, configFactory.getConfig(), ds);		
	}
}
