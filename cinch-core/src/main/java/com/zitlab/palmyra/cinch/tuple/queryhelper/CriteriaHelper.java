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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zitlab.palmyra.cinch.dbmeta.TupleAttribute;
import com.zitlab.palmyra.cinch.dbmeta.TupleType;
import com.zitlab.palmyra.sqlbuilder.condition.Condition;
import com.zitlab.palmyra.sqlbuilder.condition.CustomCondition;
import com.zitlab.palmyra.sqlbuilder.query.SelectQuery;

public class CriteriaHelper {

	private static final Pattern fieldPattern = Pattern.compile("\\$([a-zA-Z.\\d]+)[ !<>=,)(&|*/]?");

	public static Condition getCondition(String criteria, SelectQuery<Table> query, TupleType type) {		
		Matcher matcher = fieldPattern.matcher(criteria);

		while (matcher.find()) {
			String v = matcher.group(1);
			int idx = v.lastIndexOf('.');
			String ci = v.substring(0, idx);

			String key = getDbCriteria(ci, v, query, type);
			if (null != key) {
				criteria = criteria.replace("$" + v, key);
			}else
				//TODO replace with proper exception type
				throw new RuntimeException("Invalid criteria " + criteria);
		}
		return new CustomCondition(criteria);
	}

	public static String getDbCriteria(String cit, String attribute, SelectQuery<Table> query, TupleType type) {
		Table tbl = query.getSubTable(cit);

		TupleAttribute attrb = type.getRefField(attribute);

		if (null == tbl || null == attrb) {
			return null;
		} else {
			return tbl.getQueryAlias() + "." + attrb.getColumnName();
		}
	}
}
