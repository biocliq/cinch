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

public class AvlTreeTest {
	public static void main(String[] args) {
		AvlTree tree = new AvlTree();
		int i =0;
		
		tree.insert("grn", i++);
		tree.insert("billNumber", i++);
		tree.insert("recDate", i++);
		tree.insert("arecDate", i++);		
		tree.insert("brecDate", i++);		
		tree.insert("crecDate", i++);		
		tree.insert("drecDate", i++);
		tree.insert("erecDate", i++);
		tree.insert("frecDate", i++);
		tree.insert("grecDate", i++);
		tree.insert("irecDate", i++);
		tree.insert("jrecDate", i++);
		tree.insert("krecDate", i++);
		tree.insert("krecDate", i++);
		tree.preOrder();
		String sd = "arecDate";
		System.out.println(tree.get(sd));
		
		System.out.println(tree.get("grn"));
		tree.remove("grn");
		System.out.println(tree.get("grn"));
		
		System.out.println(tree.get(sd));
	}
}
