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
package com.zitlab.palmyra.jdbc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2oException;

public class DefaultJdbcUtils implements JdbcUtils {

	private static final Logger logger = LoggerFactory.getLogger(DefaultJdbcUtils.class);
	private static final JdbcUtils instance = new DefaultJdbcUtils();
	
	private DefaultJdbcUtils() {
		
	}
	
	public static JdbcUtils getInstance() {
		return instance;
	}
	
	@Override
	public Connection getConnection(DataSource ds) {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			throw new Sql2oException("Error while getting database connection", e);
		}
	}

	@Override
	public void releaseConnection(Connection con) {
		try {
			if (null != con) {
				if (!con.isClosed()) {
					con.close();
				}
			}
		} catch (SQLException e) {
		}
	}

	@Override
	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				logger.trace("Could not close JDBC ResultSet", ex);
			} catch (Throwable ex) {
				logger.trace("Unexpected exception on closing JDBC ResultSet", ex);
			}
		}
	}

	@Override
	public void closeStatement(Statement rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				logger.trace("Could not close JDBC ResultSet", ex);
			} catch (Throwable ex) {
				logger.trace("Unexpected exception on closing JDBC ResultSet", ex);
			}
		}
	}

}
