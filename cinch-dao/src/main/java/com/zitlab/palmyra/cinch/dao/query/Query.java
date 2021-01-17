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

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.util.CheckedConsumer;

import com.zitlab.palmyra.cinch.converter.Converter;
import com.zitlab.palmyra.cinch.converter.ConverterException;
import com.zitlab.palmyra.cinch.dao.rshandler.ListHandler;
import com.zitlab.palmyra.cinch.dao.rshandler.ResultSetHandler;
import com.zitlab.palmyra.cinch.dao.rshandler.RowCallbackHandler;
import com.zitlab.palmyra.cinch.exception.CinchException;
import com.zitlab.palmyra.cinch.quirks.Quirks;
import com.zitlab.palmyra.jdbc.util.JdbcUtils;

import static com.zitlab.palmyra.cinch.converter.Convert.throwIfNull;

public class Query {
	private QueryFactory factory;
	private boolean throwOnMappingFailure = false;
	private String name;
	
	private JdbcUtils jdbcUtils;
	private int fetchSize = -1;
	private final String query;
	private List<Object> params = new ArrayList<Object>();

//	private static final Logger logger = LoggerFactory.getLogger(Query.class);

	public Query(QueryFactory factory, String query) {
		this(factory, query, null);
	}

	public Query(QueryFactory factory, String query, String name) {
		this.factory = factory;		
		this.name = name;
		this.fetchSize = factory.getFetchSize();
		this.query = query;
		this.jdbcUtils = factory.getJdbcUtils();
	}

	public final Query throwOnMappingFailure(boolean throwOnMappingFailure) {
		this.throwOnMappingFailure = throwOnMappingFailure;
		return this;
	}

	public final boolean isThrowOnMappingFailure() {
		return throwOnMappingFailure;
	}

	public String getName() {
		return name;
	}

	public Query setParameters(Collection<Object> params) {
		if (null != params) {
			for (Object param : params) {
				addParameter(param);
			}
		}
		return this;
	}

	public Query withParams(Object... params) {
		for (Object param : params) {
			addParameter(param);
		}
		return this;
	}

	public Query addParameter(int idx, Object obj) {
		this.params.add(idx, obj);
		return this;
	}

	public Query addParameter(Object obj) {
		this.params.add(obj);
		return this;
	}

	private PreparedStatement prepareStatement(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(query);
		if (this.params.size() > 0) {
			int idx = 1;
			Quirks quirks = getQuirks();
			for (Object param : params) {
				quirks.setParameter(stmt, idx++, param);
			}
		}
		
		if (fetchSize > 0)
			stmt.setFetchSize(fetchSize);
		return stmt;
	}

