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
package com.zitlab.cinch.schema.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zitlab.cinch.schema.Config;
import com.zitlab.palmyra.api2db.exception.GenericValidation;
import com.zitlab.palmyra.api2db.exception.Validation;
import com.zitlab.palmyra.api2db.pdbc.pojo.ForeignKey;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleAttribute;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleRelation;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;

public class ConfigHelper  {
	private static final Logger logger = LoggerFactory.getLogger(ConfigHelper.class);
	
	public Config mergeAdditionalConfig(Config schema, List<TTCfg> relations) {
		
		for (TTCfg relation : relations) {
			String name = relation.getType();
			TupleType citype = schema.getTableCfg(name);
			logger.info("processing citype " + name) ;
			if (null != citype) {
				assignOption(relation, citype);
				if (null != relation.getChildren()) {
					for (Entry<String, TTChildCfg> childcfg : relation.getChildren().entrySet()) {
						String relationName = childcfg.getKey();
						logger.info("Processing child relation {}", relationName);
						citype.addRelation(relationName, getChild(relationName, childcfg.getValue(), citype, schema));
					}
				}

				if (null != relation.getRelations()) {
					for (Entry<String, TTRelationCfg> relationcfg : relation.getRelations().entrySet()) {
						String relationName = relationcfg.getKey();
						citype.addRelation(relationName, getRelation(relationName, relationcfg.getValue(), schema));
					}
				}

				if (null != relation.getReferences()) {
					for (Entry<String, TTReferenceCfg> parentCfg : relation.getReferences().entrySet()) {
						String relationName = parentCfg.getKey();
						if (!setParent(relationName, parentCfg.getValue(), citype)) {
							createParent(relationName, parentCfg.getValue(), citype, schema);
						}
					}
				}
				
				if(null != relation.getUniqueKey()) {
					for (Entry<String, TTUniqueKey> uniqueCfg : relation.getUniqueKey().entrySet()) {
						String keyName = uniqueCfg.getKey();
						TTUniqueKey value = uniqueCfg.getValue();
						verifyKey(keyName, value, citype, schema);
					}
				}
			}
		}
		
		for(TupleType tt : schema.getTableCfg().values()) {
			tt.postCreate();
		}
		
		return schema;
	}

	private void verifyKey(String keyName, TTUniqueKey value, TupleType citype, Config schema) {
		TupleAttribute attrib ;
		String[] fields = value.getFields();
		int size = fields.length;
		int count = 0;
		Map<String, Map<String, TupleAttribute>> uqKeys = citype.getUniqueKeyMap();
		
		for( Entry<String, Map<String, TupleAttribute>> keys: uqKeys.entrySet()) {
			Map<String, TupleAttribute> key = keys.getValue();
			count = 0;
			for(String field : value.getFields()) {
				attrib = key.get(field);
				if(null == attrib)
					break;
				count++;
			}
			if(count == size) {
				String _key = keys.getKey();
				Map<String, TupleAttribute> val = uqKeys.remove(_key);
				uqKeys.put(keyName, val);
				return;
			}
		}
	}

	private void createParent(String relationName, TTReferenceCfg value, TupleType citype, Config schema) {
		logger.info("Creating parent relation {} for citype {} with target{}", relationName, citype.getName(),
				value.getType());

		String[] target = value.getTarget();
		String[] source = value.getSource();
		if (null == target || null == source || target.length != source.length) {
			logger.error("source and target length not matching");
			return;
		}

		TupleType tgtType = schema.getTableCfg(value.getType());
		if (null == tgtType) {
			logger.error("target citype {} not found", value.getType());
			return;
		}

		List<TupleAttribute> srcAttribs = new ArrayList<TupleAttribute>();
		List<TupleAttribute> tgtAttribs = new ArrayList<TupleAttribute>();
		for (String src : source) {
			TupleAttribute attrib = citype.getField(src);
			if (null == attrib) {
				logger.error("source attribute {} not found", src);
				return;
			}
			srcAttribs.add(attrib);
		}

		for (String tgt : target) {
			TupleAttribute attrib = tgtType.getField(tgt);
			if (null == attrib) {
				logger.error("target attribute {} not found", tgt);
				return;
			}
			tgtAttribs.add(attrib);
		}

		ForeignKey fKey = new ForeignKey();
		fKey.setSource(srcAttribs);
		fKey.setTarget(tgtAttribs);
		fKey.setTargetSchema(tgtType.getSchema());
		fKey.setTargetTable(tgtType.getTable());
		fKey.setTargetType(tgtType);
		citype.addForeignKey(relationName, fKey);
	}

