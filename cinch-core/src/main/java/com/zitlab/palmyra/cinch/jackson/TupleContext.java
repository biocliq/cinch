package com.zitlab.palmyra.cinch.jackson;

import java.util.ArrayList;

import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.api2db.pojo.impl.TupleImpl;

class TupleContext {
	private TupleImpl tuple;
	private TupleType tupleType;
	private String key;
	private ArrayList<Tuple> children;
	
	public TupleContext(String key, TupleType tt, TupleImpl tuple) {
		this.key = key;
		this.tupleType = tt;
		this.tuple = tuple;
	}
	
	public TupleContext(String key, TupleType tt, TupleImpl tuple, ArrayList<Tuple> children) {
		this.key = key;
		this.tupleType = tt;
		this.tuple = tuple;
		this.children = children;
	}

	public TupleImpl getTuple() {
		return tuple;
	}
	
	public void setTuple(TupleImpl tuple) {
		this.tuple = tuple;
	}

	public TupleType getTupleType() {
		return tupleType;
	}

	public String getKey() {
		return key;
	}

	public ArrayList<Tuple> getChildren() {
		return children;
	}

	public boolean isObjectContext() {
		return null == children;
	}
}
