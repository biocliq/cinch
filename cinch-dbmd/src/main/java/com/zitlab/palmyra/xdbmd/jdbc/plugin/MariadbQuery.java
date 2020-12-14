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
package com.zitlab.palmyra.xdbmd.jdbc.plugin;

interface MariadbQuery {

	public static final String GET_IMPORTED_KEYS = "SELECT" + 
			"     KCU.CONSTRAINT_CATALOG AS 'FKTABLE_CATALOG'" + 
			"   , KCU.CONSTRAINT_SCHEMA AS 'FKTABLE_SCHEM'" + 
			"   , KCU.CONSTRAINT_NAME AS 'FK_NAME'" + 
			"   , KCU.TABLE_NAME AS 'FKTABLE_NAME'" + 
			"   , KCU.COLUMN_NAME AS 'FKCOLUMN_NAME'" + 
			"   , KCU.ORDINAL_POSITION AS 'FK_ORDINAL_POSITION'" + 
			"   , RC.UNIQUE_CONSTRAINT_CATALOG AS 'PKTABLE_CATALOG'" + 
			"   , RC.UNIQUE_CONSTRAINT_SCHEMA AS 'PKTABLE_SCHEM'" + 
			"   , RC.UNIQUE_CONSTRAINT_NAME AS 'PK_NAME'" + 
			"   , KCU.REFERENCED_TABLE_NAME AS 'PKTABLE_NAME'" + 
			"   , KCU.REFERENCED_COLUMN_NAME AS 'PKCOLUMN_NAME'" + 
			"   , RC.DELETE_RULE AS 'DELETE_RULE'" + 
			"   , RC.UPDATE_RULE AS 'UPDATE_RULE'" + 
			"  FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS RC" + 
			"  INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE KCU" + 
			"  ON KCU.CONSTRAINT_CATALOG = RC.CONSTRAINT_CATALOG" + 
			"   AND KCU.CONSTRAINT_SCHEMA = RC.CONSTRAINT_SCHEMA" + 
			"   AND KCU.CONSTRAINT_NAME = RC.CONSTRAINT_NAME" + 
			"    WHERE KCU.CONSTRAINT_SCHEMA LIKE ? " + 
			"  AND KCU.TABLE_NAME LIKE  ?";
}
