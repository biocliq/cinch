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

import com.zitlab.cinch.schema.Config;
import com.zitlab.palmyra.api2db.exception.DDMException;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleAttribute;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.sqlbuilder.query.Query;
import com.zitlab.palmyra.util.StringBuilderCache;

/**
 * @author k.raja@biocliq.com
 *
 */
public class AddlClauseHelper {

	public static final String CURRENT_USER = "::current_user";

	public static String convertJoinClause(String clause, Query<Table> query, Config schema) {

		StringBuilder sb = StringBuilderCache.get();
		if (null == clause)
			return null;
		String[] fragments = clause.split(" ");

		for (String fragment : fragments) {
			int index = fragment.indexOf("$");
			if (index >= 0) {
				updateAlias(fragment.substring(1), query, sb, schema);
			} else {
				sb.append(fragment);
			}
			sb.append(" ");
		}
		return StringBuilderCache.release(sb);
	}

	public static String convertCriteriaClause(String clause, Query<Table> query, Config schema) {

		StringBuilder sb = StringBuilderCache.get();
		if (null == clause)
			return null;
		String[] fragments = clause.split(" ");

		for (String fragment : fragments) {
			int index = fragment.indexOf("$");
			if (index >= 0) {
				updateAlias(fragment.substring(1), query, sb, schema);
			} else {
				sb.append(fragment);
			}
			sb.append(" ");
		}
		return StringBuilderCache.release(sb);
	}
	
	private static void updateAlias(String fragment, Query<Table> query, StringBuilder sb, Config schema) {
		int index = fragment.lastIndexOf(".");
		if (index > 0) {
			Table table = query.getSubTable(fragment.substring(0, index));
			if (null != table) {
				String ciType = table.getCiType();
				TupleType tt = schema.getTableCfg(ciType);
				if (null != tt) {
					sb.append(table.getQueryAlias()).append('.');
					String attribute = fragment.substring(index + 1);

					TupleAttribute attrib = tt.getField(attribute);
					if (null != attrib) {
						sb.append(attrib.getColumnName());
					} else
						sb.append(attribute);
				}else
					sb.append(fragment);
			} else
				throw new DDMException("Table reference " + fragment.substring(0, index) + " not found");
		} else if (index < 0) {
			Table table = query.getSubTable(fragment);
			if(null != table)
				sb.append(table.getQueryAlias());
			else
				sb.append(fragment);
		} else {
			throw new DDMException("Table reference cannot be empty " + fragment);
		}

	}
}
