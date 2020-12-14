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
package com.zitlab.cinch.schema;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zitlab.cinch.schema.config.SchemaConfigFileProcessor;
import com.zitlab.cinch.schema.config.TTCfg;
import com.zitlab.cinch.schema.config.TTChildCfg;
import com.zitlab.cinch.schema.config.TTReferenceCfg;
import com.zitlab.cinch.schema.config.TTRelationCfg;
import com.zitlab.cinch.schema.config.TTUniqueKey;
import com.zitlab.cinch.schema.metadata.DatabaseMetaInfo;
import com.zitlab.cinch.schema.metadata.DefaultDatabaseMetaInfo;
import com.zitlab.palmyra.api2db.exception.TupleTypeNotFoundException;
import com.zitlab.palmyra.api2db.exception.Validation;
import com.zitlab.palmyra.api2db.pdbc.pojo.ForeignKey;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleAttribute;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleRelation;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.sqlbuilder.dialect.DialectFactory;
import com.zitlab.palmyra.api2db.exception.PdbcException;

public class DefaultConfigFactory implements ConfigFactory {
	private Logger logger = LoggerFactory.getLogger(DefaultConfigFactory.class);
	private static Config config;

	public DefaultConfigFactory() {
	}

	public void remove(String appConfig) {
		config = null;
	}

	public Config getConfig() {
		return config;
	}

	public Config getConfig(String appContext) {
		return config;
	}

	public TupleType getTableCfg(String type) {
		TupleType result = null;
		if (null != config) {
			result = config.getTableCfg(type);
			if (null != result)
				return result;
			else
				throw new TupleTypeNotFoundException(
						"type " + type + " is not available in the Schema " + config.getName());
		} else
			throw new ConfigNotLoadedException("Schema not yet initialized");
	}

	public void addSchema(Config schma) {
		config = schma;
	}

	@Override
	public boolean isMulti() {
		return false;
	}

	@Override
	public void reload() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void reset() {
		config = new Config();
	}

	@Override
	public void load(String appContext, DataSource ds, List<String> schemas) {
		try {
			DatabaseMetaInfo metaInfo = new DefaultDatabaseMetaInfo(ds);
			SchemaConfigFileProcessor fileLoader = new SchemaConfigFileProcessor();
			Map<String, TupleType> tupleMap = metaInfo.getTupleTypes(schemas);
			Path filePath = Paths.get("D:\\");//to be changed for all filenames
			tupleMap.forEach((k,v)->{
				fileLoader.process(k, filePath);
			});
			config = new Config();
			config.setTableCfg(tupleMap);
			config.setDialect(DialectFactory.getDialect(ds));
			mergeMetaInfo(fileLoader.getConfList());
//			config.setConfList(fileLoader.getConfList());
		} catch (SQLException e) {
			throw new RuntimeException("error while loading " + appContext, e);
		}
	}
	public void mergeMetaInfo(List<TTCfg> confList) {
		for (TTCfg relation : confList) {
			String name = relation.getType();
			TupleType citype = getTableCfg(name);
			logger.info("processing citype " + name) ;
			if (null != citype) {
				assignOption(relation, citype);
				if (null != relation.getChildren()) {
					for (Entry<String, TTChildCfg> childcfg : relation.getChildren().entrySet()) {
						String relationName = childcfg.getKey();
//						logger.info("Processing child relation {}", relationName);
						citype.addRelation(relationName, getChild(relationName, childcfg.getValue(), citype, config));
					}
				}

				if (null != relation.getRelations()) {
					for (Entry<String, TTRelationCfg> relationcfg : relation.getRelations().entrySet()) {
						String relationName = relationcfg.getKey();
						citype.addRelation(relationName, getRelation(relationName, relationcfg.getValue(), config));
					}
				}

				if (null != relation.getParents()) {
					for (Entry<String, TTReferenceCfg> parentCfg : relation.getParents().entrySet()) {
						String relationName = parentCfg.getKey();
						if (!setParent(relationName, parentCfg.getValue(), citype)) {
							createParent(relationName, parentCfg.getValue(), citype, config);
						}
					}
				}
				
				if(null != relation.getUniqueKey()) {
					for (Entry<String, TTUniqueKey> uniqueCfg : relation.getUniqueKey().entrySet()) {
						String keyName = uniqueCfg.getKey();
						TTUniqueKey value = uniqueCfg.getValue();
						verifyKey(keyName, value, citype, config);
					}
				}
			}
		}
	}
	private void verifyKey(String keyName, TTUniqueKey value, TupleType citype, Config config) {
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

	private void createParent(String relationName, TTReferenceCfg value, TupleType citype, Config config) {
		logger.info("Creating parent relation {} for citype {} with target{}", relationName, citype.getName(),
				value.getType());

		String[] target = value.getTarget();
		String[] source = value.getSource();
		if (null == target || null == source || target.length != source.length) {
			logger.error("source and target length not matching");
			return;
		}

		TupleType tgtType = config.getTableCfg(value.getType());
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

	private TupleRelation getChild(String relation, TTChildCfg cfg, TupleType parent, Config config) {
		String[] src = cfg.getSource();
		String type = cfg.getType();
		TupleType ciType = config.getTableCfg(type);
		ForeignKey source = ciType.getForeignKeyByAttributes(src);
		if (null != source) {
			return new TupleRelation(relation, ciType, source, null);			
		}else {
			return createChild(relation, cfg, parent, config);
		}		
	}
	
	private TupleRelation createChild(String relationName, TTChildCfg value, TupleType parent, Config config) {
//		logger.info("Creating child relation {} for citype {} with target{}", relationName, parent.getName(),
//				value.getType());

		String[] target = value.getTarget();
		String[] source = value.getSource();
		if (null == target || null == source || target.length != source.length) {
			throw new PdbcException(Validation.OTHERS,"source and target length not matching");
		}

		TupleType tgtType = config.getTableCfg(value.getType());
		if (null == tgtType) {
			throw new PdbcException(Validation.OTHERS,"target citype " + value.getType() + " not found");
		}

		List<TupleAttribute> srcAttribs = new ArrayList<TupleAttribute>();
		List<TupleAttribute> tgtAttribs = new ArrayList<TupleAttribute>();
		for (String src : source) {
			TupleAttribute attrib = parent.getField(src);
			if (null == attrib) {
				throw new PdbcException(Validation.OTHERS,"source attribute " + src + " not found");			
			}
			srcAttribs.add(attrib);
		}

		for (String tgt : target) {
			TupleAttribute attrib = tgtType.getField(tgt);
			if (null == attrib) {
				throw new PdbcException(Validation.OTHERS,"target attribute " + tgt + " not found");				
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

	private TupleRelation getRelation(String relation, TTRelationCfg cfg, Config config) {
		String[] src = cfg.getSource();
		String[] tgt = cfg.getTarget();
		String type = cfg.getType();
		TupleType ciType = config.getTableCfg(type);
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
