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

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.simpleflatmapper.util.CheckedConsumer;
import org.simpleflatmapper.util.TypeReference;

import com.zitlab.palmyra.cinch.query.Query;
import com.zitlab.palmyra.cinch.query.QueryFactory;
import com.zitlab.palmyra.cinch.query.QueryOptions;
import com.zitlab.palmyra.cinch.util.JdbcUtils;

public abstract class CinchDao<T> {
	TypeReference<T> t = new TypeReference<T>() {	};

	private QueryFactory factory;

	public CinchDao() {
	}
	
	public CinchDao(QueryFactory factory) {
		this.factory = factory;
	}

	public CinchDao(DataSource ds) {
		this.factory = new QueryFactory(ds);
	}

	public CinchDao(DataSource ds, JdbcUtils utils) {
		this.factory = new QueryFactory(ds);
		this.factory.setJdbcUtils(utils);
	}

	public void insert(String sql, Object... params) {
		Query query = factory.createQuery(sql);
		query.withParams(params);
		query.insert();
	}

	public <H> H insert(String sql, Class<H> returnType, String autoIncrementColumn, Object... params) {
		Query query = factory.createQuery(sql);
		query.withParams(params);
		return query.insert(returnType, autoIncrementColumn);
	}

	public <H> H insert(QueryOptions params, Class<H> returnType, String autoIncrementColumn) {
		Query query = factory.createQuery(params.getQuery());
		query.setParameters(params.getParams());
		return query.insert(returnType, autoIncrementColumn);
	}

	public int delete(QueryOptions params) {
		return update(params);
	}

	public int delete(String sql, Object... params) {
		return update(sql, params);
	}

	public int update(QueryOptions params) {
		Query query = factory.createQuery(params.getQuery());
		query.setParameters(params.getParams());
		return query.executeUpdate();
	}

	public int update(String sql, Object... params) {
		Query query = factory.createQuery(sql);
		query.withParams(params);
		return query.executeUpdate();
	}

	public T selectUnique(QueryOptions params) {
		Query query = factory.createQuery(params.getQuery());
		query.setParameters(params.getParams());
		return query.executeAndFetchUnique(getType());
	}

	public T selectUnique(String sql, Object... params) {
		Query query = factory.createQuery(sql);
		query.withParams(params);
		return query.executeAndFetchUnique(getType());
	}

	public T selectUnique(String sql, Collection<Object> params) {
		Query query = factory.createQuery(sql);
		query.setParameters(params);
		return query.executeAndFetchUnique(getType());
	}

	public T selectFirst(String sql, Object... params) {
		Query query = factory.createQuery(sql);
		query.withParams(params);
		return query.executeAndFetchFirst(getType());
	}

	public List<T> select(QueryOptions params) {
		Query query = factory.createQuery(params.getQuery());
		query.setParameters(params.getParams());
		return query.executeAndFetch(getType());
	}

	public List<T> select(String sql, Object... params) {
		Query query = factory.createQuery(sql);
		query.withParams(params);
		return query.executeAndFetch(getType());
	}

	public void select(QueryOptions params, CheckedConsumer<T> consumer, Class<T> clazz) {
		Query query = factory.createQuery(params.getQuery());
		query.setParameters(params.getParams());
		query.executeAndFetch(consumer,clazz);
	}

	public void select(String sql, CheckedConsumer<T> consumer, Class<T> clazz, Object... params) {
		Query query = factory.createQuery(sql);
		query.withParams(params);
		query.executeAndFetch(consumer,clazz);
	}

	protected DataSource getDataSource() {
		return factory.getDataSource();
	}

	public void setDataSource(DataSource ds) {
		factory = new QueryFactory(ds);
	}

	protected QueryFactory getFactory() {
		return factory;
	}

	public Type getType() {		
		return t.getType();
	}
}
