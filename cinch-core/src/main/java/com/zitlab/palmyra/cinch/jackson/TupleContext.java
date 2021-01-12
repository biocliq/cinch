package com.zitlab.palmyra.cinch.jackson;

import java.util.ArrayList;

import com.zitlab.palmyra.cinch.dbmeta.TupleType;
import com.zitlab.palmyra.cinch.pojo.Tuple;

class TupleContext {
	private Tuple tuple;
	private TupleType tupleType;
	private String key;
	private ArrayList<Tuple> children;
	
	public TupleContext(String key, TupleType tt, Tuple tuple) {
		this.key = key;
		this.tupleType = tt;
		this.tuple = tuple;
	}
	
	public TupleContext(String key, TupleType tt, Tuple tuple, ArrayList<Tuple> children) {
		this.key = key;
		this.tupleType = tt;
		this.tuple = tuple;
		this.children = children;
	}

	public Tuple getTuple() {
		return tuple;
	}
	
	public void setTuple(Tuple tuple) {
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
