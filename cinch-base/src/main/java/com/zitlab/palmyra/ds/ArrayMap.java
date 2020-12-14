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
package com.zitlab.palmyra.ds;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ArrayMap<V> extends AbstractMap<String, V> {

	private Set entries = null;

	private ArrayList<Map.Entry<String, V>> list;
	

	static class Entry<V> implements Map.Entry<String, V> {
		protected String key;
		protected V value;

		public Entry(String key, V value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		public V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		public boolean equals(Object o) {
			if (!(o instanceof Map.Entry)) {
				return false;
			}
			Map.Entry e = (Map.Entry) o;
			return (key == null ? e.getKey() == null : key.equals(e.getKey()))
					&& (value == null ? e.getValue() == null : value.equals(e.getValue()));
		}

		public int hashCode() {
			int keyHash = (key == null ? 0 : key.hashCode());
			int valueHash = (value == null ? 0 : value.hashCode());
			return keyHash ^ valueHash;
		}

		public String toString() {
			return key + "=" + value;
		}
	}

	public ArrayMap() {
		list = new ArrayList();
	}

	public ArrayMap(Map map) {
		list = new ArrayList();
		putAll(map);
	}

	public ArrayMap(int initialCapacity) {
		list = new ArrayList(initialCapacity);
	}

	public Set entrySet() {
		if (entries == null) {
			entries = new AbstractSet() {
				public void clear() {
					list.clear();
				}

				public Iterator iterator() {
					return list.iterator();
				}

				public int size() {
					return list.size();
				}
			};
		}
		return entries;
	}

	public V put(String key, V value) {
		int size = list.size();
		Map.Entry<String, V> entry = null;
		int i;

		String curKey;
		for (i = 0; i < size; i++) {
			entry = (Entry) (list.get(i));
			curKey = entry.getKey();
			if (0 == key.compareTo(curKey)) {
				break;
			}
		}

		V oldValue = null;
		if (i < size) {
			oldValue = entry.getValue();
			entry.setValue(value);
		} else {
			list.add(new Entry(key, value));
		}
		return oldValue;
	}

	public V get(Object obj) {
		String key = (String) obj;
		int size = list.size();
		Map.Entry<String, V> entry = null;
		int i;
		if (key == null) {
			for (i = 0; i < size; i++) {
				entry = (Entry<V>) (list.get(i));
				if (entry.getKey() == null) {
					return entry.getValue();
				}
			}
		} else {
			String curKey;
			for (i = 0; i < size; i++) {
				entry = (Entry<V>) (list.get(i));
				curKey = entry.getKey();
				if (0 == key.compareTo(curKey)) {
					return entry.getValue();
				}
			}
		}
		return null;
	}

	public Object clone() {
		return new ArrayMap(this);
	}
}
