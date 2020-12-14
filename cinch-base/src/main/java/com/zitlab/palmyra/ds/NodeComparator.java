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

import java.util.Comparator;

public class NodeComparator<V> implements Comparator<Node<V>>{

	@Override
	public int compare(Node<V> o1, Node<V> o2) {
		String key1 = o1.getKey();
		String key2 = o2.getKey();
		int l1 = key1.length();
		int l2 = key2.length();
		return l1 != l2 ? l1 - l2 : key1.compareTo(key2);
	}

}
