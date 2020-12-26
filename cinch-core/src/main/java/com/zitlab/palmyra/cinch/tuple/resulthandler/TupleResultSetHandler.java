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
package com.zitlab.palmyra.cinch.tuple.resulthandler;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.cinch.rshandler.ResultSetHandler;
import com.zitlab.palmyra.cinch.rshandler.TupleFactory;

/**
 * @author ksvraja
 *
 */
public class TupleResultSetHandler extends TupleProcessor implements ResultSetHandler<Tuple> {

	private int attribMapSize = 16;

	@Override
	public Tuple processRow(ResultSet rs) throws SQLException {
		return process(rs);
	}

	@Override
	public void processMetaData(ResultSet rs) throws SQLException {
		attribMapSize = rs.getMetaData().getColumnCount();
	}
	
	public int getAttributeMapSize() {
		return attribMapSize;
	}
}