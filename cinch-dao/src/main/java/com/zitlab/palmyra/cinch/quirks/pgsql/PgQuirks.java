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
package com.zitlab.palmyra.cinch.quirks.pgsql;

import java.io.InputStream;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.sql2o.converters.Convert;
import org.sql2o.converters.Converter;
import org.sql2o.quirks.Quirks;
import org.sql2o.quirks.parameterparsing.SqlParameterParsingStrategy;
import org.sql2o.quirks.parameterparsing.impl.DefaultSqlParameterParsingStrategy;

@SuppressWarnings("rawtypes")
public class PgQuirks implements Quirks{
    protected final Map<Class,Converter>  converters;
    private final SqlParameterParsingStrategy sqlParameterParsingStrategy = new DefaultSqlParameterParsingStrategy();

    public PgQuirks(Map<Class, Converter> converters) {    
        this.converters = new HashMap<Class, Converter>(converters);
    }

    public PgQuirks() {
        this(Collections.<Class,Converter>emptyMap());
    }
    
    @SuppressWarnings("unchecked") @Override
    public <E> Converter<E> converterOf(Class<E> ofClass) {
        Converter c =  converters.get(ofClass);
        return c!=null?c:Convert.getConverterIfExists(ofClass);
    }

    @Override
    public String getColumnName(ResultSetMetaData meta, int colIdx) throws SQLException {
        return meta.getColumnLabel(colIdx);
    }

    @Override
    public boolean returnGeneratedKeysByDefault() {
        return true;
    }

    @Override
    public void setParameter(PreparedStatement statement, int paramIdx, Object value) throws SQLException {
        statement.setObject(paramIdx, value);
    }

    @Override
    public void setParameter(PreparedStatement statement, int paramIdx, InputStream value) throws SQLException {
        statement.setBinaryStream(paramIdx, value);
    }

    @Override
    public void setParameter(PreparedStatement statement, int paramIdx, int value) throws SQLException {
        statement.setInt(paramIdx, value);
    }

    @Override
    public void setParameter(PreparedStatement statement, int paramIdx, Integer value) throws SQLException {
        if (null != value) {
        	statement.setInt(paramIdx, value);
        } else {
        	statement.setNull(paramIdx, Types.INTEGER);
        }
    }

    @Override
    public void setParameter(PreparedStatement statement, int paramIdx, long value) throws SQLException {
        statement.setLong(paramIdx, value);
    }

    @Override
    public void setParameter(PreparedStatement statement, int paramIdx, Long value) throws SQLException {
        if (null != value) {
        	statement.setLong(paramIdx, value);
        } else {
        	statement.setNull(paramIdx, Types.BIGINT);
        }
    }

    @Override
    public void setParameter(PreparedStatement statement, int paramIdx, String value) throws SQLException {
        if (null != value) {
        	statement.setString(paramIdx, value);
        } else {
        	statement.setNull(paramIdx, Types.VARCHAR);
        }
    }

    @Override
    public void setParameter(PreparedStatement statement, int paramIdx, Timestamp value) throws SQLException {
        if (null != value) {
        	statement.setTimestamp(paramIdx, value);
        } else {
        	statement.setNull(paramIdx, Types.TIMESTAMP);
        }
    }

    @Override
    public void setParameter(PreparedStatement statement, int paramIdx, Time value) throws SQLException {
        if (null != value) {
        	statement.setTime(paramIdx, value);
        } else {
        	statement.setNull(paramIdx, Types.TIME);
        }
    }


    public void setParameter(PreparedStatement statement, int paramIdx, Boolean value) throws SQLException {
        if (null != value)
        	statement.setBoolean(paramIdx, value);
        else
        	statement.setNull(paramIdx, Types.BOOLEAN);
    }


    public void setParameter(PreparedStatement statement, int paramIdx, Date value) throws SQLException {
        if (null != value) {
            statement.setObject(paramIdx, value, Types.DATE);            
        } else {
        	statement.setNull(paramIdx, Types.DATE);
        }
    }
    
    public void setParameter(PreparedStatement statement, int paramIdx, java.sql.Date value) throws SQLException {
    	if (null != value) {
    	    statement.setDate(paramIdx, value);            
        } else {
        	statement.setNull(paramIdx, Types.DATE);
        }
    }
    
    @Override
    public void setParameter(PreparedStatement statement, int paramIdx, UUID value) throws SQLException {
        statement.setObject(paramIdx, value);
    }

    @Override
    public void setParameter(PreparedStatement statement, int paramIdx, boolean value) throws SQLException {
        statement.setBoolean(paramIdx, value);
    }

    @Override
    public Object getRSVal(ResultSet rs, int idx) throws SQLException {
        return rs.getObject(idx);
    }

    @Override
    public void closeStatement(Statement statement) throws SQLException {
        statement.close();
    }

    @Override
    public SqlParameterParsingStrategy getSqlParameterParsingStrategy() {
        return this.sqlParameterParsingStrategy;
    }
}