	private TupleRelation getChild(String relation, TTChildCfg cfg, TupleType parent, Config schema) {
		String[] src = cfg.getSource();
		String type = cfg.getType();
		TupleType ciType = schema.getTableCfg(type);
		ForeignKey source = ciType.getForeignKeyByAttributes(src);
		if (null != source) {
			return new TupleRelation(relation, ciType, source, null);			
		}else {
			return createChild(relation, cfg, parent, schema);
		}		
	}
	
	private TupleRelation createChild(String relationName, TTChildCfg value, TupleType parent, Config schema) {
		logger.info("Creating child relation {} for citype {} with target{}", relationName, parent.getName(),
				value.getType());

		String[] target = value.getTarget();
		String[] source = value.getSource();
		if (null == target || null == source || target.length != source.length) {
			throw new GenericValidation(Validation.OTHERS,"source and target length not matching");
		}

		TupleType tgtType = schema.getTableCfg(value.getType());
		if (null == tgtType) {
			throw new GenericValidation(Validation.OTHERS,"target citype " + value.getType() + " not found");
		}

		List<TupleAttribute> srcAttribs = new ArrayList<TupleAttribute>();
		List<TupleAttribute> tgtAttribs = new ArrayList<TupleAttribute>();
		for (String src : source) {
			TupleAttribute attrib = parent.getField(src);
			if (null == attrib) {
				throw new GenericValidation(Validation.OTHERS,"source attribute " + src + " not found");			
			}
			srcAttribs.add(attrib);
		}

		for (String tgt : target) {
			TupleAttribute attrib = tgtType.getField(tgt);
			if (null == attrib) {
				throw new GenericValidation(Validation.OTHERS,"target attribute " + tgt + " not found");				
			}
			tgtAttribs.add(attrib);
		}

		ForeignKey fKey = new ForeignKey();
		fKey.setSource(tgtAttribs);
		fKey.setTarget(srcAttribs);
		fKey.setTargetSchema(parent.getSchema());
		fKey.setTargetTable(parent.getTable());
		fKey.setTargetType(parent);
		//tgtType.addForeignKey(relationName, fKey);
		return new TupleRelation(relationName, tgtType, fKey, null);
	}

	private TupleRelation getRelation(String relation, TTRelationCfg cfg, Config schema) {
		String[] src = cfg.getSource();
		String[] tgt = cfg.getTarget();
		String type = cfg.getType();
		TupleType ciType = schema.getTableCfg(type);
		// TODO this mapping is wrong.. to be revisited.
		ForeignKey source = ciType.getForeignKeyByAttributes(src);
		ForeignKey target = ciType.getForeignKeyByAttributes(tgt);
		TupleRelation tr = new TupleRelation(relation, ciType, source, target);
		return tr;
	}

	private boolean setParent(String parent, TTReferenceCfg cfg, TupleType tupleType) {
		Map<String, ForeignKey> fKeyMap = tupleType.getForeignKeyMap();
		ForeignKey fKey = null;
		String key = null;

		for (Entry<String, ForeignKey> entry : fKeyMap.entrySet()) {
			fKey = entry.getValue();
			if (cfg.getType().equals(fKey.getTargetType().getName())) {
				if (isMatch(entry.getValue(), cfg.getSource())) {
					key = entry.getKey();
					break;
				}
			}
		}
		if (null != key) {
			fKeyMap.remove(key);
			tupleType.addForeignKey(parent, fKey);
			return true;
		}
		return false;
	}

	private boolean isMatch(ForeignKey fKey, String[] keys) {
		int size = keys.length;
		List<String> srcList = null;
		int index = 0;
		String key = null;

		srcList = fKey.getTargetAttributes();
		if (srcList.size() == size) {
			for (index = 0; index < size;) {
				key = keys[index];
				if (srcList.contains(key)) {
					index++;
				} else
					return false;
			}
			if (index == size)
				return true;
		}

		return false;
	}

	private void assignOption(TTCfg cfg, TupleType tupleType) {
		String options = cfg.getOption();
		if(null == options)
			return ;
		for(String option : options.split(",")) {
			switch (option) {
			case "master":
				tupleType.setMaster(true);
				break;
			case "history":
				tupleType.setHistory(true);
			case "system":
				tupleType.setSysTable(true);
			case "link":
				tupleType.setLink(true);

			default:
				break;
			}
		}
	}
}
