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

public class AvlTreeMap<V> extends AbstractMap<String, V> {

	private Set entries = null;

	// private ArrayList<Map.Entry<String, V>> list;
	private AvlTree<V> avlTree = new AvlTree<V>();

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
			return (key == null ? e.getKey() == null : key.equals(e.getKey()));
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

	public AvlTreeMap() {
		// list = new ArrayList();
	}

	public AvlTreeMap(Map map) {
		// list = new ArrayList();
		putAll(map);
	}

	public Set entrySet() {
		if (entries == null) {
			entries = new AbstractSet() {
				public void clear() {
					avlTree.clear();
				}

				public Iterator iterator() {
					return avlTree.iterator();
				}

				public int size() {
					return avlTree.size();
				}
			};
		}
		return entries;
	}

	public V put(String key, V value) {
		V oldValue;
		AvlTreeNode<V> node = avlTree.get(key);
		if (null == node) {
			avlTree.insert(key, value);
			return value;
		} else {
			oldValue = node.getValue();
			node.setValue(value);
		}

		return oldValue;
	}

	public V remove(Object obj) {
		String key = (String) obj;
		AvlTreeNode<V> node = avlTree.get(key);
		if (null != node) {
			avlTree.remove(key);
			return node.getValue();
		}
		return null;
	}

	public V get(Object obj) {
		String key = (String) obj;
		AvlTreeNode<V> node = avlTree.get(key);
		if (null != node)
			return node.getValue();
		else
			return null;
	}

	public Object clone() {
		return new AvlTreeMap(this);
	}
}
