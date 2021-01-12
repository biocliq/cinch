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
package com.zitlab.palmyra.cinch.core.audit.dbimpl;

import java.util.ArrayList;
import java.util.List;

import com.zitlab.palmyra.cinch.core.audit.ChangeLog;
import com.zitlab.palmyra.cinch.core.audit.ChangeLogger;

public class DbChangeLoggerImpl implements ChangeLogger{
	private List<ChangeLog> records = new ArrayList<ChangeLog>();
	AuditLogDao auditLogDao;
	private String user;
	
	
	public DbChangeLoggerImpl(String user){
		this.user = user;
	}
	
	public void addLog(ChangeLog log){		
			records.add(log);
	}
	
	public void commit(String user){		
		auditLogDao.insert(records, user);
		records.clear();
	}
	
	public String getLogUser(){
		return user;
	}
	
	public void setAuditLogDao(AuditLogDao dao) {
		this.auditLogDao = dao;
	}

	@Override
	public void reset(String user) {
		records.clear();		
	}
}