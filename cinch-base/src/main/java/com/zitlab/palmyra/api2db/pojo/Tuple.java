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

package com.zitlab.palmyra.api2db.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.impl.TupleImpl;

/**
 * The base class for all the data operations. This class will carry the
 * information from the json format and transfer all the way to the database
 * object.
 * 
 * @author ksvraja
 *
 */

public interface Tuple extends Record, Serializable {

	public default Object getNonNullValuesAsMap(String[] keys) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Object value = null;
		String key = null;
		for (int i = 0; i < keys.length; i++) {
			key = keys[i];
			value = this.getAttribute(keys[i]);
			if (null == value)
				return null;
			map.put(key, value);
		}
		if (map.size() > 0)
			return map;
		return null;
	}
	
	public static Tuple newInstance() {
		return new TupleImpl();
	}

	public Object getId();
	
	public void setId(Object id);
	
	public Map<String, Tuple> getParent();

	public Tuple getParent(String key);

	public void setParent(Map<String, Tuple> parent);

	public void addParent(String key, Tuple value);

	public void removeParent(String key);

	public Map<String, List<Tuple>> getChildren();

	public List<Tuple> getChildren(String key);

	public void setChildren(Map<String, List<Tuple>> children);

	public void addChildren(String key, Tuple tuple);

	public void setChildren(String key, List<Tuple> tuples);

	public void setReference(String field, Tuple tuple);

	public void setParentAttribute(String ref, String field, Tuple value);

	public void setParentAttribute(String field, Tuple value);

	public Object getParentAttribute(String field);

	public Object removeParentAttribute(String field);

	public default Tuple getReference(String name) {
		return getParent(name);
	}

	public String getPreferredKey();

	public void setPreferredKey(String preferredKey);
	
	public void clear();

	public static Tuple newInstance(String type) {		
		return new TupleImpl(type);
	}

	public boolean isDbExists();
	
	public Tuple getDbTuple();
	
	public TupleType getTupleType();

	public void setDbExists(boolean b);

	public void setActionCode(int create);

	public void setDbTuple(Tuple item);
}