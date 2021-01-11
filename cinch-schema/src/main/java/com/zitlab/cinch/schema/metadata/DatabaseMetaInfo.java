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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;

public interface DatabaseMetaInfo {
	
	public Map<String, TupleType> getTupleTypes(List<String> schemas) throws SQLException;
	
	public static DatabaseMetaInfo getDatabase(DataSource ds) {
		return new DefaultDatabaseMetaInfo(ds);
	}
	
	public default Map<String, TupleType> getTupleTypes(String... schemas) throws SQLException {
		List<String> sch = new ArrayList<String>();
		for(String schema : schemas) {
			sch.add(schema);
		}
		return getTupleTypes(sch);
	}
}