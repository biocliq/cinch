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
package com.zitlab.palmyra.cinch.schema.config;

import java.util.HashMap;

class TTCfg {
	private String type;
	private String option;
	private HashMap<String, TTUniqueKey> uniqueKey;
	private HashMap<String, TTChildCfg> children;
	private HashMap<String, TTRelationCfg> relations;
	private HashMap<String, TTReferenceCfg> references;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public HashMap<String, TTChildCfg> getChildren() {
		return children;
	}
	public void setChildren(HashMap<String, TTChildCfg> children) {
		this.children = children;
	}
	public HashMap<String, TTRelationCfg> getRelations() {
		return relations;
	}
	public void setRelations(HashMap<String, TTRelationCfg> relations) {
		this.relations = relations;
	}
	public HashMap<String, TTReferenceCfg> getReferences() {
		return references;
	}
	public void setReferences(HashMap<String, TTReferenceCfg> references) {
		this.references = references;
	}
	public HashMap<String, TTUniqueKey> getUniqueKey() {
		return uniqueKey;
	}
	public void setUniqueKey(HashMap<String, TTUniqueKey> uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
}
