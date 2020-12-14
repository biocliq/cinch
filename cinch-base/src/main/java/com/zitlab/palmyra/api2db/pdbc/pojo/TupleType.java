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
package com.zitlab.palmyra.api2db.pdbc.pojo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zitlab.palmyra.util.StringBuilderCache;

public class TupleType {
	public static final int ACL_NONE = 0;
	public static final int ACL_TABLE = 1;
	public static final int ACL_COLUMN = 2;

	private static final int EDITABLE = 1;
	private static final int MASTER = 2;
	private static final int SYSTEM = 4;
	private static final int HISTORY = 8;
	private static final int LINK = 16;

	private String label;
	private String schema;
	private String table;
	private String name;
	private int id;

	private TupleAttribute autoIncrementkey = null;
	
	private String icon;

	private String seqFormat;
	// Database sequence name to fetch the sequence values
	private String seqName;
	private String seqFieldName;

	private int options = 1;

	/**
	 * ACL Granularity to be applied for this Tuple None - 0 Table - 1 Field - 2
	 */
	private int aclType;

	/**
	 * None - 0 Common changeLog table - 1 Grouped changeLog table - 2 Unique
	 * changeLog table - 3
	 */
	private int changeLogType;

	private String changLogTable;

	private boolean active;

	private String selectQuery;

	private String criteria;

	/**
	 * For single column primary key, the values will be stored in reference Map. --
	 * To be validated Otherwise, the values will be stored in the attributes of the
	 * tuple
	 */
	private List<TupleAttribute> primaryKey = new ArrayList<TupleAttribute>();

	/**
	 * Columns of the Table (including foreign key columns)
	 */
	private Map<String, TupleAttribute> fieldList = new LinkedHashMap<>(16, 0.6f);

	private List<TupleAttribute> singleFKeyList = new ArrayList<TupleAttribute>();

	/**
	 * Unique keys for this table.
	 */
	private Map<String, Map<String, TupleAttribute>> uniqueKeyMap;

	/**
	 * Foreign keys for this table.
	 */
	private Map<String, ForeignKey> foreignKeyMap = new LinkedHashMap<String, ForeignKey>();

	private Map<String, TupleRelation> relations = new LinkedHashMap<String, TupleRelation>();

	public TupleType() {

	}

	public TupleType(String table, String name, int isEditable) {
		this.table = table;
		this.name = name;
		this.setEditable(isEditable > 0);
		this.uniqueKeyMap = new LinkedHashMap<>();
	}

	public TupleType(String table, String name, boolean isEditable) {
		this.table = table;
		this.name = name;
		this.setEditable(isEditable);
		this.uniqueKeyMap = new LinkedHashMap<>();
	}

	public final void setTable(String table) {
		this.table = table;
	}

