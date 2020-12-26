package com.zitlab.palmyra.cinch.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

public class EndObjectProcessor implements TokenProcessor {

	@Override
	public void process(JsonParser parser, ParserContext context) throws IOException {
		TupleContext ctx = context.pop();
		TupleContext prev = context.get();
		
		if (null != prev && prev.isObjectContext()) {			
			context.setFieldKey(ctx.getKey());
		}else {			
//			ctx.getChildren().add(ctx.getTuple());
//			ctx.setTuple(new TupleImpl());
		}
	}
}
