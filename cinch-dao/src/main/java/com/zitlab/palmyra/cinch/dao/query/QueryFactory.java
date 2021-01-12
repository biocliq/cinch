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
package com.zitlab.palmyra.cinch.dao.query;

import java.sql.Connection;
import java.util.Collection;

import javax.sql.DataSource;

import org.sql2o.quirks.Quirks;
import org.sql2o.quirks.QuirksDetector;

import com.zitlab.palmyra.jdbc.util.DefaultJdbcUtils;
import com.zitlab.palmyra.jdbc.util.JdbcUtils;

public final class QueryFactory{
	
	private final DataSource dataSource;
	private JdbcUtils jdbcUtils = DefaultJdbcUtils.getInstance();
	private Quirks quirks;
	
	/**
	 * If this variable is set to a non-negative value, it will be used for setting
	 * the fetchSize property on statements used for query processing.
	 */
	private int fetchSize = -1;

	/**
	 * If this variable is set to a non-negative value, it will be used for setting
	 * the maxRows property on statements used for query processing.
	 */
	private int maxRows = -1;

	/**
	 * If this variable is set to a non-negative value, it will be used for setting
	 * the queryTimeout property on statements used for query processing.
	 */
	private int queryTimeout = -1;


	public QueryFactory(DataSource dataSource) {
		this.dataSource = dataSource;
		this.quirks = QuirksDetector.forObject(dataSource);
	}

	/**
	 * Gets the DataSource that Sql2o uses internally to acquire database
	 * connections.
	 * 
	 * @return The DataSource instance
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	public Query createQuery(String sql) {
		Query query = new Query(this, sql);		
		return query;
	}
	
	public Query createQuery(String sql, Object... params) {
		Query query = new Query(this, sql);
		query.withParams(params);
		return query;
	}
	
	public Query createQuery(String sql, Collection<Object> params) {
		Query query = new Query(this, sql);
		query.setParameters(params);
		return query;
	}

	/**
	 * @return the fetchSize
	 */
	public int getFetchSize() {
		return fetchSize;
	}

	/**
	 * @param fetchSize the fetchSize to set
	 */
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	/**
	 * @return the maxRows
	 */
	public int getMaxRows() {
		return maxRows;
	}

	/**
	 * @param maxRows the maxRows to set
	 */
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	/**
	 * @return the queryTimeout
	 */
	public int getQueryTimeout() {
		return queryTimeout;
	}

	/**
	 * @param queryTimeout the queryTimeout to set
	 */
	public void setQueryTimeout(int queryTimeout) {
		this.queryTimeout = queryTimeout;
	}

	public JdbcUtils getJdbcUtils() {
		return jdbcUtils;
	}

	public void setJdbcUtils(JdbcUtils jdbcUtils) {
		this.jdbcUtils = jdbcUtils;
	}

	public Connection getConnection() {
		return this.jdbcUtils.getConnection(this.dataSource);
	}

	public void releaseConnection(Connection con) {
		this.jdbcUtils.releaseConnection(con);
	}

	public Quirks getQuirks() {
		return quirks;
	}
}
