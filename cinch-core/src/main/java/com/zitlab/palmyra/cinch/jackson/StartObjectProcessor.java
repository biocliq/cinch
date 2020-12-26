package com.zitlab.palmyra.cinch.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.impl.TupleImpl;

public class StartObjectProcessor implements TokenProcessor {

	@Override
	public void process(JsonParser parser, ParserContext context) throws IOException {
		TupleContext ctx = context.get();
		TupleType tupleType = ctx.getTupleType();
		TupleImpl tuple = ctx.getTuple();
		if (ctx.isObjectContext()) {
			TupleType parentType = null;
			String parentKey = context.getFieldKey();
			TupleImpl parent = context.newTuple();
			if (null != tupleType)
				parentType = tupleType.getForeignKeyTupleType(parentKey);
			tuple.setParent(parentKey, parent);
			context.push(parentKey, parentType, parent);
		}else {
			TupleImpl child = new TupleImpl();
			ctx.getChildren().add(child);
			context.push("__array", tupleType, child);
		}
	}
}
