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

import java.lang.reflect.Type;
import java.util.List;

import javax.sql.DataSource;

import com.zitlab.palmyra.cinch.dao.annotations.Extractor;
import com.zitlab.palmyra.cinch.dao.query.QueryFactory;
import com.zitlab.palmyra.cinch.dbmeta.TupleType;
import com.zitlab.palmyra.cinch.orm.dao.CinchDao;
import com.zitlab.palmyra.cinch.pojo.TupleFilter;
import com.zitlab.palmyra.cinch.schema.SchemaFactory;
import com.zitlab.palmyra.cinch.tuple.dao.QueryParams;
import com.zitlab.palmyra.cinch.tuple.queryhelper.SelectQueryHelper;

public class QueryBuilder {
	private Class<?> clazz;
	private SchemaFactory configFactory;
	private DataSource ds;
	private TupleFilter filter = new TupleFilter();
	
	public <T> QueryBuilder(Class<T> clazz, SchemaFactory config, DataSource ds) {
		this.clazz = clazz;
		this.configFactory = config;
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
		Extractor mapper = new Extractor();
		String type = mapper.getTable(clazz);
		TupleType tupleType = configFactory.getTableCfg(type);
		SelectQueryHelper selectQueryHelper = new SelectQueryHelper(configFactory);
		QueryParams params = selectQueryHelper.getSearchQuery(tupleType, filter);
		params.setExpectedResultSetSize(filter.getLimit());
		return dao.select(params);
	}
	
}
