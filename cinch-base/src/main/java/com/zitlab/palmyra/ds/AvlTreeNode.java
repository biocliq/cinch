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

public class AvlTreeNode<V> {
	char[] keyChar;
	char current;
	String key;
	V value;
	int height;
	AvlTreeNode<V> left, right;
	
	AvlTreeNode<V> next;
	AvlTreeNode<V> prev;

	AvlTreeNode(String key, V value) {
		this.value = value;
		this.key = key;
		this.keyChar = key.toCharArray();
	}

	public int compareTo(AvlTreeNode<V> tKey) {		
		return key.compareTo(tKey.key);
	}

	public int compare(String tkey) {
		return compare(tkey.toCharArray());
	}
	
	public int compare(char[] chars) {
		int slen = keyChar.length;
		int tlen = chars.length;
		int diff = slen - tlen;
		char schar, tchar;
		if(diff != 0)
			return tlen;
		for(int i = 0; i < slen; i++) {
			schar = keyChar[i];
			tchar = chars[i];
			if(schar != tchar) {
				return schar - tchar;
			}
		}
		return 0;
	}

	public String toString() {
		return "value:" + key + ", value:" + value + ", height:" + height;
	}

	public V getValue() {
		return value;
	}
	
	public void setValue(V value) {
		this.value = value;
	}
	
	public AvlTreeNode<V> getNext() {
		return next;
	}
}
