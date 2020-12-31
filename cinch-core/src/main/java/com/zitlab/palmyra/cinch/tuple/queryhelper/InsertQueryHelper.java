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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zitlab.palmyra.api2db.exception.FieldValidationException;
import com.zitlab.palmyra.api2db.exception.Validation;
import com.zitlab.palmyra.api2db.pdbc.pojo.ForeignKey;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleAttribute;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.api2db.schema.SchemaFactory;
import com.zitlab.palmyra.cinch.exception.RecordException;
import com.zitlab.palmyra.cinch.tuple.dao.QueryParams;
import com.zitlab.palmyra.cinch.tuple.dao.TupleDao;
import com.zitlab.palmyra.sqlbuilder.dialect.Dialect;
import com.zitlab.palmyra.sqlbuilder.query.InsertQuery;

public class InsertQueryHelper extends QueryHelper {

	private static Logger logger = LoggerFactory.getLogger(InsertQueryHelper.class);
	private Set<String> excludeFields;
	private BiConsumer<String, String> verifier;

	public InsertQueryHelper(Set<String> excludeFields) {
		this.excludeFields = excludeFields;
		this.verifier = (null == excludeFields || 0 == excludeFields.size()) ? NoopVerifier.instance()
				: new ExcludeVerifier();
	}

	public QueryParams getInsertStatement(TupleType tupleType, Tuple item, SchemaFactory configFactory, TupleDao dao) {
		TupleAttribute tupleAttribute;
		Dialect dialect = configFactory.getConfig().getDialect();
		Map<String, Object> attributes = item.getAttributes();
		DataList valueList = new DataList();
		Map<String, TupleAttribute> fieldMap = tupleType.getAllFields();
		Map<String, Tuple> relatedCIs = item.getParent();
		String reference = tupleType.getName();
		Table table = new Table(tupleType.getSchema(), tupleType.getTable(), tupleType.getName(), reference);
		// Initialize the Insert Query
		InsertQuery<Table> query = new InsertQuery<Table>(table, reference, dialect);
		// Table table = query.getPrimaryTable();
		// For each attribute in the Configuration Item Type.
		for (Entry<String, TupleAttribute> field : fieldMap.entrySet()) {
			Object value;
			String key = field.getKey();
			tupleAttribute = field.getValue();

			if (attributes.containsKey(key)) {
				verifier.accept(reference, key);
				value = attributes.get(key);
				tupleAttribute.checkMandatory(reference, value);

				table.addColumn(tupleAttribute);
				valueList.add(value, tupleAttribute);
			} else {
				// Referenced CI
				Tuple refItem = relatedCIs.get(key);
				ForeignKey fKey = tupleType.getForeignKey(key);
				// Verify mandatory parameter
				if (null != refItem && null != refItem.getId() && null != fKey) {
					if (refItem.isDbExists()) {
						List<TupleAttribute> srcAttrs = fKey.getSource();
						List<TupleAttribute> tgtAttrs = fKey.getTarget();
						TupleAttribute tgtAttrib;
						TupleAttribute srcAttrib;

						for (int i = 0; i < srcAttrs.size(); i++) {
							srcAttrib = srcAttrs.get(i);
							verifier.accept(reference, srcAttrib.getAttribute());
							table.addColumn(srcAttrib);
							tgtAttrib = tgtAttrs.get(i);
							value = getRefField(refItem, tgtAttrib.getAttribute());
							valueList.add(value, tgtAttrib);
						}
					} else {
						throw new RecordException.NotFound("No record is found for the primary key `" + refItem.getId()
								+ "` of the CI Type `" + tupleType.getName() + "`", tupleType.getName(),
								refItem.getId());
						// Validation.NO_PARENT_RECORD_FOUND);
					}
				} else if (tupleAttribute.isMandatory()) {
					if (null == fKey)
						throw new FieldValidationException(
								key, tupleType.getName(), "Mandatory parameter `" + key
										+ "` is missing for the CI Type `" + tupleType.getName() + "`",
								Validation.MANDATORY);
					else
						throw new FieldValidationException(
								key, tupleType.getName(), "No reference record is found for the `" + key
										+ "` of the CI Type `" + tupleType.getName() + "`",
								Validation.MISSING_REFERENCE);
				}
			}
		}
		if (valueList.size() > 0)
		{
			String queryString = query.getQuery();
			logger.debug("Generated query {}", queryString);
			QueryParams params = new QueryParams();
			params.setQuery(queryString);
			params.setParams(valueList);
			return params;
		} else
			return null;
	}

	private class ExcludeVerifier implements BiConsumer<String, String> {
		@Override
		public void accept(String type, String fieldName) {
			if (excludeFields.contains(fieldName)) {
				throw new FieldValidationException(fieldName,type,  "Non Insertable field " + fieldName,
						Validation.NON_INSERTABLE);
			}
		}
	}
}
