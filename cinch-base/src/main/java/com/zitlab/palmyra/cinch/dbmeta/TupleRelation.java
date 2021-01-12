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
package com.zitlab.palmyra.cinch.dbmeta;

public class TupleRelation {
	private TupleType relationTuple;
	private ForeignKey source;
	private ForeignKey target;
	private String relation;
		
	public TupleRelation(String relation, TupleType relationTuple,ForeignKey source, ForeignKey target) {
		this.relation = relation;
		this.relationTuple = relationTuple;
		this.source = source;
		this.target = target;
	}
	
	public boolean isSingle() {
		return null == target;
	}
	
	public boolean isMulti() {
		return null != target;
	}
	
	public TupleType getRelationTuple() {
		return relationTuple;
	}


	public void setRelationTuple(TupleType relationTuple) {
		this.relationTuple = relationTuple;
	}


	public ForeignKey getSource() {
		return source;
	}


	public void setSource(ForeignKey source) {
		this.source = source;
	}


	public ForeignKey getTarget() {
		return target;
	}


	public void setTarget(ForeignKey target) {
		this.target = target;
	}


	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}	
}
