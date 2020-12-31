package com.zitlab.palmyra.cinch.jackson;

import java.util.ArrayList;

import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.cinch.rshandler.TupleFactory;

public class ParserContext {
	private ArrayList<TupleContext> context = new ArrayList<TupleContext>();

	private TupleContext tupleContext;
	
	private TupleFactory factory = TupleFactory.NOOP;

	private String fieldKey;

	public ParserContext(String key, TupleType tt, Tuple tuple) {
		tupleContext = new TupleContext(key, tt, tuple);
	}
	
	public ParserContext(String key, TupleType tt, Tuple tuple, ArrayList<Tuple> tuples) {
		tupleContext = new TupleContext(key, tt, tuple, tuples);
	}

	public void push(String key, TupleType tt, Tuple tuple) {		
		context.add(tupleContext);
		tupleContext = new TupleContext(key, tt, tuple);
	}

	public TupleContext pop() {
		TupleContext tmp = tupleContext;
		if(context.size() > 0)
			tupleContext = context.remove(context.size() - 1);
		else
			tupleContext = null;
		return tmp;
	}

	public TupleContext get() {
		return tupleContext;
	}

	public String getFieldKey() {
		return fieldKey;
	}

	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}

	public void push(TupleContext arrayContext) {		
		context.add(tupleContext);
		tupleContext = arrayContext;
	}
	
	public TupleFactory getFactory() {
		return factory;
	}

	public void setFactory(TupleFactory factory) {
		this.factory = factory;
	}

	public Tuple newTuple() {
		return factory.instance();
	}
}
