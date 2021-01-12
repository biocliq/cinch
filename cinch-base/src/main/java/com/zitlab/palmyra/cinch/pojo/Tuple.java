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

package com.zitlab.palmyra.cinch.pojo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zitlab.palmyra.cinch.dbmeta.TupleType;
import com.zitlab.palmyra.cinch.security.AclFieldRight;
import com.zitlab.palmyra.cinch.util.TupleIDMapper;

/**
 * The base class for all the data operations. This class will carry the
 * information from the json format and transfer all the way to the database
 * object.
 * 
 * @author ksvraja
 *
 */

public class Tuple extends RecordImpl implements Serializable{

	private static final long serialVersionUID = 1659792920242277041L;

	public static final int DB_UNKNOWN = 0;
	public static final int DB_NOT_EXISTS = 1;
	public static final int DB_EXISTS = 2;

	private int dbExists = DB_UNKNOWN;

	private String preferredKey;

	private Object id;

	protected Map<String, Tuple> parentMap = new LinkedHashMap<String, Tuple>();

	protected Map<String, List<Tuple>> childrenMap = new LinkedHashMap<String, List<Tuple>>();
	
	private Map<String, AclFieldRight> aclFieldMap = new HashMap<String, AclFieldRight>();

	private Tuple dbTuple;

	private TupleType tupleType;
	
	public Tuple() {

	}

	public Tuple(int size) {
		super(size);
	}
	
	public Tuple(String type) {
		super.type = type;
	}

	public Tuple(String type, String id) {
		super.type = type;
		this.id = id;
	}

	public Tuple(String type, int attCount) {
		super(attCount);
		super.type = type;
	}

	public Object getId() {
		if (null != tupleType)
			return TupleIDMapper.getId(this, tupleType);
		else
			return this.id;
	}

	public void setId(Object id) {
		if (null == tupleType)
			this.id = id;
		else {
			this.id = null;
			TupleIDMapper.setId(this, tupleType, id);
		}
	}

	public Map<String, Tuple> getParent() {
		return parentMap;
	}

	public Tuple getParent(String key) {
		return parentMap.get(key);
	}

	public void setParent(Map<String, Tuple> parent) {
		this.parentMap = parent;
	}

	public void addParent(String key, Tuple value) {
		this.parentMap.put(key, value);
	}

	public void removeParent(String key) {
		this.parentMap.remove(key);
	}

	public Map<String, List<Tuple>> getChildren() {
		return childrenMap;
	}

	public List<Tuple> getChildren(String key) {
		return childrenMap.get(key);
	}

	public void setChildren(Map<String, List<Tuple>> children) {
		this.childrenMap = children;
	}

	public void addChildren(String key, Tuple tuple) {
		List<Tuple> childList = childrenMap.get(key);
		if (null != childList) {
			childList.add(tuple);
			return;
		} else {
			childList = new ArrayList<Tuple>();
			childrenMap.put(key, childList);
			childList.add(tuple);
		}
	}

	public void setChildren(String key, List<Tuple> tuples) {
		childrenMap.remove(key);
		childrenMap.put(key, tuples);
	}

	public int isDbExists() {
		return dbExists;
	}

	public void setDbExists(int dbExists) {
		this.dbExists = dbExists;
	}

	public void setParent(String field, Tuple tuple) {
		int index = field.indexOf('.');
		if (index < 0) {
			Tuple parent = parentMap.get(field);
			if (null == parent) {
				this.parentMap.put(field, tuple);
			} else {
				parent.getAttributes().putAll(tuple.getAttributes());
				parent.getParent().putAll(tuple.getParent());
				parent.getChildren().putAll(tuple.getChildren());
			}
		} else {
			String ref = field.substring(0, index);
			String _field = field.substring(index + 1);
			Tuple parent = parentMap.get(ref);
			if (null == parent) {
				parent = new Tuple();
				parentMap.put(ref, parent);
			}
			parent.setParent(_field, tuple);
		}
	}

	public void setParentAttribute(String ref, String field, Tuple value) {
		Tuple parent = parentMap.get(ref);
		if (null == parent) {
			parent = new Tuple();
			parentMap.put(ref, parent);
		}
		parent.setParentAttribute(field, value);
	}

	public void setParentAttribute(String field, Object value) {
		int index = field.indexOf('.');
		if (index > 0) {
			String ref = field.substring(0, index);
			String _field = field.substring(index + 1);
			Tuple parent = parentMap.get(ref);
			if (null == parent) {
				parent = new Tuple();
				parentMap.put(ref, parent);
			}
			parent.setParentAttribute(_field, value);
		} else {
			this.setAttribute(field, value);
		}
	}

	public Object getParentAttribute(String field) {
		int index = field.indexOf('.');
		if (index > 0) {
			String ref = field.substring(0, index);
			String _field = field.substring(index + 1);
			Tuple parent = parentMap.get(ref);
			if (null == parent) {
				return null;
			}
			return parent.getParentAttribute(_field);
		} else {
			return this.getAttribute(field);
		}
	}

	public Tuple getDbTuple() {
		return dbTuple;
	}

	public void setDbTuple(Tuple dbTuple) {
		this.dbTuple = dbTuple;
		this.dbExists = (null != dbTuple) ? DB_EXISTS : DB_NOT_EXISTS;
	}

	public Object removeParentAttribute(String attribute) {
		int index = attribute.indexOf('.');
		if (index > 0) {
			String ref = attribute.substring(0, index);
			String _field = attribute.substring(index + 1);
			Tuple parent = parentMap.get(ref);
			if (null == parent) {
				return null;
			}
			return parent.removeParentAttribute(_field);
		} else
			return this.removeAttribute(attribute);
	}

	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
		id = aInputStream.readObject();
		type = aInputStream.readUTF();
		attributes = (Map<String, Object>) aInputStream.readObject();
		parentMap = (Map<String, Tuple>) aInputStream.readObject();
		preferredKey = (String) aInputStream.readObject();
	}

	private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
		aOutputStream.writeObject(id);
		aOutputStream.writeUTF(type);
		aOutputStream.writeObject(attributes);
		aOutputStream.writeObject(parentMap);
		aOutputStream.writeObject(preferredKey);
	}

	public String getPreferredKey() {
		return preferredKey;
	}

	public void setPreferredKey(String preferredKey) {
		this.preferredKey = preferredKey;
	}

	public final Object getAttribute(String key) {
		return this.attributes.get(key);
	}

	public TupleType getTupleType() {
		return tupleType;
	}

	public void setTupleType(TupleType tupleType) {
		this.tupleType = tupleType;
	}

	public void addAclFieldRight(String attribute, AclFieldRight aclRight) {
		aclFieldMap.put(attribute, aclRight);
	}

	public Map<String, AclFieldRight> getAclFieldMap() {
		return aclFieldMap;
	}
	
	public void clear() {
		this.type = null;
		this.error = null;
		this.attributes.clear();
		this.preferredKey = null;
		this.parentMap.clear();
		this.childrenMap.clear();
		this.dbTuple = null;
		this.dbExists = DB_UNKNOWN;
		this.id = null;
	}

}