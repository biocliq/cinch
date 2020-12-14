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
package com.zitlab.palmyra.cinch.api2db.audit.dbimpl;

import java.util.List;

import com.zitlab.palmyra.cinch.api2db.audit.ChangeLog;
import com.zitlab.palmyra.cinch.query.Query;
import com.zitlab.palmyra.cinch.query.QueryFactory;

/**
 * This class manages the data access for Audit Log tables.
 * 
 * @author k.raja@biocliq.com
 *
 */

public class AuditLogDao {

	private QueryFactory factory;
	
	public AuditLogDao(QueryFactory  factory) {		
		this.factory = factory;
	}

	/**
	 * Inserts the AuditLog records into the database specified by the template.
	 * 
	 * @param records
	 * @param template
	 * @param user
	 */
	public void insert(List<ChangeLog> records, String user) {
		
		Query query = factory.createQuery(DBQuery.INSERT_AUDIT_LOG);
		for (ChangeLog record : records) {
			query.addParameter(1, record.getParentCit()); // Parent CiT Id
			query.addParameter(2, record.getParentId()); // Parent Id
			query.addParameter(3, record.getCit()); // CIT Id
			query.addParameter(4, record.getCiId()); // CI Id
			query.addParameter(5, record.getFieldName());
			query.addParameter(6, record.getNewValue());
			query.addParameter(7, record.getOldValue());
			query.addParameter(8, record.getDataType());
			query.addParameter(9, user);
			query.addParameter(10, new java.sql.Timestamp(record.getCreatedOn().getTime()));
			query.executeUpdate();
		}
	}

}