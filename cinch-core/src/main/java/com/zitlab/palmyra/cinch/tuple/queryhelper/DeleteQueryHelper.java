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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.api2db.schema.SchemaFactory;
import com.zitlab.palmyra.cinch.api2db.audit.ChangeLogger;
import com.zitlab.palmyra.cinch.tuple.dao.QueryParams;
import com.zitlab.palmyra.sqlbuilder.dialect.Dialect;
import com.zitlab.palmyra.sqlbuilder.query.DeleteQuery;

public class DeleteQueryHelper {
	private static Logger logger = LoggerFactory.getLogger(DeleteQueryHelper.class);
	
	public static QueryParams getDeleteQueryByID(Tuple item,
			ChangeLogger auditLogger, String currentUser, SchemaFactory configFactory) {
		Object id = item.getId();
		if (null == id)
			return null;
		TupleType type = item.getTupleType();
		Dialect dialect = configFactory.getConfig().getDialect();
		
		PrimaryKeyHelper keyHelper = new PrimaryKeyHelper(type);
		String reference = type.getName();
		Table table = new Table(type.getSchema(),type.getTable(), type.getName(), reference);
		
		DeleteQuery<Table> query = new DeleteQuery<Table>(table, reference, dialect);
		DataList list = new DataList();
		QueryParams queryParams =  new QueryParams(list);
		if(keyHelper.addPrimaryKeyValues(item, query, list)) {
			String queryString = query.getQuery(); 
			logger.debug("Generated delete query {}", queryString);		
			queryParams.setQuery(queryString);
			return queryParams;	
		}else
			return null;
		
	}
}
