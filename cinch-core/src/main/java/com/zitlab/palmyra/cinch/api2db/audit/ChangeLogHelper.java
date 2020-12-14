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
package com.zitlab.palmyra.cinch.api2db.audit;

import java.util.Date;

import com.zitlab.palmyra.api2db.pojo.Tuple;

public class ChangeLogHelper {

	private Object parentId;
	private String parentCit;
	private String cit;
	private Object ciId;
	private Date createdOn;
	private String createdBy;

	public ChangeLogHelper(String cit, Object ciid, String parentCit, Object parentId, String user, Date createdOn) {
		this.cit = cit;
		this.ciId = ciid;
		this.parentCit = parentCit;
		this.parentId = parentId;
		this.createdBy = user;
		this.createdOn = createdOn;
	}

	public ChangeLogHelper(Tuple item, String user, Date createdOn) {
		this.cit = item.getType();
		this.ciId = item.getId();
		this.parentCit = this.cit;
		this.parentId = this.ciId;
		this.createdBy = user;
		this.createdOn = createdOn;
	}

	public void addLog(ChangeLogger logger, String fieldName, int dataType, Object newValue, Object oldValue) {
		if (null != logger) {
			ChangeLog log = new ChangeLog(cit, ciId, parentCit, parentId, createdBy, createdOn);
			log.setFieldName(fieldName);
			log.setDataType(dataType);
			if (null != newValue) {
				log.setNewValue(newValue.toString());
			}
			if (null != oldValue) {
				log.setOldValue(oldValue.toString());
			}
			logger.addLog(log);
		}
	}
}
