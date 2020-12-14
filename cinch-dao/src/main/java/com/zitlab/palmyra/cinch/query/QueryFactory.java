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
package com.zitlab.palmyra.cinch.query;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.sql2o.quirks.Quirks;
import org.sql2o.quirks.QuirksDetector;

import com.zitlab.palmyra.cinch.util.DefaultJdbcUtils;
import com.zitlab.palmyra.cinch.util.JdbcUtils;

public final class QueryFactory{
	final Quirks quirks;
	private final DataSource dataSource;
	private JdbcUtils jdbcUtils = DefaultJdbcUtils.getInstance();
	private Map<String, String> defaultColumnMappings;
	private boolean defaultCaseSensitive;
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

	// private final static Logger logger =
	// LocalLoggerFactory.getLogger(Zql2o.class);

	/**
	 * Creates a new instance of the Sql2o class, which uses the given DataSource to
	 * acquire connections to the database.
	 * 
	 * @param dataSource The DataSource Sql2o uses to acquire connections to the
	 *                   database.
	 */
	public QueryFactory(DataSource dataSource) {
		this(dataSource, QuirksDetector.forObject(dataSource));	
	}

	/**
	 * Creates a new instance of the Sql2o class, which uses the given DataSource to
	 * acquire connections to the database.
	 * 
	 * @param dataSource The DataSource Sql2o uses to acquire connections to the
	 *                   database.
	 * @param quirks     {@link org.sql2o.quirks.Quirks} allows sql2o to work around
	 *                   known quirks and issues in different JDBC drivers.
	 */
	public QueryFactory(DataSource dataSource, Quirks quirks) {
		this.dataSource = dataSource;
		this.quirks = quirks;
		this.defaultColumnMappings = new HashMap<String, String>();
	}

	public Quirks getQuirks() {
		return quirks;
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
	 * Gets the default column mappings Map. column mappings added to this Map are
	 * always available when Sql2o attempts to map between result sets and object
	 * instances.
	 * 
	 * @return The {@link Map<String, String>} instance, which Sql2o internally uses
	 *         to map column names with property names.
	 */
	public Map<String, String> getDefaultColumnMappings() {
		return defaultColumnMappings;
	}

	/**
	 * Sets the default column mappings Map.
	 * 
	 * @param defaultColumnMappings A {@link Map} instance Sql2o uses internally to
	 *                              map between column names and property names.
	 */
	public void setDefaultColumnMappings(Map<String, String> defaultColumnMappings) {
		this.defaultColumnMappings = defaultColumnMappings;
	}

	/**
	 * Gets value indicating if this instance of Sql2o is case sensitive when
	 * mapping between columns names and property names.
	 * 
	 * @return
	 */
	public boolean isDefaultCaseSensitive() {
		return defaultCaseSensitive;
	}

	/**
	 * Sets a value indicating if this instance of Sql2o is case sensitive when
	 * mapping between columns names and property names. This should almost always
	 * be false, because most relational databases are not case sensitive.
	 * 
	 * @param defaultCaseSensitive
	 */
	public void setDefaultCaseSensitive(boolean defaultCaseSensitive) {
		this.defaultCaseSensitive = defaultCaseSensitive;
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
}
