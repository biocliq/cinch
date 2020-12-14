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

import java.util.Map;
import java.util.Objects;

public class Node<V> implements Map.Entry<String, V>, Comparable<String>{
	private String key;
	private V value;
	
	public Node(String key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String getKey() {
		return this.key;
	}
	@Override
	public V getValue() {
		return this.value;
	}
	@Override
	public V setValue(V newValue) {
		  V oldValue = value;
          value = newValue;
          return oldValue;
	}
	
	public final boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof Map.Entry) {
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
            if (Objects.equals(key, e.getKey()) &&
                Objects.equals(value, e.getValue()))
                return true;
        }
        return false;
    }
	
	public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }
	
	public final String toString() { return key + "=" + value; }

	@Override
	public int compareTo(String key2) {
		int l1 = key.length();
		int l2 = key2.length();
		return l1 != l2 ? l1 - l2 : key.compareTo(key2);
	}
}