	public final void setTableName(String table) {
		this.table = table;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public Map<String, TupleAttribute> getUniqueKey(String key) {
		if (null == key)
			return getUniqueKey();
		return uniqueKeyMap.get(key);
	}

	public Map<String, TupleAttribute> getUniqueKey() {
		if (uniqueKeyMap.size() > 0) {
			return uniqueKeyMap.values().iterator().next();
		}
		return null;
	}

	public final Collection<String> getUQKeyList() {
		return this.uniqueKeyMap.keySet();
	}

	public final Map<String, Map<String, TupleAttribute>> getUniqueKeyMap() {
		return this.uniqueKeyMap;
	}

	public void addUniqueKey(String keyName, TupleAttribute uniqueKey) {
		Map<String, TupleAttribute> key = this.uniqueKeyMap.get(keyName);
		if (null == key) {
			key = new LinkedHashMap<>();
			this.uniqueKeyMap.put(keyName, key);
		}
		key.put(uniqueKey.getAttribute(), uniqueKey);
	}

	public synchronized void removeDuplicateUQKey() {
		Map<String, Map<String, TupleAttribute>> copyMap = new LinkedHashMap<String, Map<String, TupleAttribute>>();
		copyMap.putAll(uniqueKeyMap);
		List<String> finalKeys = new ArrayList<String>(uniqueKeyMap.size());
		List<String> duplKeys = new ArrayList<String>(4);

		List<String> pKeys = new ArrayList<String>();
		for(TupleAttribute attrib : fieldList.values()) {
			if(attrib.isPrimaryKey())
				pKeys.add(attrib.getAttribute());
		}
		List<String> pKeyNames = new ArrayList<String>();

		String src = null, compare = null;
		Map<String, TupleAttribute> srcMap, compareMap, pKey = null;
		for (Entry<String, Map<String, TupleAttribute>> copy : copyMap.entrySet()) {
			src = copy.getKey();
			if (duplKeys.contains(src))
				continue;
			srcMap = copy.getValue();
			if (pKeys.size() > 0) {
				if (srcMap.size() == pKeys.size()) {
					boolean _equal = true;
					for (String srcKey : pKeys) {
						if (!srcMap.containsKey(srcKey)) {
							_equal = false;
							break;
						}
					}
					if (_equal) {
						pKeyNames.add(src);
						continue;
					}
				}
			}

			for (Entry<String, Map<String, TupleAttribute>> entry : uniqueKeyMap.entrySet()) {
				compare = entry.getKey();
				compareMap = entry.getValue();

				if (src.equals(compare))
					continue;
				else {
					if (srcMap.size() == compareMap.size()) {
						boolean _equal = true;
						for (String srcKey : srcMap.keySet()) {
							if (!compareMap.containsKey(srcKey)) {
								_equal = false;
								break;
							}
						}
						finalKeys.add(src);
						if (_equal)
							duplKeys.add(compare);
					}
				}
			}
		}

		for (String key : duplKeys) {
			uniqueKeyMap.remove(key);
		}

		for (String pKeyname : pKeyNames) {
			pKey = uniqueKeyMap.remove(pKeyname);
		}
		if (null != pKey)
			uniqueKeyMap.put("primaryKey", pKey);
	}

	public Map<String, TupleAttribute> removeUniqueKey(String keyName) {
		if (null != keyName)
			return this.uniqueKeyMap.remove(keyName);
		return null;
	}

	public void setUniquekeys(Map<String, Map<String, TupleAttribute>> keys) {
		this.uniqueKeyMap = keys;
	}

	public final Map<String, TupleAttribute> getAllFields() {
		return fieldList;
	}

	public final List<TupleAttribute> getPrimaryKeyAttributes() {
		return primaryKey;
	}

	public List<String> getPrimaryKeyColumns() {
		ArrayList<String> result = new ArrayList<String>();
		for (TupleAttribute att : primaryKey) {
			result.add(att.getColumnName());
		}
		return result;
	}

	public final List<String> getPrimaryKeyAttributeNames() {
		ArrayList<String> result = new ArrayList<String>();
		for (TupleAttribute att : primaryKey) {
			result.add(att.getAttribute());
		}
		return result;
	}

	public final boolean isSinglePrimaryKey() {
		return primaryKey.size() == 1;
	}

	public final int getPrimaryKeyColumnCount() {
		return primaryKey.size();
	}

	public final TupleAttribute getField(String attributeName) {
		return fieldList.get(attributeName);
	}

	public TupleAttribute getRefField(String field) {
		int index = field.indexOf('.');
		if (index < 0) {
			return this.getField(field);
		} else {
			String ref = field.substring(0, index);
			String _field = field.substring(index + 1);
			TupleType tup = getForeignKeyTupleType(ref);
			if (null != tup)
				return tup.getRefField(_field);
		}
		return null;
	}

	public TupleType getReference(String field) {
		int index = field.indexOf('.');
		if (index < 0) {
			return null;
		} else {
			String ref = field.substring(0, index);
			return getForeignKeyTupleType(ref);
		}
	}

	public TupleAttribute getFieldByColumn(String column) {
		for (TupleAttribute attrib : fieldList.values()) {
			if (attrib.getColumnName().equals(column))
				return attrib;
		}
		return null;
	}

	public void addField(TupleAttribute fieldCfg) {
		this.fieldList.put(fieldCfg.getAttribute(), fieldCfg);
		if (fieldCfg.isPrimaryKey()) {
			this.primaryKey.add(fieldCfg);
		}
		if (fieldCfg.isAutoIncrement()) {
			this.autoIncrementkey = fieldCfg;
		}
	}

	public final String getTable() {
		return table;
	}

	public final String getName() {
		return name;
	}

	public final int getId() {
		return id;
	}

	public final void setId(int id) {
		this.id = id;
	}

	public final String getIcon() {
		return icon;
	}

	public final void setIcon(String icon) {
		this.icon = icon;
	}

	public final String getSeqFormat() {
		return seqFormat;
	}

	public final void setSeqFormat(String uniqueKeyFormat) {
		this.seqFormat = uniqueKeyFormat;
	}

	public final String getSeqFieldName() {
		return seqFieldName;
	}

	public final boolean hasSeqField() {
		return null != seqFieldName;
	}

	public final void setSeqFieldName(String uniqueKeyField) {
		this.seqFieldName = uniqueKeyField;
	}

	public final TupleAttribute getSeqField() {
		if (null != seqFieldName) {
			return getField(seqFieldName);
		}
		return null;
	}

	@Override
	public int hashCode() {
		if (0 != id) {
			return id;
		} else
			return super.hashCode();
	}

	public String getUniqueListAsJSON() {

		StringBuilder sb = StringBuilderCache.get();

		for (Entry<String, Map<String, TupleAttribute>> entry : uniqueKeyMap.entrySet()) {
			sb.append(entry.getKey());
			sb.append(":{");
			Map<String, TupleAttribute> value = entry.getValue();
			for (String field : value.keySet()) {
				sb.append(field);
				sb.append(" ");
			}
			sb.append("}  ");
		}

		return StringBuilderCache.release(sb);

	}

	public final void addRelation(String relation, TupleRelation tupleRelation) {
		this.relations.put(relation, tupleRelation);
	}

	public void addRelation(String relation, TupleType relationType, ForeignKey source, ForeignKey target) {
		TupleRelation tupleRelation = new TupleRelation(relation, relationType, source, target);
		this.relations.put(relation, tupleRelation);
	}

	public final TupleRelation getRelation(String key) {
		return relations.get(key);
	}

	public final Map<String, TupleRelation> getRelations() {
		return relations;
	}

	public final void setRelations(Map<String, TupleRelation> relations) {
		this.relations = relations;
	}

	public final int getOptions() {
		return options;
	}

	public final void setOptions(int options) {
		this.options = options;
	}

	public final int getAclType() {
		return aclType;
	}

	public final void setAclType(int aclType) {
		this.aclType = aclType;
	}

	public final boolean isEditable() {
		return (options & EDITABLE) > 0;
	}

	public final boolean isSysTable() {
		return (options & SYSTEM) > 0;
	}

	public final boolean isReadOnly() {
		return (options & EDITABLE) > 0;
	}

	public final boolean isMaster() {
		return (options & MASTER) > 0;
	}

	public final boolean isHistory() {
		return (options & HISTORY) > 0;
	}

	public final boolean isLink() {
		return (options & LINK) > 0;
	}

	public final void setSysTable(boolean flag) {
		options = flag ? (options | SYSTEM) : (options & ~SYSTEM);
	}

	public final void setEditable(boolean flag) {
		options = flag ? (options | EDITABLE) : (options & ~EDITABLE);
	}

	public void setReadOnly(boolean flag) {

	}

	public final void setMaster(boolean flag) {
		options = flag ? (options | MASTER) : (options & ~MASTER);
	}

	public final void setHistory(boolean flag) {
		options = flag ? (options | HISTORY) : (options & ~HISTORY);
	}

	public final void setLink(boolean flag) {
		options = flag ? (options | LINK) : (options & ~LINK);
	}

	public final boolean isAutoDeleteChild() {
		return false;
	}

	public final boolean isChangeLog() {
		return changeLogType > 0;
	}

	public final String getSeqName() {
		return seqName;
	}

	public final void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	public final int getChangeLogType() {
		return changeLogType;
	}

	public final void setChangeLogType(int changeLogType) {
		this.changeLogType = changeLogType;
	}

	public final String getChangLogTable() {
		return changLogTable;
	}

	public final void setChangLogTable(String changLogTable) {
		this.changLogTable = changLogTable;
	}

	public final boolean isActive() {
		return active;
	}

	public final void setActive(boolean active) {
		this.active = active;
	}

	public final String getSelectQuery() {
		return selectQuery;
	}

	public final void setSelectQuery(String selectQuery) {
		this.selectQuery = selectQuery;
	}

	public final String getCriteria() {
		return criteria;
	}

	public final void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public final String getLabel() {
		return label;
	}

	public final void setLabel(String label) {
		this.label = label;
	}

	public final String getSchema() {
		return schema;
	}

	public final void setSchema(String schema) {
		this.schema = schema;
	}

	public final Map<String, ForeignKey> getForeignKeyMap() {
		return foreignKeyMap;
	}

	public final ForeignKey getForeignKeyByKeyName(String key) {
		return foreignKeyMap.get(key);
	}

	public final ForeignKey getForeignKey(String key) {
		return foreignKeyMap.get(key);
	}

	public ForeignKey getForeignKeyByAttributes(String... keys) {
		int size = keys.length;
		List<String> srcList = null;
		int index = 0;
		String key = null;
		for (ForeignKey fKey : foreignKeyMap.values()) {
			srcList = fKey.getSourceAttributes();
			if (srcList.size() == size) {
				for (index = 0; index < size;) {
					key = keys[index];
					if (srcList.contains(key)) {
						index++;
					} else
						break;
				}
				if (index == size)
					return fKey;
			}
		}
		return null;
	}

	public ForeignKey getForeignKeyByTgtAttributes(String... keys) {
		int size = keys.length;
		List<String> srcList = null;
		int index = 0;
		String key = null;
		for (ForeignKey fKey : foreignKeyMap.values()) {
			srcList = fKey.getTargetAttributes();
			if (srcList.size() == size) {
				for (index = 0; index < size;) {
					key = keys[index];
					if (srcList.contains(key)) {
						index++;
					} else
						break;
				}
				if (index == size)
					return fKey;
			}
		}
		return null;
	}

	public TupleType getForeignKeyTupleType(String key) {
		ForeignKey fk = foreignKeyMap.get(key);
		if (null != fk)
			return fk.getTargetType();
		return null;
	}

	public void addForeignKey(String key, ForeignKey value) {
		value.setAlias(key);
		foreignKeyMap.put(key, value);
	}

	public final void setForeignKeyMap(Map<String, ForeignKey> foreignKeyMap) {
		this.foreignKeyMap = foreignKeyMap;
	}

	public final TupleAttribute getAutoIncrementkey() {
		return autoIncrementkey;
	}

	public final boolean isAutoIncrementPkey() {
		return null != autoIncrementkey;
	}

	public void postCreate() {
		singleFKeyList.clear();

		for (ForeignKey fKey : foreignKeyMap.values()) {
			if (fKey.isSingleColumn()) {
				TupleAttribute attrib = fKey.getSource().get(0);
				singleFKeyList.add(attrib);
			}
		}
	}

	public List<TupleAttribute> getSingleFKeyList() {
		return singleFKeyList;
	}

	public void setSingleFKeyList(List<TupleAttribute> singleFKeyList) {
		this.singleFKeyList = singleFKeyList;
	}

	public void refactorForeignKey() {
		ForeignKey fKey;
		Map<String, ForeignKey> fkeyMap = new HashMap<String, ForeignKey>();
		for(Entry<String, ForeignKey> entry: foreignKeyMap.entrySet()) {
			fKey = entry.getValue();
			if(fKey.isSingleColumn()) {
				fKey.setAlias(fKey.getSource().get(0).getAttribute());
			}
			else {
				fKey.setAlias(entry.getKey());
			}
			fkeyMap.put(fKey.getAlias(), fKey);
		}
		this.foreignKeyMap = fkeyMap;
	}
}