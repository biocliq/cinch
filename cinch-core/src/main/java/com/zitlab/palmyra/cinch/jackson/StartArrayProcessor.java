package com.zitlab.palmyra.cinch.jackson;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParser;
import com.zitlab.palmyra.cinch.dbmeta.TupleRelation;
import com.zitlab.palmyra.cinch.dbmeta.TupleType;
import com.zitlab.palmyra.cinch.pojo.Tuple;

public class StartArrayProcessor implements TokenProcessor {

	@Override
	public void process(JsonParser parser, ParserContext context) throws IOException {
		TupleContext ctx = context.get();
		TupleType tupleType = ctx.getTupleType();
		Tuple tuple = ctx.getTuple();
		TupleType childType = null;

		String childKey = context.getFieldKey();
		ArrayList<Tuple> childArray = new ArrayList<Tuple>();
		Tuple child = context.newTuple();
		TupleRelation relation;

		if (null != tupleType) {
			relation = tupleType.getRelation(childKey);
			if (null != relation) {
				childType = relation.getRelationTuple();
			}
		}

		TupleContext arrayContext = new TupleContext(childKey, childType, child, childArray);

		context.push(arrayContext);

		tuple.setChildren(childKey, childArray);
	}
}