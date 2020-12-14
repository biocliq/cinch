package com.zitlab.cinch.schema.config;

import java.util.HashMap;

public class TTCfg {
	private String type;
	private String option;
	private HashMap<String, TTUniqueKey> uniqueKey;
	private HashMap<String, TTChildCfg> children;
	private HashMap<String, TTRelationCfg> relations;
	private HashMap<String, TTReferenceCfg> parents;
	
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
	
	public HashMap<String, TTReferenceCfg> getParents() {
		return parents;
	}
	public void setParents(HashMap<String, TTReferenceCfg> parents) {
		this.parents = parents;
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
