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
package com.zitlab.palmyra.sqlbuilder.condition;

import com.zitlab.palmyra.sqlbuilder.query.Column;
import com.zitlab.palmyra.sqlbuilder.query.Table;

public abstract class Condition extends NestableObject{

	protected final String getColumnAlias(Table<? extends Column> table, String column) {
		StringBuilder sb = new StringBuilder(32);
		return sb.append(table.getQueryAlias()).append('.').append(column).toString();
	}
}
