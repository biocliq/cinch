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
package com.zitlab.palmyra.cinch.tuple.queryhelper;

import java.lang.reflect.Type;
import java.util.List;

import javax.sql.DataSource;

import com.zitlab.cinch.schema.Config;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.cinch.dao.CinchDao;
import com.zitlab.palmyra.cinch.mapper.ClassMapper;
import com.zitlab.palmyra.cinch.query.QueryFactory;
import com.zitlab.palmyra.cinch.tuple.dao.QueryParams;
import com.zitlab.palmyra.cinch.tuple.dao.TupleFilter;

public class QueryBuilder {
	private Class<?> clazz;
	private Config config;
	private DataSource ds;
	private TupleFilter filter = new TupleFilter();
	
	public <T> QueryBuilder(Class<T> clazz, Config config, DataSource ds) {
		this.clazz = clazz;
		this.config = config;
		this.ds = ds;
		filter.setLimit(-1);
	}
	
	public QueryBuilder addCriteria(String column, String condition){
		filter.addCriteria(column, condition);
		return this;
	}
	
	
	public QueryBuilder addCriteria(String condition){
		filter.addCriteria(condition);
		return this;
	}
	
	public <T> List<T> list() {
		QueryFactory qf = new QueryFactory(ds);
		CinchDao<T> dao = new CinchDao<T>(qf) {
			public Type getType() {
				return clazz;
			}
		};
		ClassMapper mapper = new ClassMapper();
		String type = mapper.getTable(clazz);
		TupleType tupleType = config.getTableCfg(type);
		SelectQueryHelper selectQueryHelper = new SelectQueryHelper(config);
		QueryParams params = selectQueryHelper.getSearchQuery(tupleType, filter);
		params.setExpectedResultSetSize(filter.getLimit());
		System.out.println(params.getQuery());
		return dao.select(params);
	}
	
}