	private PreparedStatement prepareStatement(Connection con, int option) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(query, option);
		if (this.params.size() > 0) {
			int idx = 1;
			Quirks quirks = getQuirks();
			for (Object param : params) {
				quirks.setParameter(stmt, idx++, param);
			}
		}
		return stmt;
	}

	public final int getFetchSize() {
		return fetchSize;
	}

	public final void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public <T> List<T> executeAndFetch(Class<T> returnType) {
		Connection con = factory.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			statement = prepareStatement(con);
			rs = statement.executeQuery();
			JdbcMapper<T> mapper = MapperFactory.getMapper(returnType);
			return mapper.forEach(rs, new ListHandler<T>()).list();
		} catch (SQLException ex) {
			throw new CinchException("Error in executeAndFetch, " + ex.getMessage(), ex);
		} finally {
			params.clear();
			jdbcUtils.closeResultSet(rs);
			jdbcUtils.closeStatement(statement);
			factory.releaseConnection(con);
		}
	}

	public <T> List<T> executeAndFetch(Type type) {
		Connection con = factory.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			statement = prepareStatement(con);
			rs = statement.executeQuery();
			JdbcMapper<T> mapper = MapperFactory.getMapper(type);
			return mapper.forEach(rs, new ListHandler<T>()).list();
		} catch (SQLException ex) {
			throw new CinchException("Error in executeAndFetch, " + ex.getMessage(), ex);
		} finally {
			params.clear();
			jdbcUtils.closeResultSet(rs);
			jdbcUtils.closeStatement(statement);
			factory.releaseConnection(con);
		}
	}

	public <T> void executeAndFetch(CheckedConsumer<T> handler, Class<T> clazz) {		
		Connection con = factory.getConnection();					
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = prepareStatement(con);
			rs = statement.executeQuery();			
			JdbcMapper<T> mapper = MapperFactory.getMapper(clazz);
			mapper.forEach(rs, handler);			
		} catch (SQLException ex) {
			throw new CinchException("Error in executeAndFetch, " + ex.getMessage(), ex);
		} finally {			
			params.clear();
			jdbcUtils.closeResultSet(rs);
			jdbcUtils.closeStatement(statement);
			factory.releaseConnection(con);			
		}
	}
	
	public void executeAndFetch(RowCallbackHandler handler) {
		Connection con = factory.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			statement = prepareStatement(con);
			rs = statement.executeQuery();
			while(rs.next())
				handler.processRow(rs);
		} catch (SQLException ex) {
			throw new CinchException("Error in executeAndFetch, " + ex.getMessage(), ex);
		} finally {
			params.clear();
			jdbcUtils.closeResultSet(rs);
			jdbcUtils.closeStatement(statement);
			factory.releaseConnection(con);
		}
	}

	public <T> List<T> executeAndFetch(ResultSetHandler<T> handler) {		
		Connection con = factory.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;		
		try {
			statement = prepareStatement(con);
			rs = statement.executeQuery();
			List<T> list = new ArrayList<T>();			
			handler.processMetaData(rs);			
			while (rs.next())
				list.add(handler.processRow(rs));			
			return list;
		} catch (SQLException ex) {
			throw new CinchException("Error in executeAndFetch, " + ex.getMessage(), ex);
		} finally {
			params.clear();
			jdbcUtils.closeResultSet(rs);
			jdbcUtils.closeStatement(statement);
			factory.releaseConnection(con);
		}
	}

	public <T> void executeAndFetch(Class<T> returnType, CheckedConsumer<T> consumer) {
		Connection con = factory.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			statement = prepareStatement(con);
			rs = statement.executeQuery();
			JdbcMapper<T> mapper = MapperFactory.getMapper(returnType);
			mapper.forEach(rs, consumer);
		} catch (SQLException ex) {
			throw new CinchException("Error in executeAndFetch, " + ex.getMessage(), ex);
		} finally {
			params.clear();
			jdbcUtils.closeResultSet(rs);
			jdbcUtils.closeStatement(statement);
			factory.releaseConnection(con);
		}
	}

	public <T> T executeAndFetchFirst(Class<T> returnType) {
		List<T> list = null;
		list = executeAndFetch(returnType);
		return list.size() > 0 ? list.get(0) : null;
	}

	public <T> T executeAndFetchFirst(Type returnType) {
		List<T> list = null;
		list = executeAndFetch(returnType);
		return list.size() > 0 ? list.get(0) : null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" }) // need to change Convert
	public <V> V insert(Class returnType, String primaryKeyColumn) {
		final Quirks quirks = this.getQuirks();
		Connection con = factory.getConnection();
		PreparedStatement statement = null;
		try {
			statement = prepareStatement(con, Statement.RETURN_GENERATED_KEYS);
			statement.executeUpdate();
			Object key = null;
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				key = rs.getObject(primaryKeyColumn);
			}
			if (null != key) {
				try {
					Converter<V> converter = throwIfNull(returnType, quirks.converterOf(returnType));
					return converter.convert(key);
				} catch (ConverterException e) {
					throw new CinchException(
							"Exception occurred while converting value from database to type " + returnType.toString(),
							e);
				}
			} else
				return null;
		} catch (SQLException ex) {
			throw new CinchException("Error in insert, " + ex.getMessage(), ex);
		} finally {
			jdbcUtils.closeStatement(statement);
			factory.releaseConnection(con);
			params.clear();
		}
	}

	public void insert() {
		executeUpdate();
	}

	public int executeUpdate() {
		Connection con = factory.getConnection();
		PreparedStatement statement = null;
		try {
			statement = prepareStatement(con);
			return statement.executeUpdate();
		} catch (SQLException ex) {
			throw new CinchException("Error in executeUpdate, " + ex.getMessage(), ex);
		} finally {
			jdbcUtils.closeStatement(statement);
			factory.releaseConnection(con);
			params.clear();
		}
	}

	public Object executeScalar() {
		Connection con = factory.getConnection();
		PreparedStatement statement = null;
		try {
			statement = prepareStatement(con);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				Object o = getQuirks().getRSVal(rs, 1);
				return o;
			} else {
				return null;
			}

		} catch (SQLException e) {
			throw new CinchException("Database error occurred while running executeScalar: " + e.getMessage(), e);
		} finally {
			jdbcUtils.closeStatement(statement);
			factory.releaseConnection(con);
			params.clear();
		}

	}

	private Quirks getQuirks() {
		return this.factory.getQuirks();
	}

	public <V> V executeScalar(Class<V> returnType) {
		try {
			Converter<V> converter;
			converter = throwIfNull(returnType, getQuirks().converterOf(returnType));
			return executeScalar(converter);
		} catch (ConverterException e) {
			throw new CinchException("Error occured while converting value from database to type " + returnType, e);
		}
	}

	public <V> V executeScalar(Converter<V> converter) {
		try {
			return converter.convert(executeScalar());
		} catch (ConverterException e) {
			throw new CinchException("Error occured while converting value from database", e);
		}
	}

	public <T> T executeAndFetchUnique(Class<T> returnType) {
		List<T> list = null;
		list = executeAndFetch(returnType);
		if (1 == list.size()) {
			return list.get(0);
		} else if (0 == list.size()) {
			return null;
		}
		throw new CinchException("More than one record found");
	}
	
	public <T> T executeAndFetchUnique(ResultSetHandler<T> returnType) {
		List<T> list = null;
		list = executeAndFetch(returnType);
		if (1 == list.size()) {
			return list.get(0);
		} else if (0 == list.size()) {
			return null;
		}
		throw new CinchException("More than one record found");
	}

	public <T> T executeAndFetchUnique(Type type) {
		List<T> list = null;
		list = executeAndFetch(type);
		if (1 == list.size()) {
			return list.get(0);
		} else if (0 == list.size()) {
			return null;
		}
		throw new CinchException("More than one record found");
	}
}
