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
package com.zitlab.palmyra.api2db.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zitlab.palmyra.api2db.exception.TupleTypeNotFoundException;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.sqlbuilder.dialect.Dialect;

public class Schema {
	private int id;
	private String name;
	private String customer;
	private String label;
	private String dbName;
	private String dbVersion;
	private int majorVersion;
	private int minorVersion;
	private Dialect dialect;
	private List<String> schemas = new ArrayList<String>();
	private Map<String, TupleType> entityMap = new HashMap<String, TupleType>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public TupleType getTableCfg(String name) {
		TupleType tt = entityMap.get(name);
		if(null != tt)
			return tt;
		throw new TupleTypeNotFoundException(name);
	}
	public void putTableCfg(String name, TupleType cfg) {
		entityMap.put(name, cfg);
	}
	public void setTableCfg(Map<String, TupleType> map) {
		this.entityMap = map;
	}
	public Map<String, TupleType> getTableCfg(){
		return this.entityMap;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbVersion() {
		return dbVersion;
	}
	public void setDbVersion(String dbVersion) {
		this.dbVersion = dbVersion;
	}
	public int getMajorVersion() {
		return majorVersion;
	}
	public void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}
	public int getMinorVersion() {
		return minorVersion;
	}
	public void setMinorVersion(int minorVersion) {
		this.minorVersion = minorVersion;
	}
	public Dialect getDialect() {
		return dialect;
	}
	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}
	public List<String> getSchemas() {
		return schemas;
	}
	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}
	public void addSchema(String schema) {
		this.schemas.add(schema);
	}
}
