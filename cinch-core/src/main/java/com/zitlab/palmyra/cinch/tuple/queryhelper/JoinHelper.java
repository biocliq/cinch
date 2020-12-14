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

import java.util.List;

import com.zitlab.palmyra.api2db.pdbc.pojo.ForeignKey;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.sqlbuilder.query.SelectQuery;

public class JoinHelper extends ClauseHelper{

	protected void addInnerJoin(SelectQuery<Table> query, Table table, TupleType tuple, Table tgtTable, TupleType tgtTuple, ForeignKey key) {
		List<String> srcColumns = key.getSourceColumns();
		List<String> tgtColumns = key.getTargetColumns();		
		query.addInnerJoin(table, srcColumns, tgtTable, tgtColumns);
	}
	
	protected void addLeftOuterJoin(SelectQuery<Table> query, Table table, TupleType tuple, Table tgtTable, TupleType tgtTuple, ForeignKey key) {
		List<String> srcColumns = key.getSourceColumns();
		List<String> tgtColumns = key.getTargetColumns();		
		query.addLeftOuterJoin(table, srcColumns, tgtTable, tgtColumns);
	}
}
