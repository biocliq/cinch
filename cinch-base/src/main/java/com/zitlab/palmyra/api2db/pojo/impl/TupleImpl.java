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

package com.zitlab.palmyra.api2db.pojo.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.api2db.pojo.TupleMetaInfo;

/**
 * The base class for all the data operations. This class will carry the
 * information from the json format and transfer all the way to the database
 * object.
 * 
 * @author ksvraja
 *
 */

public class TupleImpl extends RecordImpl implements Tuple {

	private static final long serialVersionUID = 1659792920242277041L;

	private boolean dbExists = false;

	private String preferredKey;

	private Object id;

	private String error;

	protected Map<String, Tuple> parent = new LinkedHashMap<String, Tuple>();

	protected Map<String, List<Tuple>> children = new LinkedHashMap<String, List<Tuple>>();

	private Tuple dbTuple;

	private TupleType tupleType;
	
	public TupleImpl() {

	}

	public TupleImpl(int size) {
		super(size);
	}
	
	public TupleImpl(String type) {
		super.type = type;
	}

	public TupleImpl(String type, String id) {
		super.type = type;
		this.id = id;
	}

	public Object getId() {
		return this.id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Map<String, Tuple> getParent() {
		return parent;
	}

	public Tuple getParent(String key) {
		return parent.get(key);
	}

	public void setParent(Map<String, Tuple> parent) {
		this.parent = parent;
	}

	public void addParent(String key, Tuple value) {
		this.parent.put(key, value);
	}

	public void removeParent(String key) {
		this.parent.remove(key);
	}

	public Map<String, List<Tuple>> getChildren() {
		return children;
	}

	public List<Tuple> getChildren(String key) {
		return children.get(key);
	}

	public void setChildren(Map<String, List<Tuple>> children) {
		this.children = children;
	}

	public void addChildren(String key, Tuple tuple) {
		List<Tuple> childList = children.get(key);
		if (null != childList) {
			childList.add(tuple);
			return;
		} else {
			childList = new ArrayList<Tuple>();
			children.put(key, childList);
			childList.add(tuple);
		}
	}

	public void setChildren(String key, List<Tuple> tuples) {
		children.remove(key);
		children.put(key, tuples);
	}

	public TupleMetaInfo getMetainfo() {
		if (null == error)
			return null;
		TupleMetaInfo metaInfo = new TupleMetaInfo();
		metaInfo.setError(error);
		return metaInfo;
	}

	public void setMetainfo(TupleMetaInfo metainfo) {
		this.type = metainfo.getType();
	}

	public boolean isDbExists() {
		return dbExists;
	}

	public void setDbExists(boolean dbExists) {
		this.dbExists = dbExists;
	}

	public void setReference(String field, Tuple tuple) {
		int index = field.indexOf('.');
		if (index < 0) {
			Tuple reference = parent.get(field);
			if (null == reference) {
				this.parent.put(field, tuple);
			} else {
				reference.getAttributes().putAll(tuple.getAttributes());
				reference.getParent().putAll(tuple.getParent());
				reference.getChildren().putAll(tuple.getChildren());
			}
		} else {
			String ref = field.substring(0, index);
			String _field = field.substring(index + 1);
			Tuple reference = parent.get(ref);
			if (null == reference) {
				reference = new TupleImpl();
				parent.put(ref, reference);
			}
			reference.setReference(_field, tuple);
		}
	}

	public void setParentAttribute(String ref, String field, Tuple value) {

		Tuple reference = parent.get(ref);
		if (null == reference) {
			reference = new TupleImpl();
			parent.put(ref, reference);
		}
		reference.setParentAttribute(field, value);
	}

	@Override
	public void setParentAttribute(String field, Tuple value) {
		int index = field.indexOf('.');
		if (index > 0) {
			String ref = field.substring(0, index);
			String _field = field.substring(index + 1);
			Tuple reference = parent.get(ref);
			if (null == reference) {
				reference = new TupleImpl();
				parent.put(ref, reference);
			}
			reference.setParentAttribute(_field, value);
		} else {
			this.setAttribute(field, value);
		}
	}
	@Override
	public Object getParentAttribute(String field) {
		int index = field.indexOf('.');
		if (index > 0) {
			String ref = field.substring(0, index);
			String _field = field.substring(index + 1);
			Tuple reference = parent.get(ref);
			if (null == reference) {
				return null;
			}
			return reference.getParentAttribute(_field);
		} else {
			return this.getAttribute(field);
		}
	}

	public Tuple getDbTuple() {
		return dbTuple;
	}

	public void setDbTuple(Tuple dbTuple) {
		this.dbTuple = dbTuple;
		this.dbExists = (null != dbTuple);
	}

	public Tuple getReference(String name) {
		return parent.get(name);
	}

	@Override
	public Object removeParentAttribute(String attribute) {
		int index = attribute.indexOf('.');
		if (index > 0) {
			String ref = attribute.substring(0, index);
			String _field = attribute.substring(index + 1);
			Tuple reference = parent.get(ref);
			if (null == reference) {
				return null;
			}
			return reference.removeParentAttribute(_field);
		} else
			return this.removeAttribute(attribute);
	}

	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
		id = aInputStream.readObject();
		type = aInputStream.readUTF();
		attributes = (Map<String, Object>) aInputStream.readObject();
		parent = (Map<String, Tuple>) aInputStream.readObject();
		preferredKey = (String) aInputStream.readObject();
	}

	private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
		aOutputStream.writeObject(id);
		aOutputStream.writeUTF(type);
		aOutputStream.writeObject(attributes);
		aOutputStream.writeObject(parent);
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

	public void clear() {
		this.type = null;
		this.error = null;
		this.attributes.clear();
		this.preferredKey = null;
		this.parent.clear();
		this.children.clear();
		this.dbTuple = null;
		this.dbExists = false;
		this.id = null;
	}

	@Override
	public void setActionCode(int create) {
		
	}
	
	public int getActionCode() {
		return 0;
	}
}