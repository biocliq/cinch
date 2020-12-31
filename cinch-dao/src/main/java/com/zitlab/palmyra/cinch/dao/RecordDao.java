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
package com.zitlab.palmyra.cinch.dao;

import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.simpleflatmapper.util.CheckedConsumer;

import com.zitlab.palmyra.api2db.pojo.NativeQuery;
import com.zitlab.palmyra.cinch.query.Query;
import com.zitlab.palmyra.cinch.query.QueryFactory;
import com.zitlab.palmyra.cinch.query.QueryOptions;
import com.zitlab.palmyra.cinch.rshandler.ResultSetHandler;
import com.zitlab.palmyra.cinch.rshandler.RowCallbackHandler;


/**
 * @author ksvraja
 *
 */
public abstract class RecordDao{
	protected QueryFactory factory;
	
	public RecordDao(QueryFactory queryFactory) {
		this.factory = queryFactory;
	}
	
	protected void insert(String sql, Object... params) {
		Query query = factory.createQuery(sql, params);
		query.insert();
	}

	protected <T> T insert(String sql, Class<T> returnType, String primaryKeyColumn, Object... params) {
		Query query = factory.createQuery(sql, params);
		return query.insert(returnType, primaryKeyColumn);
	}

	public <T> T insert(NativeQuery params, Class<T> returnType, String primaryKeyColumn) {
		Query query = factory.createQuery(params.getQuery(), params.getParams());
		return query.insert(returnType, primaryKeyColumn);
	}
	
	public void insert(NativeQuery params) {
		Query query = factory.createQuery(params.getQuery(), params.getParams());
		query.insert();
	}

	protected int delete(NativeQuery params) {
		return update(params);
	}

	protected int delete(String sql, Object... params) {
		Query query = factory.createQuery(sql, params);
		return query.executeUpdate();
	}

	public int update(NativeQuery params) {
		Query query = factory.createQuery(params.getQuery(), params.getParams());
		query.setParameters(params.getParams());
		return query.executeUpdate();
	}

	protected int update(String sql, Object... params) {
		Query query = factory.createQuery(sql, params);
		return query.executeUpdate();
	}

	protected <T> T selectUnique(NativeQuery params, Class<T> returnType) {
		Query query = factory.createQuery(params.getQuery(), params.getParams());		
		return query.executeAndFetchUnique(returnType);
	}
	
	protected <T> T selectUnique(NativeQuery params, ResultSetHandler<T> handler) {
		Query query = factory.createQuery(params.getQuery(), params.getParams());		
		return query.executeAndFetchUnique(handler);
	}

	protected Long getCount(NativeQuery params) {
		if(null == params.getCountQuery())
			throw new RuntimeException("Count Query cannot be null");		
		Query query = factory.createQuery(params.getCountQuery(), params.getParams());
		return query.executeAndFetchUnique(Long.class);
	}
	
	protected <T> T selectUnique(String sql, Class<T> returnType, Object... params) {
		Query query = factory.createQuery(sql, params);
		return query.executeAndFetchUnique(returnType);
	}
	
	protected <T> T selectUnique(String sql, ResultSetHandler<T> handler, Object... params) {
		Query query = factory.createQuery(sql, params);
		return query.executeAndFetchUnique(handler);
	}

	protected <T> T selectUnique(String sql, Class<T> returnType, Collection<Object> params) {
		Query query = factory.createQuery(sql, params);		
		return query.executeAndFetchUnique(returnType);
	}

	protected <T> T selectFirst(String sql, Class<T> returnType, Object... params) {
		Query query = factory.createQuery(sql, params);
		return query.executeAndFetchFirst(returnType);
	}

	public <T> List<T> select(NativeQuery params, Class<T> returnType) {
		Query query = factory.createQuery(params.getQuery(), params.getParams());		
		return query.executeAndFetch(returnType);
	}
	
	public <T> List<T> select(NativeQuery params, ResultSetHandler<T> handler) {
		Query query = factory.createQuery(params.getQuery(), params.getParams());		
		return query.executeAndFetch(handler);
	}
	
	public <T> List<T> select(String sql, ResultSetHandler<T> handler, Object... params) {
		Query query = factory.createQuery(sql, params);
		return query.executeAndFetch(handler);
	}
	
	protected <T> List<T> select(String sql, Class<T> returnType, Object... params) {
		Query query = factory.createQuery(sql, params);
		return query.executeAndFetch(returnType);
	}
	
	public <T> void select(QueryOptions params, CheckedConsumer<T> consumer, Class<T> clazz) {
		Query query = factory.createQuery(params.getQuery(), params.getParams());
		query.executeAndFetch(consumer,clazz);
	}

	public <T> void select(QueryOptions params, RowCallbackHandler consumer) {
		Query query = factory.createQuery(params.getQuery(), params.getParams());
		query.executeAndFetch(consumer);
	}
	
	public <T> void select(String sql, CheckedConsumer<T> consumer, Class<T> clazz, Object... params) {
		Query query = factory.createQuery(sql, params);
		query.executeAndFetch(consumer,clazz);
	}
	
	protected DataSource getDataSource() {
		return factory.getDataSource();
	}
}
