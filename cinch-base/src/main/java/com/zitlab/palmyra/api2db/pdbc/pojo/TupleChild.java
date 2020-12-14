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
package com.zitlab.palmyra.api2db.pdbc.pojo;

public class TupleChild {
	private TupleType childType;
	private TupleAttribute referenceAttribute;
	private String relation;
	
	public TupleChild(String relation, TupleType child, TupleAttribute reference) {
		this.relation = relation;
		this.childType = child;
		this.referenceAttribute = reference;
	}
	
	public TupleType getChildType() {
		return childType;
	}
	public void setChildType(TupleType childType) {
		this.childType = childType;
	}
	public TupleAttribute getReferenceAttribute() {
		return referenceAttribute;
	}
	public void setReferenceAttribute(TupleAttribute referenceAttribute) {
		this.referenceAttribute = referenceAttribute;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}	
}
